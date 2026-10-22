# TuliaMind

A mental-wellness Android app built with **Kotlin + Jetpack Compose**.  
Features include mood tracking, guided breathing, a journaling screen, a streak tracker, panic support, a therapist-booking flow, an OSM map, and an interactive Android-components lab.

---

## Prerequisites

| Tool | Minimum version | Notes |
|------|----------------|-------|
| Android Studio | Ladybug (2024.2) or newer | Recommended: latest stable |
| Android SDK | API 37 (compileSdk) | Install via SDK Manager |
| JDK | 11 | Bundled with Android Studio |
| Kotlin | 2.2.10 | Managed by Gradle |
| Android Gradle Plugin | 9.4.0 | Managed by Gradle |
| Git | Any recent version | For pushing to GitHub |

> **minSdk is 24** — the app runs on Android 7.0 (Nougat) and above.

---

## Getting started locally

```bash
# 1. Clone the repo
git clone https://github.com/<your-username>/TuliaMind.git
cd TuliaMind

# 2. Open in Android Studio
#    File → Open → select the TuliaMind folder

# 3. Let Gradle sync (it downloads all dependencies automatically)

# 4. Run on an emulator or device (API 24+)
```

No manual dependency downloads are needed — Gradle fetches everything from Maven Central and Google's Maven repository.

---

## Pushing to GitHub for the first time

### 1. Create a repository on GitHub
Go to [github.com/new](https://github.com/new), give it a name (e.g. `TuliaMind`), leave it **empty** (no README, no .gitignore), and click **Create repository**.

### 2. Initialise Git locally (if not already done)

```bash
cd path/to/TuliaMind

git init
git add .
git commit -m "Initial commit"
```

### 3. Add the remote and push

```bash
git remote add origin https://github.com/<your-username>/TuliaMind.git
git branch -M main
git push -u origin main
```

### 4. Authenticate
GitHub no longer accepts passwords over HTTPS. Use one of:
- **Personal Access Token (PAT)** — generate at *Settings → Developer settings → Personal access tokens* and use it as the password when prompted.
- **GitHub CLI** — run `gh auth login` before pushing.
- **SSH key** — add your public key at *Settings → SSH and GPG keys* and use the `git@github.com:…` remote URL.

---

## What is already excluded from Git

The `.gitignore` at the root already excludes the files that must **not** be committed:

| Excluded path | Why |
|--------------|-----|
| `local.properties` | Contains your machine's local Android SDK path |
| `.gradle/` | Gradle cache — regenerated on each sync |
| `/build`, `app/build/` | Compiled outputs |
| `*.iml` | IDE module files |
| `.idea/workspace.xml`, `caches/`, etc. | Per-developer IDE state |
| `.externalNativeBuild`, `.cxx` | Native build artefacts |

**Do not add `local.properties` to the repo.** Anyone who clones the project will have their own SDK path set automatically by Android Studio.

---

## Project structure

```
TuliaMind/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/tuliamind/
│           ├── MainActivity.kt          # Single activity entry point
│           ├── AppNav.kt                # Navigation graph (all routes)
│           ├── Components.kt            # Shared UI components
│           ├── Db.kt                    # Local data helpers
│           ├── Extras.kt                # Utility extensions
│           ├── LabState.kt              # Lifecycle log state
│           ├── AboutActivity.kt
│           ├── MeditationTimerService.kt
│           ├── MoodProvider.kt          # ContentProvider
│           ├── ReminderReceiver.kt      # BroadcastReceiver
│           ├── screens/                 # One file per screen
│           │   ├── HomeScreen.kt
│           │   ├── MoodScreen.kt
│           │   ├── JournalScreen.kt
│           │   ├── BreathingScreen.kt
│           │   ├── QuizScreen.kt
│           │   ├── PanicScreen.kt
│           │   ├── StreakScreen.kt
│           │   ├── HelpScreen.kt
│           │   ├── BookingScreen.kt
│           │   ├── MapScreen.kt         # OSMDroid map
│           │   ├── WebScreen.kt
│           │   ├── LabScreen.kt         # Android-components lab
│           │   └── ...
│           └── ui/theme/
│               ├── Color.kt
│               ├── Theme.kt
│               └── Type.kt
├── gradle/libs.versions.toml            # Version catalogue
├── build.gradle.kts
├── settings.gradle.kts
└── .gitignore
```

---

## Key dependencies

| Library | Version |
|---------|---------|
| Jetpack Compose BOM | 2026.02.01 |
| AndroidX Navigation Compose | 2.8.5 |
| OSMDroid (offline maps) | 6.1.20 |
| Material3 | via Compose BOM |
| AndroidX Core KTX | 1.10.1 |
| Lifecycle Runtime KTX | 2.6.1 |

---

## Permissions declared in the manifest

| Permission | Used for |
|-----------|---------|
| `INTERNET` | Map tiles (OSMDroid), web screen |
| `ACCESS_NETWORK_STATE` | Network checks for map/web features |

---

## License

_Add your preferred license here (e.g. MIT, Apache 2.0)._
