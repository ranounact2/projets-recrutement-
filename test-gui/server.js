const express = require('express');
const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = process.env.PORT || 3000;

const PROJECT_ROOT = path.resolve(__dirname, '..');
const REPORT_DIR = path.join(PROJECT_ROOT, 'playwright-report');
const RESULTS_JSON_PATH = path.join(REPORT_DIR, 'results.json');
const TRACES_DIR_ROOT = path.join(PROJECT_ROOT, 'playwright-traces');
const TRACES_DIR_TARGET = path.join(PROJECT_ROOT, 'target', 'playwright-traces');
const TEST_RESULTS_DIR = path.join(PROJECT_ROOT, 'test-results');

// Ensure traces folder exists at root level
if (!fs.existsSync(TRACES_DIR_ROOT)) {
  fs.mkdirSync(TRACES_DIR_ROOT, { recursive: true });
}

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));
app.use('/report', express.static(REPORT_DIR));

// Track active test process
let activeProcess = null;
let currentRunState = {
  status: 'idle', // 'idle', 'running', 'completed', 'failed', 'stopped'
  startTime: null,
  endTime: null,
  activeTest: '',
  progress: 0,
  logs: [],
  mode: 'headless',
};

// Connected Server-Sent Events clients for live streaming
let sseClients = [];

function broadcastEvent(type, data) {
  const eventPayload = `event: ${type}\ndata: ${JSON.stringify(data)}\n\n`;
  sseClients.forEach(client => client.res.write(eventPayload));
}

function appendLog(text, level = 'info') {
  const logEntry = {
    timestamp: new Date().toLocaleTimeString(),
    text,
    level
  };
  currentRunState.logs.push(logEntry);
  if (currentRunState.logs.length > 1000) {
    currentRunState.logs.shift();
  }
  broadcastEvent('log', logEntry);
}

// Cross-platform command resolver for Playwright CLI
function getPlaywrightRunner() {
  const localCli = path.join(PROJECT_ROOT, 'node_modules', '@playwright', 'test', 'cli.js');
  if (fs.existsSync(localCli)) {
    return { cmd: process.execPath, argsPrefix: [localCli] };
  }
  const isWin = process.platform === 'win32';
  return { cmd: isWin ? 'npx.cmd' : 'npx', argsPrefix: ['playwright'] };
}

// Cross-platform file/URL opener helper
function openPathOrUrl(targetPathOrUrl) {
  const platform = process.platform;
  let cmd, args;
  if (platform === 'win32') {
    cmd = 'cmd.exe';
    args = ['/c', 'start', '""', targetPathOrUrl];
  } else if (platform === 'darwin') {
    cmd = 'open';
    args = [targetPathOrUrl];
  } else {
    cmd = 'xdg-open';
    args = [targetPathOrUrl];
  }
  try {
    const p = spawn(cmd, args, { detached: true, stdio: 'ignore' });
    p.unref();
  } catch (err) {
    console.error('Failed to open path:', targetPathOrUrl, err);
  }
}

// Parse Playwright JSON report file if available
function getParsedResults() {
  if (!fs.existsSync(RESULTS_JSON_PATH)) {
    return null;
  }
  try {
    const rawData = fs.readFileSync(RESULTS_JSON_PATH, 'utf-8');
    const json = JSON.parse(rawData);

    let total = 0;
    let passed = 0;
    let failed = 0;
    let skipped = 0;
    let duration = 0;
    const testCases = [];

    if (json.stats) {
      duration = Math.round(json.stats.duration || 0);
    }

    function processSuite(suite, parentTitle = '') {
      const suiteTitle = parentTitle ? `${parentTitle} > ${suite.title}` : suite.title;

      if (suite.specs) {
        suite.specs.forEach(spec => {
          spec.tests.forEach(testObj => {
            total++;
            const lastResult = testObj.results[testObj.results.length - 1] || {};
            const status = lastResult.status || testObj.status || 'unknown';

            if (status === 'passed') passed++;
            else if (status === 'failed' || status === 'timedOut') failed++;
            else if (status === 'skipped') skipped++;

            // Check for trace files or artifacts
            let tracePath = null;
            if (lastResult.attachments) {
              const traceAtt = lastResult.attachments.find(a => a.name === 'trace');
              if (traceAtt && traceAtt.path) {
                tracePath = traceAtt.path;
              }
            }

            // Fallback: search for trace zip file matching test title/spec in test-results or playwright-traces
            if (!tracePath) {
              const traces = getAvailableTraces();
              const sanitizedTitle = spec.title.toLowerCase().replace(/[^a-z0-9]/g, '');
              const matched = traces.find(t => t.name.toLowerCase().replace(/[^a-z0-9]/g, '').includes(sanitizedTitle));
              if (matched) {
                tracePath = matched.fullPath;
              }
            }

            const normFile = (spec.file || '').replace(/\\/g, '/');
            testCases.push({
              id: `${normFile}-${spec.title}`,
              title: spec.title,
              file: path.basename(normFile),
              fullPath: normFile,
              suite: suiteTitle,
              status,
              duration: lastResult.duration || 0,
              error: lastResult.error ? (lastResult.error.message || lastResult.error.stack || 'Test failed') : null,
              tracePath: tracePath ? tracePath.replace(/\\/g, '/') : null
            });
          });
        });
      }

      if (suite.suites) {
        suite.suites.forEach(child => processSuite(child, suiteTitle));
      }
    }

    if (json.suites) {
      json.suites.forEach(s => processSuite(s));
    }

    if (json.errors && json.errors.length > 0) {
      json.errors.forEach((err, idx) => {
        total++;
        failed++;
        const errFile = (err.location?.file || '').replace(/\\/g, '/');
        testCases.push({
          id: `error-${idx}`,
          title: `Erreur de Compilation / Syntaxe: ${path.basename(errFile || 'test')}`,
          file: path.basename(errFile || ''),
          fullPath: errFile,
          suite: 'Compilation',
          status: 'failed',
          duration: 0,
          error: err.message || err.stack || 'Erreur lors du chargement des fichiers de test',
          tracePath: null
        });
      });
    }

    const successRate = total > 0 ? Math.round((passed / total) * 100) : 0;

    return {
      summary: {
        total,
        passed,
        failed,
        skipped,
        successRate,
        duration,
        lastRunTime: fs.statSync(RESULTS_JSON_PATH).mtime
      },
      testCases
    };
  } catch (err) {
    console.error('Error reading results.json:', err);
    return null;
  }
}

