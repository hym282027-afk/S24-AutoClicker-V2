# S24 Auto Clicker
Android Studio source project for a generic single-point accessibility auto-tapper.

## Features
- Configurable absolute X/Y screen coordinates
- Configurable repeat interval (100ms to 24h)
- Start/stop controls and saved settings
- AccessibilityService gesture dispatch (no root required)

## Build
Open project in Android Studio with JDK 17, sync Gradle, select Build > Build APK(s). Install resulting debug APK on Android, then enable the service in Accessibility settings.

## Limitations
- This is source code, NOT a compiled APK; it has not been built or tested in this environment.
- No floating overlay or multi-point sequence; return to app to stop.
- Coordinates are raw screen pixels, not density-independent units.
- Android can stop services or restrict execution; locked-screen tapping is not supported.
- Do not use for repetitive spam or automation that violates third-party service rules.
