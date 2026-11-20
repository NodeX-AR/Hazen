# Architecture

Hazen is one self-contained web app, `demo.html` (HTML + CSS + JavaScript, WebGL2 for the image pipeline). Platform wrappers only host it and add native saving/sharing.

```
              demo.html
   +-------------+--------------+----------------+
   | Android     | iOS          | Desktop        |  Web / PWA
   | Kotlin      | Capacitor    | Electron       |  GitHub Pages
   | WebView     | WKWebView    | Chromium       |  manifest + sw.js
   +-------------+--------------+----------------+
```

- **Image pipeline:** a WebGL2 shader grades live preview, photos and video (exposure, contrast, looks, LUT, sharpen/soften, vignette, grain). Auto Enhance adds an offset layer on top of the user's grade.
- **Capture:** photos are rendered through the same pipeline; the watermark is composited on the final image. Video is recorded from a canvas stream with the watermark and mixer audio chain baked in.
- **AI:** MediaPipe Tasks (face landmarks, gestures, selfie segmentation) run on-device.
- **Android bridge:** `MainActivity.kt` exposes `CarCamAndroid` (save to gallery, share, delete). The name is historical and kept for compatibility.
- **Storage:** items live in IndexedDB/the app sandbox; wrappers add gallery saving where the platform allows.
- **Release:** every wrapper copies `demo.html` at build time. There is no bundler, so the file in the repo is exactly what ships.
