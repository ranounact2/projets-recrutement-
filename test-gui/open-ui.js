const { spawn } = require('child_process');
const path = require('path');
const os = require('os');

const PORT = process.env.PORT || 3000;
const url = `http://localhost:${PORT}`;

console.log(`Starting Playwright GUI Dashboard on ${url}...`);

// Start backend server
const serverPath = path.join(__dirname, 'server.js');
const serverProcess = spawn(process.execPath, [serverPath], {
  stdio: 'inherit'
});

// Wait 1.5s for server to start, then open default browser
setTimeout(() => {
  const platform = process.platform;
  let cmd, args;

  if (platform === 'win32') {
    cmd = 'cmd.exe';
    args = ['/c', 'start', '""', url];
  } else if (platform === 'darwin') {
    cmd = 'open';
    args = [url];
  } else {
    cmd = 'xdg-open';
    args = [url];
  }

  try {
    const openProc = spawn(cmd, args, { detached: true, stdio: 'ignore' });
    openProc.unref();
  } catch (err) {
    console.error('Failed to open browser automatically:', err);
  }
}, 1500);

process.on('SIGINT', () => {
  serverProcess.kill();
  process.exit();
});
