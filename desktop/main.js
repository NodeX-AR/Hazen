const { app, BrowserWindow, session, shell, systemPreferences, Menu, protocol, net } = require('electron');
const path = require('path');
const { pathToFileURL } = require('url');

// The app and its bundled AI models are served from hazen://app/ so WebAssembly and fetch work offline.
protocol.registerSchemesAsPrivileged([{ scheme: 'hazen', privileges: { standard: true, secure: true, supportFetchAPI: true, corsEnabled: true, stream: true } }]);

function createWindow() {
  const win = new BrowserWindow({
    width: 480, height: 900, minWidth: 380, minHeight: 640,
    backgroundColor: '#000000', title: 'Hazen', autoHideMenuBar: true,
    icon: path.join(__dirname, 'build', 'icon.png'),
    webPreferences: { contextIsolation: true, sandbox: true, nodeIntegration: false }
  });
  win.loadURL('hazen://app/demo.html');
  win.webContents.setWindowOpenHandler(({ url }) => { shell.openExternal(url); return { action: 'deny' }; });
  win.webContents.on('will-navigate', (e, url) => { if (!url.startsWith('hazen://')) { e.preventDefault(); shell.openExternal(url); } });
}

app.whenReady().then(async () => {
  protocol.handle('hazen', (req) => {
    const u = new URL(req.url);
    let p = decodeURIComponent(u.pathname);
    if (p === '/' || p === '') p = '/demo.html';
    const file = path.normalize(path.join(__dirname, p));
    if (!file.startsWith(__dirname)) return new Response('Forbidden', { status: 403 });
    return net.fetch(pathToFileURL(file).toString());
  });
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