// Search for trace zip files in all potential trace folders
function getAvailableTraces() {
  const traces = [];
  const searchDirs = [TRACES_DIR_ROOT, TRACES_DIR_TARGET, TEST_RESULTS_DIR];

  searchDirs.forEach(dir => {
    if (fs.existsSync(dir)) {
      try {
        const scan = (currentDir) => {
          const items = fs.readdirSync(currentDir, { withFileTypes: true });
          items.forEach(item => {
            const full = path.join(currentDir, item.name);
            if (item.isDirectory()) {
              scan(full);
            } else if (item.name.endsWith('.zip') || item.name === 'trace.zip') {
              const stat = fs.statSync(full);
              traces.push({
                name: item.name,
                fullPath: full,
                relativePath: path.relative(PROJECT_ROOT, full),
                size: (stat.size / (1024 * 1024)).toFixed(2) + ' MB',
                modifiedTime: stat.mtime
              });
            }
          });
        };
        scan(dir);
      } catch (err) {
        console.error('Error scanning trace dir:', dir, err);
      }
    }
  });

  return traces.sort((a, b) => b.modifiedTime - a.modifiedTime);
}

/* ------------------- REST API ENDPOINTS ------------------- */

// Live SSE Stream endpoint
app.get('/api/events', (req, res) => {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  res.flushHeaders();

  const clientId = Date.now();
  sseClients.push({ id: clientId, res });

  res.write(`event: state\ndata: ${JSON.stringify(currentRunState)}\n\n`);

  req.on('close', () => {
    sseClients = sseClients.filter(c => c.id !== clientId);
  });
});

// Current execution state
app.get('/api/status', (req, res) => {
  res.json({
    state: currentRunState,
    os: process.platform,
    projectRoot: PROJECT_ROOT
  });
});

// Current parsed results
app.get('/api/results', (req, res) => {
  const results = getParsedResults();
  res.json(results || { summary: { total: 0, passed: 0, failed: 0, skipped: 0, successRate: 0, duration: 0 }, testCases: [] });
});

// List trace files
app.get('/api/traces', (req, res) => {
  res.json(getAvailableTraces());
});

