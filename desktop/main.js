const { app, BrowserWindow, session, shell, systemPreferences, Menu } = require('electron');
const path = require('path');

function createWindow() {
  const win = new BrowserWindow({
    width: 480, height: 900, minWidth: 380, minHeight: 640,
    backgroundColor: '#000000', title: 'Hazen', autoHideMenuBar: true,
    icon: path.join(__dirname, 'build', 'icon.png'),
    webPreferences: { contextIsolation: true, sandbox: true, nodeIntegration: false }
  });
  win.loadFile(path.join(__dirname, 'demo.html'));
  win.webContents.setWindowOpenHandler(({ url }) => { shell.openExternal(url); return { action: 'deny' }; });
  win.webContents.on('will-navigate', (e, url) => { if (!url.startsWith('file://')) { e.preventDefault(); shell.openExternal(url); } });
}

app.whenReady().then(async () => {
  const allowed = ['media', 'fullscreen', 'clipboard-sanitized-write', 'display-capture'];
  session.defaultSession.setPermissionRequestHandler((wc, permission, cb) => cb(allowed.includes(permission)));
  session.defaultSession.setPermissionCheckHandler((wc, permission) => allowed.includes(permission));
  if (process.platform === 'darwin') {
    try { await systemPreferences.askForMediaAccess('camera'); await systemPreferences.askForMediaAccess('microphone'); } catch {}
  } else { Menu.setApplicationMenu(null); }
  createWindow();
  app.on('activate', () => { if (BrowserWindow.getAllWindows().length === 0) createWindow(); });
});
app.on('window-all-closed', () => { if (process.platform !== 'darwin') app.quit(); });
