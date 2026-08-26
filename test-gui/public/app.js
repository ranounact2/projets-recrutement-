document.addEventListener('DOMContentLoaded', () => {

  // DOM Elements
  const valTotal = document.getElementById('valTotal');
  const valTotalSub = document.getElementById('valTotalSub');
  const valPassed = document.getElementById('valPassed');
  const valPassedSub = document.getElementById('valPassedSub');
  const valFailed = document.getElementById('valFailed');
  const valFailedSub = document.getElementById('valFailedSub');
  const valSkipped = document.getElementById('valSkipped');
  const valSkippedSub = document.getElementById('valSkippedSub');
  const valRate = document.getElementById('valRate');
  const valRateSub = document.getElementById('valRateSub');
  const valDuration = document.getElementById('valDuration');
  const valLastTime = document.getElementById('valLastTime');

  const btnRunAll = document.getElementById('btnRunAll');
  const btnRunHeaded = document.getElementById('btnRunHeaded');
  const btnRunSearch = document.getElementById('btnRunSearch');
  const btnRunUI = document.getElementById('btnRunUI');
  const btnOpenReport = document.getElementById('btnOpenReport');
  const btnOpenTrace = document.getElementById('btnOpenTrace');
  const btnStop = document.getElementById('btnStop');

  const progressText = document.getElementById('progressText');
  const progressPercent = document.getElementById('progressPercent');
  const progressFill = document.getElementById('progressFill');

  const testTableBody = document.getElementById('testTableBody');
  const searchFilter = document.getElementById('searchFilter');
  const filterBtns = document.querySelectorAll('.filter-btn');

  const terminalView = document.getElementById('terminalView');
  const chkAutoScroll = document.getElementById('chkAutoScroll');
  const btnClearLogs = document.getElementById('btnClearLogs');

  const reportFrame = document.getElementById('reportFrame');
  const tracesList = document.getElementById('tracesList');
  const btnOpenFolder = document.getElementById('btnOpenFolder');

  const errorModal = document.getElementById('errorModal');
  const modalTitle = document.getElementById('modalTitle');
  const modalErrorStack = document.getElementById('modalErrorStack');
  const modalCloseBtn = document.getElementById('modalCloseBtn');
  const closeModal = document.getElementById('closeModal');
  const modalOpenTraceBtn = document.getElementById('modalOpenTraceBtn');

  const osText = document.getElementById('osText');
  const connStatus = document.getElementById('connStatus');

  let activeTracePathForModal = null;
  let allTestCases = [];
  let currentFilter = 'all';

  /* --- SSE Stream Connection --- */
  function connectSSE() {
    const eventSource = new EventSource('/api/events');

    eventSource.onopen = () => {
      connStatus.querySelector('.status-label').textContent = 'Serveur Actif';
      connStatus.style.opacity = '1';
    };

    eventSource.onerror = () => {
      connStatus.querySelector('.status-label').textContent = 'Serveur Hors-ligne';
      connStatus.style.opacity = '0.5';
    };

    eventSource.addEventListener('state', (e) => {
      const state = JSON.parse(e.data);
      updateRunStateUI(state);
    });

    eventSource.addEventListener('log', (e) => {
      const log = JSON.parse(e.data);
      appendTerminalLog(log);
    });

    eventSource.addEventListener('results', (e) => {
      const results = JSON.parse(e.data);
      if (results) {
        updateDashboard(results);
        reloadReportIframe();
        loadTraces();
      }
    });
  }

  /* --- System Info --- */
  async function initSystemInfo() {
    try {
      const res = await fetch('/api/status');
      const data = await res.json();
      if (data.os) {
        osText.textContent = `${data.os === 'darwin' ? 'macOS' : (data.os === 'win32' ? 'Windows' : data.os)} Compatible`;
      }
      if (data.state) {
        updateRunStateUI(data.state);
      }
    } catch (err) {
      console.error('System info fetch error:', err);
    }
  }

  /* --- Initial Results (100% Real Values) --- */
  async function loadInitialResults() {
    try {
      const res = await fetch('/api/results');
      const data = await res.json();
      updateDashboard(data);
    } catch (err) {
      console.error('Results fetch error:', err);
    }
  }

  /* --- Available Trace Files --- */
  async function loadTraces() {
    try {
      const res = await fetch('/api/traces');
      const traces = await res.json();
      renderTracesList(traces);
    } catch (err) {
      console.error('Traces fetch error:', err);
    }
  }

  /* --- UI State --- */
  function updateRunStateUI(state) {
    const isRunning = state.status === 'running';

    btnRunAll.disabled = isRunning;
    btnRunHeaded.disabled = isRunning;
    btnRunSearch.disabled = isRunning;
    btnRunUI.disabled = isRunning;
    btnStop.disabled = !isRunning;

    if (isRunning) {
      progressText.textContent = state.activeTest || 'Exécution de la suite de tests en cours...';
      const pct = Math.max(state.progress || 10, 15);
      progressPercent.textContent = `${pct}%`;
      progressFill.style.width = `${pct}%`;
    } else {
      if (state.status === 'completed') {
        progressText.textContent = 'Suite de tests exécutée avec succès';
        progressPercent.textContent = '100%';
        progressFill.style.width = '100%';
      } else if (state.status === 'failed') {
        progressText.textContent = 'Exécution terminée avec des erreurs';
        progressPercent.textContent = '100%';
        progressFill.style.width = '100%';
      } else if (state.status === 'stopped') {
        progressText.textContent = 'Exécution interrompue par l\'utilisateur';
        progressPercent.textContent = '0%';
        progressFill.style.width = '0%';
      } else {
        progressText.textContent = 'Prêt pour l\'exécution des tests';
        progressPercent.textContent = '0%';
        progressFill.style.width = '0%';
      }
    }
  }

  /* --- Dashboard Data Updater (100% Real Values) --- */
  function updateDashboard(data) {
    if (!data || !data.summary) {
      renderEmptyState();
      return;
    }
    const s = data.summary;

    if (!s.total || s.total === 0) {
      renderEmptyState();
      return;
    }

    valTotal.textContent = s.total;
    valTotalSub.textContent = `${s.total} cas de test au total`;

    valPassed.textContent = s.passed;
    const passPct = s.total > 0 ? Math.round((s.passed / s.total) * 100) : 0;
    valPassedSub.textContent = `${passPct}% du total`;

    valFailed.textContent = s.failed;
    const failPct = s.total > 0 ? Math.round((s.failed / s.total) * 100) : 0;
    valFailedSub.textContent = `${failPct}% d'échecs`;

    valSkipped.textContent = s.skipped;
    valSkippedSub.textContent = `${s.skipped} ignorés`;

    valRate.textContent = `${s.successRate}%`;
    valRateSub.textContent = s.successRate >= 80 ? 'Conforme aux exigences' : 'Vérification requise';

    valDuration.textContent = s.duration > 1000 ? `${(s.duration / 1000).toFixed(1)}s` : `${s.duration}ms`;

    if (s.lastRunTime) {
      const dateObj = new Date(s.lastRunTime);
      valLastTime.textContent = `Exécuté à ${dateObj.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })}`;
    } else {
      valLastTime.textContent = 'Jamais exécuté';
    }

    allTestCases = data.testCases || [];
    renderTestTable();
  }

  function renderEmptyState() {
    valTotal.textContent = '0';
    valTotalSub.textContent = 'Aucune exécution';

    valPassed.textContent = '0';
    valPassedSub.textContent = '0% du total';

    valFailed.textContent = '0';
    valFailedSub.textContent = '0% d\'échecs';

    valSkipped.textContent = '0';
    valSkippedSub.textContent = 'En attente';

    valRate.textContent = '0%';
    valRateSub.textContent = 'Score global';

    valDuration.textContent = '0.0s';
    valLastTime.textContent = 'Jamais exécuté';

    allTestCases = [];
    testTableBody.innerHTML = `
      <tr>
        <td colspan="5" class="empty-state-cell">
          <div class="empty-state-box">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><path d="M12 8v4"/><path d="M12 16h.01"/></svg>
            <p class="empty-title">Aucune donnée d'exécution enregistrée</p>
            <p class="empty-desc">Cliquez sur <strong>"Lancer Tous les Tests"</strong> ci-dessus pour effectuer un premier test réel.</p>
          </div>
        </td>
      </tr>
    `;
  }

  /* --- Render Test Cases Table --- */
  function renderTestTable() {
    const query = searchFilter.value.toLowerCase().trim();

    const filtered = allTestCases.filter(tc => {
      const matchSearch = tc.title.toLowerCase().includes(query) || (tc.file && tc.file.toLowerCase().includes(query));
      const matchStatus = currentFilter === 'all' || tc.status === currentFilter;
      return matchSearch && matchStatus;
    });

    if (filtered.length === 0) {
      testTableBody.innerHTML = `
        <tr>
          <td colspan="5" class="empty-state-cell">
            <p class="empty-title">Aucun test correspondant trouvé</p>
          </td>
        </tr>
      `;
      return;
    }

    testTableBody.innerHTML = filtered.map(tc => {
      let badgeHtml = '';
      if (tc.status === 'passed') {
        badgeHtml = `
          <span class="badge badge-passed">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
            RÉUSSI
          </span>`;
      } else if (tc.status === 'failed') {
        badgeHtml = `
          <span class="badge badge-failed">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            ÉCHOUÉ
          </span>`;
      } else {
        badgeHtml = `
          <span class="badge badge-skipped">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
            IGNORÉ
          </span>`;
      }

      const durationText = tc.duration > 1000 ? `${(tc.duration / 1000).toFixed(2)}s` : `${tc.duration}ms`;

      return `
        <tr>
          <td>${badgeHtml}</td>
          <td><strong>${escapeHtml(tc.title)}</strong></td>
          <td><span style="color: var(--text-muted); font-size: 0.82rem;">${escapeHtml(tc.file || '')}</span></td>
          <td>${durationText}</td>
          <td style="text-align: right;">
            <div style="display: flex; gap: 0.4rem; justify-content: flex-end;">
              ${tc.error ? `<button class="btn btn-danger btn-sm view-err-btn" data-id="${tc.id}" style="padding: 0.25rem 0.6rem; font-size: 0.78rem;">Détails Erreur</button>` : ''}
              <button class="btn btn-trace btn-sm open-trace-btn" data-trace="${tc.tracePath || ''}" style="padding: 0.25rem 0.6rem; font-size: 0.78rem;">Consulter Trace</button>
            </div>
          </td>
        </tr>
      `;
    }).join('');

    // Attach click handlers
    document.querySelectorAll('.view-err-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const id = e.target.getAttribute('data-id');
        const tc = allTestCases.find(t => t.id === id);
        if (tc) openErrorModal(tc);
      });
    });

    document.querySelectorAll('.open-trace-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const path = e.target.getAttribute('data-trace');
        triggerOpenTrace(path);
      });
    });
  }

  /* --- Render Trace Explorer --- */
  function renderTracesList(traces) {
    if (!traces || traces.length === 0) {
      tracesList.innerHTML = `
        <div class="empty-state-box" style="grid-column: 1 / -1;">
          <p class="empty-title">Aucun fichier de trace (.zip) disponible</p>
          <p class="empty-desc">Les traces seront créées automatiquement lors de l'exécution des tests.</p>
        </div>
      `;
      return;
    }

    tracesList.innerHTML = traces.map(t => `
      <div class="trace-card">
        <span class="trace-card-name">${escapeHtml(t.name)}</span>
        <div class="trace-card-meta">
          <div>Taille : ${t.size}</div>
          <div>Emplacement : ${escapeHtml(t.relativePath)}</div>
        </div>
        <button class="btn btn-trace btn-trace-item" data-path="${escapeHtml(t.fullPath)}">
          <svg class="btn-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 2 7 12 12 22 7 12 2"/><polyline points="2 17 12 22 22 17"/><polyline points="2 12 12 17 22 12"/></svg>
          <span>Ouvrir Trace Viewer</span>
        </button>
      </div>
    `).join('');

    document.querySelectorAll('.btn-trace-item').forEach(btn => {
      btn.addEventListener('click', (e) => {
        triggerOpenTrace(btn.getAttribute('data-path'));
      });
    });
  }

  /* --- Terminal Log Output --- */
  function appendTerminalLog(log) {
    const line = document.createElement('div');
    line.className = `log-row ${log.level === 'system' ? 'log-sys' : (log.level === 'stderr' || log.level === 'error' ? 'log-err' : (log.level === 'success' ? 'log-succ' : 'log-out'))}`;
    line.textContent = `[${log.timestamp}] ${log.text}`;
    terminalView.appendChild(line);

    if (chkAutoScroll.checked) {
      terminalView.scrollTop = terminalView.scrollHeight;
    }
  }

  /* --- API Action Triggers --- */
  async function runTests(options = {}) {
    try {
      const res = await fetch('/api/run-tests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(options)
      });
      const data = await res.json();
      if (res.ok) {
        updateRunStateUI(data.state);
      } else {
        alert('Erreur: ' + data.error);
      }
    } catch (err) {
      alert('Impossible de contacter le serveur GUI.');
    }
  }

  async function stopTests() {
    try {
      await fetch('/api/stop-tests', { method: 'POST' });
    } catch (err) {
      console.error(err);
    }
  }

  async function triggerOpenReport() {
    try {
      await fetch('/api/open-report', { method: 'POST' });
    } catch (err) {
      console.error(err);
    }
  }

  async function triggerOpenTrace(tracePath) {
    try {
      const res = await fetch('/api/open-trace', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tracePath })
      });
      const data = await res.json();
      if (!res.ok) alert(data.error || 'Erreur lors de l\'ouverture de la trace');
    } catch (err) {
      console.error(err);
    }
  }

  async function triggerOpenFolder() {
    try {
      await fetch('/api/open-folder', { method: 'POST' });
    } catch (err) {
      console.error(err);
    }
  }

  function reloadReportIframe() {
    reportFrame.src = '/report/index.html?t=' + Date.now();
  }

  /* --- Error Modal --- */
  function openErrorModal(tc) {
    modalTitle.textContent = `Détails de l'échec : ${tc.title}`;
    modalErrorStack.textContent = tc.error || 'Aucun détail de pile d\'erreur.';
    activeTracePathForModal = tc.tracePath;
    errorModal.classList.add('active');
  }

  function closeModalWindow() {
    errorModal.classList.remove('active');
    activeTracePathForModal = null;
  }

  modalCloseBtn.addEventListener('click', closeModalWindow);
  closeModal.addEventListener('click', closeModalWindow);
  modalOpenTraceBtn.addEventListener('click', () => {
    triggerOpenTrace(activeTracePathForModal);
    closeModalWindow();
  });

  /* --- Toolbar Listeners --- */
  btnRunAll.addEventListener('click', () => runTests({ headed: false }));
  btnRunHeaded.addEventListener('click', () => runTests({ headed: true }));
  btnRunSearch.addEventListener('click', () => runTests({ headed: true, grep: 'recherche' }));
  btnRunUI.addEventListener('click', () => runTests({ ui: true }));

  btnOpenReport.addEventListener('click', triggerOpenReport);
  btnOpenTrace.addEventListener('click', () => triggerOpenTrace(null));
  btnOpenFolder.addEventListener('click', triggerOpenFolder);
  btnStop.addEventListener('click', stopTests);
  btnClearLogs.addEventListener('click', () => {
    terminalView.innerHTML = '';
  });

  // Filter & Search
  searchFilter.addEventListener('input', renderTestTable);

  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      currentFilter = btn.getAttribute('data-filter');
      renderTestTable();
    });
  });

  // Tabs Switcher
  const tabItems = document.querySelectorAll('.tab-item');
  const tabPanels = document.querySelectorAll('.tab-panel');

  tabItems.forEach(item => {
    item.addEventListener('click', () => {
      tabItems.forEach(i => i.classList.remove('active'));
      tabPanels.forEach(p => p.classList.remove('active'));

      item.classList.add('active');
      const tabId = item.getAttribute('data-tab');
      document.getElementById(tabId).classList.add('active');
    });
  });

  function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }

  // Init
  connectSSE();
  initSystemInfo();
  loadInitialResults();
  loadTraces();

});