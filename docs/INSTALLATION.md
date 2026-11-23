# Installation

Every download below is a direct file link: click it and the exact file downloads, no detour through GitHub pages.
Base address: `https://github.com/NodeX-AR/Hazen/releases/latest/download/`

| Device | File |
|---|---|
| Android | `Hazen.apk` |
| Windows | `Hazen-Setup-x64.exe` (guided installer) or `Hazen-Portable-x64.exe` |
| macOS | `Hazen-mac-arm64.dmg` (Apple Silicon) or `Hazen-mac-x64.dmg` (Intel) |
| Linux | `Hazen-linux-x64.AppImage` or `Hazen-linux-x64.deb` |

## One-line installers
- macOS and Linux: `curl -fsSL https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.sh | sh`
- Windows (PowerShell): `irm https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.ps1 | iex`

## The on-device AI is already inside
The Android, Windows, macOS and Linux apps carry the AI engine and models inside the package, so nothing is downloaded after installing and everything works offline. In the browser (or an installed web app) Hazen shows a one-time **Setting up Hazen** screen that saves the AI to your device; after that it never downloads it again.

## Older and budget phones
Hazen detects low-memory and low-core devices on first run and switches on **Low-end mode**: 720p, 24 fps and lighter graphics. Switch it any time in Settings > Pro.

## Android
1. Tap the Android download on the website, or open `https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen.apk` on your phone.
2. Open the file. If Android asks, allow "Install unknown apps" for your browser or file manager.
3. Open **Hazen** and allow Camera and Microphone.
Prefer Google Play when it is listed: it updates automatically.

## iPhone and iPad
- **App Store / TestFlight** once published (see `docs/STORE_PUBLISHING.md`).
- **No store yet?** Open the website in Safari, tap Share, then **Add to Home Screen**. Allow camera access when asked. Save photos with Share > Save Image.

## Windows
1. Download `Hazen-Setup-x64.exe` (installer) or `Hazen-Portable-x64.exe`.
2. Run it. If Windows SmartScreen warns about an unknown publisher, choose **More info > Run anyway** (the build is not code-signed unless the maintainer added a certificate).

## macOS
1. Download the `.dmg` (Apple Silicon: `Hazen-mac-arm64.dmg`, Intel: `Hazen-mac-x64.dmg`) and drag Hazen into Applications.
2. First launch: right-click the app > **Open** (unsigned builds are blocked by a double-click).
3. Allow Camera and Microphone in System Settings > Privacy & Security.

## Linux
- **AppImage:** `chmod +x Hazen-*.AppImage && ./Hazen-*.AppImage`
- **Debian/Ubuntu:** `sudo apt install ./Hazen-*.deb`
- Make sure your user can read the webcam (`/dev/video0`, group `video`).

## Browser (any device)
Open the live demo over HTTPS. Chrome, Edge, Safari and Firefox work; installing it as an app is available from the browser menu.

## Offline use
The AI is installed with the app (or during the web setup screen), so everything works offline.

## Uninstall
Remove the app the normal way for your system. Photos you saved stay in your gallery or Downloads folder.
