# Answers for Google "Data safety" and Apple "App Privacy"

- Data collected by the developer: **None**. Photos, videos and audio stay on the device and are not sent to the developer.
- Data shared with third parties: **None** by the app itself.
- Tracking / ads: **No**. No analytics, no advertising ID, no third-party SDKs.
- Permissions to declare: Camera, Microphone, Photos (add/import).
- Network use: downloads AI models (Google storage, jsDelivr); Voice Shutter uses the system speech service (optional).
- Encryption: standard HTTPS only (iOS: ITSAppUsesNonExemptEncryption = false is already set).
- Target audience: general (13+). Content rating questionnaire: no violence, no user-generated content shared, no purchases.
