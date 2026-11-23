<p align="center"><img src="branding/hazen-logo.svg" width="120" alt="Hazen logo"></p>

<h1 align="center">Hazen</h1>
<p align="center"><b>Pro camera, album and editor with on-device AI.</b><br>
Android &middot; iPhone &middot; Windows &middot; macOS &middot; Linux &middot; Web</p>

<p align="center">
<a href="https://github.com/NodeX-AR/hazen/actions/workflows/ci.yml"><img alt="CI" src="https://github.com/NodeX-AR/hazen/actions/workflows/ci.yml/badge.svg"></a>
<a href="https://github.com/NodeX-AR/hazen/releases/latest"><img alt="Release" src="https://img.shields.io/github/v/release/NodeX-AR/hazen?color=0A66C2"></a>
<a href="LICENSE"><img alt="License" src="https://img.shields.io/badge/license-use%20only%2C%20no%20modification-25D366"></a>
</p>

<p align="center"><a href="https://nodex-ar.github.io/hazen/">Website</a> &middot; <a href="https://nodex-ar.github.io/hazen/demo.html">Live demo</a> &middot; <a href="docs/INSTALLATION.md">Install</a> &middot; <a href="https://github.com/NodeX-AR/hazen/releases/latest">Downloads</a></p>

---

## What is Hazen?
Hazen is a camera, a photo/video album and an editor in one app. It runs the same code on every platform: a single self-contained web app (`demo.html`) wrapped for Android, iOS and desktop.

**Camera** photo, video, portrait, ultra (multi-frame) and night modes, lens-ring zoom, manual controls, histogram, zebra, focus peaking, looks and `.cube` LUTs, Auto Enhance that keeps your look, face lock, smile and gesture shutter, optional watermark.
**Editor** crop, adjust, looks, draw, text, stickers, video trim and speed, a Fairlight-style audio mixer (EQ, compressor, pan, meter), AI cut-out, background blur and smart frame.
**Album** favourites, albums, import and share.

Everything runs on your device. See [docs/FEATURES.md](docs/FEATURES.md).

## Get it
| Platform | How |
|---|---|
| Android | [`Hazen.apk`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen.apk) direct download, or Google Play |
| iPhone / iPad | App Store / TestFlight, or add the website to your Home Screen |
| Windows | [`Hazen-Setup-x64.exe`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-Setup-x64.exe) guided installer, or [portable](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-Portable-x64.exe) |
| macOS | [Apple Silicon `.dmg`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-mac-arm64.dmg) or [Intel `.dmg`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-mac-x64.dmg) |
| Linux | [`.AppImage`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-linux-x64.AppImage) or [`.deb`](https://github.com/NodeX-AR/Hazen/releases/latest/download/Hazen-linux-x64.deb) |
| Browser | open the [live demo](https://nodex-ar.github.io/hazen/demo.html) (HTTPS, camera permission needed) |

One-line install: `curl -fsSL https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.sh | sh` (macOS, Linux) or `irm https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.ps1 | iex` (Windows).
The on-device AI ships inside every app, so it works offline from the first launch and is tuned for low-end devices. Step-by-step: [docs/INSTALLATION.md](docs/INSTALLATION.md).

## Repository map
```
demo.html            the app (single file, all features)
index.html           project website (GitHub Pages)
android/             Android wrapper (Kotlin WebView) -> APK / AAB
desktop/             Electron wrapper -> Windows, macOS, Linux
capacitor.config.json, package.json   iOS wrapper (Capacitor)
manifest.webmanifest, sw.js           installable web app (PWA)
branding/, icons/, store/             logo, icons, store listing material
docs/                installation, building, architecture, store publishing
.github/             CI, release workflows, issue and PR templates
```

## Build it yourself
See [docs/BUILDING.md](docs/BUILDING.md). In short: push a tag like `v1.0.0` and GitHub Actions builds the APK/AAB, the desktop installers and the iOS build, and attaches them to the release.

## License
Hazen is free to **use**, but it is **not open source**: you may not modify it, repackage it or claim it as your own. Read [LICENSE](LICENSE) for the exact terms.

## Support, security, contributing
- Questions and bugs: [open an issue](https://github.com/NodeX-AR/hazen/issues/new/choose)
- Vulnerabilities: see [SECURITY.md](SECURITY.md) (please do not open a public issue)
- Contributions: see [CONTRIBUTING.md](CONTRIBUTING.md)
- Privacy: [store/privacy-policy.md](store/privacy-policy.md)

&copy; 2026 Aswanth R. All rights reserved.
