# Building and releasing

All builds run on GitHub Actions. Push a tag to release:
```
git tag v1.0.0 && git push origin v1.0.0
```

| Workflow | Output | Secrets needed |
|---|---|---|
| `ci.yml` | checks the JavaScript parses, JSON is valid, files exist | none |
| `pages.yml` | publishes the website and live demo to GitHub Pages | none (enable Pages: Settings > Pages > GitHub Actions) |
| `android.yml` | debug APK always; signed AAB + APK with secrets | `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD` |
| `desktop.yml` | Windows (exe, portable), macOS (dmg, zip), Linux (AppImage, deb) | none (code-signing secrets optional) |
| `ios.yml` | simulator build check; TestFlight upload with secrets | see `docs/STORE_PUBLISHING.md` |

## Before the first release
1. Your name, email and GitHub user (NodeX-AR) are already filled in. Replace `app.hazen.camera` everywhere if you want a different app id.
2. Enable GitHub Pages (Actions source) and private vulnerability reporting (Settings > Security).
3. Make the Android keystore (command in `docs/STORE_PUBLISHING.md`) and add the four secrets.

## Local builds
- **Web:** open `demo.html` through any HTTPS or localhost server (camera needs a secure context): `python3 -m http.server`
- **Android:** `cp demo.html android/app/src/main/assets/index.html && cd android && gradle assembleDebug`
- **Desktop:** `cd desktop && npm install && npm start` (run) or `npm run dist` (installers in `desktop/dist`)
- **iOS:** on a Mac, `npm install && npm run ios`, then archive in Xcode

## Versioning
Semantic versions in tags (`v1.2.3`). Android `versionCode` is the Actions run number. Update `CHANGELOG.md` for every release.

## Desktop code signing (optional)
- Windows: add `WIN_CSC_LINK` and `WIN_CSC_KEY_PASSWORD` secrets (a code-signing certificate).
- macOS: add `CSC_LINK`, `CSC_KEY_PASSWORD` and notarization variables (`APPLE_ID`, `APPLE_APP_SPECIFIC_PASSWORD`, `APPLE_TEAM_ID`). Without them the installers still build but show an "unknown developer" warning.
