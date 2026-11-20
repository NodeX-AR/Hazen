# Installation

## Android
1. Open the latest [Release](../../../releases/latest) on your phone and download `app-release.apk`.
2. Open the file. If Android asks, allow "Install unknown apps" for your browser or file manager.
3. Open **Hazen** and allow Camera and Microphone.
Prefer Google Play when it is listed: it updates automatically.

## iPhone and iPad
- **App Store / TestFlight** once published (see `docs/STORE_PUBLISHING.md`).
- **No store yet?** Open the website in Safari, tap Share, then **Add to Home Screen**. Allow camera access when asked. Save photos with Share > Save Image.

## Windows
1. Download `Hazen-<version>-win-x64.exe` (installer) or the `portable` build from Releases.
2. Run it. If Windows SmartScreen warns about an unknown publisher, choose **More info > Run anyway** (the build is not code-signed unless the maintainer added a certificate).

## macOS
1. Download the `.dmg` (Apple Silicon: `arm64`, Intel: `x64`) and drag Hazen to Applications.
2. First launch: right-click the app > **Open** (unsigned builds are blocked by a double-click).
3. Allow Camera and Microphone in System Settings > Privacy & Security.

## Linux
- **AppImage:** `chmod +x Hazen-*.AppImage && ./Hazen-*.AppImage`
- **Debian/Ubuntu:** `sudo apt install ./Hazen-*.deb`
- Make sure your user can read the webcam (`/dev/video0`, group `video`).

## Browser (any device)
Open the live demo over HTTPS. Chrome, Edge, Safari and Firefox work; installing it as an app is available from the browser menu.

## Offline use
AI features download small models the first time you use them, then work from cache. Everything else works offline.

## Uninstall
Remove the app the normal way for your system. Photos you saved stay in your gallery or Downloads folder.
