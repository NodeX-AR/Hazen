# Publishing Hazen to the app stores

One codebase, three outputs:

| Output | Built by | Goes to |
|---|---|---|
| Signed **AAB** (and APK) | `.github/workflows/android.yml` | Google Play (AAB), and every other Android store (APK) |
| **iOS app** (IPA, via Capacitor) | `.github/workflows/ios.yml` (macOS runner) | Apple App Store / TestFlight |
| **PWA** (`manifest.webmanifest`, `sw.js`, `icons/`) | host the repo on any HTTPS site (GitHub Pages) | Microsoft Store via pwabuilder.com, or "Add to Home screen" |

Everything you upload (name, description, icon, screenshots, privacy policy) is in `store/`.

## 0. Before the first upload (do this once)
1. **Package id is permanent.** It is `app.hazen.camera` (Android and iOS). Change it now if you prefer another one: `android/app/build.gradle.kts`, `AndroidManifest`/`MainActivity.kt` package, and `capacitor.config.json`.
2. **Privacy policy URL is required by every store.** Edit `store/privacy-policy.md`, host it (GitHub Pages is fine) and paste the URL.
3. Take real screenshots on a phone (see `store/listing.md` for sizes).

## 1. Google Play (25 USD one-time)
1. Create a developer account at play.google.com/console and verify your identity.
2. Make a signing key once (keep the file and passwords forever, back them up):
   `keytool -genkeypair -v -keystore hazen.jks -alias hazen -keyalg RSA -keysize 2048 -validity 10000`
3. GitHub repo > Settings > Secrets > Actions, add:
   `ANDROID_KEYSTORE_BASE64` (output of `base64 -w0 hazen.jks`), `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.
4. Push to `main` or a tag like `v1.0`. Download **Hazen-release-aab-and-apk** from Actions (or the Release page).
5. Play Console > Create app > upload `app-release.aab` (turn on Play App Signing), fill the store listing from `store/listing.md`, Data safety from `store/privacy-answers.md`, content rating, then submit.
- The build already targets **Android 16 (API 36)**, which Google Play has required for new apps and updates since 31 Aug 2026. Re-check the requirement each August.
- Personal developer accounts created recently may need a closed test (about 12 testers for 14 days) before production. Check the current rule in the Console.

## 2. Apple App Store (99 USD/year)
1. Enrol at developer.apple.com/programs. Then in App Store Connect create the app with bundle id `app.hazen.camera`.
2. Create in the Apple developer portal: an **Apple Distribution certificate** (export as .p12), an **App Store provisioning profile** for the bundle id, and an **App Store Connect API key** (.p8).
3. Add these repo secrets: `IOS_CERT_P12_BASE64`, `IOS_CERT_P12_PASSWORD`, `IOS_PROFILE_BASE64`, `APPLE_TEAM_ID`, `ASC_KEY_ID`, `ASC_ISSUER_ID`, `ASC_KEY_P8`.
4. Run **Build iOS** in Actions (or push a tag). It uploads to TestFlight. In App Store Connect, add screenshots and the privacy answers, pick the build, and submit for review.
- No Mac? The workflow runs on GitHub's Mac. With a Mac you can instead run `npm install && npm run ios` and archive in Xcode.
- The iOS workflow is written but **untested** by me. Expect to fix a signing detail on the first run.
- **Review risk:** Apple can reject apps that are mostly a website in a wrapper (guideline 4.2). Hazen has real camera, editing and on-device AI features, so say so in the review notes (`store/listing.md`). Adding a native touch (for example saving straight to Photos) lowers the risk further.
- On iPhone the app keeps photos in its Album; use **Share > Save Image/Video** to put them in Photos.

## 3. Other stores (they all accept the signed APK)
Samsung Galaxy Store, Huawei AppGallery, Amazon Appstore, Xiaomi GetApps, OPPO/vivo/Transsion stores, Aptoide, APKPure. Each needs its own free or low-cost developer account and the same listing text, icon and privacy policy. Note: Huawei devices have no Google services, but this app does not need them. Microsoft Store: put the hosted PWA URL into pwabuilder.com.

## 4. Tell the stores the truth about
- **Camera and microphone** use (required, already declared in the manifest / iOS permission texts).
- The AI features download small models from Google (storage.googleapis.com) and jsDelivr the first time. Say this in the privacy policy. Bundling the models inside the app would make it fully offline and simplify review.
- Photos stay on the device; nothing is uploaded by the app.