// Trigger Playwright Test Run
app.post('/api/run-tests', (req, res) => {
  if (activeProcess) {
    return res.status(400).json({ error: 'Une suite de tests est déjà en cours d\'exécution.' });
  }

  const { headed = false, grep = '', ui = false, browser = '' } = req.body || {};

  const runner = getPlaywrightRunner();
  const args = [...runner.argsPrefix];

  if (ui) {
    args.push('test', '--ui');
  } else {
    args.push('test');
    args.push('--reporter=html,json');
    args.push('--trace', 'retain-on-failure');
    if (headed) args.push('--headed');
    if (grep) args.push('-g', grep);
    if (browser && browser !== 'all') args.push('--project=' + browser);
  }

  // Remove old results file to avoid serving stale cached results during execution
  if (fs.existsSync(RESULTS_JSON_PATH)) {
    try { fs.unlinkSync(RESULTS_JSON_PATH); } catch (e) {}
  }

  currentRunState = {
    status: 'running',
    startTime: new Date().toISOString(),
    endTime: null,
    activeTest: 'Démarrage de la suite de tests Playwright...',
    progress: 5,
    logs: [],
    mode: ui ? 'ui' : (headed ? 'headed' : 'headless')
  };

  appendLog(`[GUI Runner] Lancement de la commande : ${runner.cmd} ${args.join(' ')}`, 'system');
  broadcastEvent('state', currentRunState);

  const env = {
    ...process.env,
    PLAYWRIGHT_JSON_OUTPUT_NAME: RESULTS_JSON_PATH
  };

  try {
    const useShell = runner.cmd.endsWith('.cmd') || runner.cmd.endsWith('.bat');
    activeProcess = spawn(runner.cmd, args, { cwd: PROJECT_ROOT, env, shell: useShell });
  } catch (err) {
    currentRunState.status = 'failed';
    currentRunState.endTime = new Date().toISOString();
    appendLog(`[Erreur] Impossible de lancer le processus: ${err.message}`, 'error');
    broadcastEvent('state', currentRunState);
    activeProcess = null;
    return res.status(500).json({ error: err.message });
  }

  activeProcess.stdout.on('data', (chunk) => {
    const text = chunk.toString();
    const lines = text.split(/\r?\n/).filter(Boolean);
    lines.forEach(line => {
      appendLog(line, 'stdout');
      if (line.includes('Running') || line.includes('✓') || line.includes('✘')) {
        currentRunState.activeTest = line.trim();
        broadcastEvent('state', currentRunState);
      }
    });
  });

  activeProcess.stderr.on('data', (chunk) => {
    const text = chunk.toString();
    const lines = text.split(/\r?\n/).filter(Boolean);
    lines.forEach(line => appendLog(line, 'stderr'));
  });

  activeProcess.on('close', (code) => {
    activeProcess = null;
    currentRunState.status = code === 0 ? 'completed' : 'failed';
    currentRunState.endTime = new Date().toISOString();
    currentRunState.progress = 100;
    currentRunState.activeTest = code === 0 ? 'Tests terminés avec succès !' : `Tests terminés avec le code de sortie ${code}`;

    appendLog(`[GUI Runner] Processus terminé avec le code de sortie : ${code}`, code === 0 ? 'success' : 'error');

    // Give OS 250ms buffer to finalize writing results.json to disk
    setTimeout(() => {
      const parsed = getParsedResults();
      broadcastEvent('state', currentRunState);
      broadcastEvent('results', parsed);
    }, 250);
  });

  res.json({ message: 'Lancement des tests effectué', state: currentRunState });
});

// Stop test execution
app.post('/api/stop-tests', (req, res) => {
  if (!activeProcess) {
    return res.status(400).json({ error: 'Aucun test en cours d\'exécution.' });
  }
  try {
    if (process.platform === 'win32') {
      spawn('taskkill', ['/pid', activeProcess.pid, '/f', '/t']);
    } else {
      activeProcess.kill('SIGTERM');
    }
    appendLog('[GUI Runner] Interruption des tests demandée par l\'utilisateur.', 'warning');
    currentRunState.status = 'stopped';
    currentRunState.endTime = new Date().toISOString();
    broadcastEvent('state', currentRunState);
    activeProcess = null;
    res.json({ message: 'Tests interrompus.' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Open HTML Report directly
app.post('/api/open-report', (req, res) => {
  const indexHtml = path.join(REPORT_DIR, 'index.html');
  if (fs.existsSync(indexHtml)) {
    openPathOrUrl(`http://localhost:${PORT}/report/index.html`);
    res.json({ message: 'Rapport HTML ouvert dans le navigateur.' });
  } else {
    // Try via npx playwright show-report
    const runner = getPlaywrightRunner();
    const args = [...runner.argsPrefix, 'show-report'];
    spawn(runner.cmd, args, { cwd: PROJECT_ROOT, shell: process.platform === 'win32' });
    res.json({ message: 'Lancement du rapport HTML Playwright...' });
  }
});

// Open Trace Viewer
app.post('/api/open-trace', (req, res) => {
  const { tracePath } = req.body || {};
  let targetTrace = tracePath;

  if (!targetTrace) {
    const traces = getAvailableTraces();
    if (traces.length > 0) {
      targetTrace = traces[0].fullPath;
    }
  }

  if (targetTrace && fs.existsSync(targetTrace)) {
    const runner = getPlaywrightRunner();
    const args = [...runner.argsPrefix, 'show-trace', targetTrace];
    spawn(runner.cmd, args, { cwd: PROJECT_ROOT, shell: process.platform === 'win32' });
    res.json({ message: `Lancement de Playwright Trace Viewer pour: ${path.basename(targetTrace)}` });
  } else {
    res.status(404).json({ error: 'Aucun fichier de trace (.zip) trouvé.' });
  }
});

// Open Traces Folder in OS Explorer / Finder
app.post('/api/open-folder', (req, res) => {
  const folder = fs.existsSync(TRACES_DIR_ROOT) ? TRACES_DIR_ROOT : PROJECT_ROOT;
  openPathOrUrl(folder);
  res.json({ message: 'Dossier ouvert.' });
});

app.listen(PORT, () => {
  console.log(`=======================================================`);
  console.log(` 🚀 Playwright Graphical Interface (GUI) Server`);
  console.log(` 🌐 Interface Web disponible sur : http://localhost:${PORT}`);
  console.log(` 💻 OS Détecté : ${process.platform}`);
  console.log(`=======================================================`);
});