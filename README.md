# HealthScan AI

Offline-first Android health screening and monitoring application.

## Important scope

This project is a screening/monitoring framework. It does **not** diagnose disease and does not claim to scan the entire body. Results are observations and prompts for appropriate follow-up.

The first version includes:

- Offline local health database
- Symptoms
- Vital measurements
- Lab-report OCR
- Health timeline
- Basic safety rules
- Camera capture
- Health Connect integration
- Optional online synchronization
- WorkManager background sync
- Privacy-first local storage
- Placeholder ML interfaces ready for validated models

## Build

Requirements:

- Android Studio Quail 4 (or compatible)
- JDK 17
- Android SDK 36
- Gradle 9.6
- Android 8.0+ for the app; Health Connect requires Android 9+ for its own service.

Run:

```bash
./gradlew assembleDebug
```

APK:

`app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions

Push to GitHub and the workflow in `.github/workflows/build.yml` builds the debug APK.

For a signed release AAB, add:

- `ANDROID_KEYSTORE_BASE64`
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEY_PASSWORD`

The included workflow supports both debug APK and optional signed AAB.

## Online sync

Online sync is disabled by default.

In Settings, enter your HTTPS API base URL and enable sync. The Android client expects:

- `GET /v1/health/ping`
- `POST /v1/sync`

The included `server/` folder is a minimal Node/Express reference implementation. Replace it with a properly authenticated production backend before handling real health data.

## Medical/Regulatory note

Do not market this project as a diagnostic device until each intended medical function has been clinically validated and the applicable regulatory/privacy requirements have been addressed.
