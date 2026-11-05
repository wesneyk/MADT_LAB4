# Code Review Report: Notes Storage Android App

## 1. Review Overview
This review evaluated the complete architecture, source code, data layers, coroutines/flows, Jetpack Compose UI, and localization of the Notes Storage application.

### Key Strengths:
- Clean MVVM pattern with separation into database, model, repository, storage, UI, and ViewModel layers.
- Full Jetpack Compose Material 3 implementation supporting dynamic color and dark/light themes.
- Proper input validation preventing empty or whitespace-only note titles and content.
- Complete localization resources in English (`values`) and Russian (`values-ru`).
- Thread confinement using Kotlin Coroutines and Room `@Transaction` support.

---

## 2. Findings & Severity Ranking

| Rank | Severity | Location | Issue Description |
| :--- | :--- | :--- | :--- |
| **1** | **CRITICAL** | `JsonNotesStorage.kt` | `NoClassDefFoundError` on Android API 24/25 due to `java.nio.file.Files.move` usage with `minSdk = 24`. Causes fatal crash on Android 7.x. |
| **2** | **CRITICAL** | `NotesViewModel.kt` | Flow collection coroutine leak when switching storages. SQLite Room flow continues running in background and overwrites JSON state. |
| **3** | **HIGH** | `NotesViewModel.kt`, `NoteDetailScreen.kt` | Stale note displayed in `NoteDetailScreen` after editing because `selectedNote` was not updated in ViewModel on save. |
| **4** | **HIGH** | `NotesListScreen.kt` | Destructive "Sync SQLite → JSON" action lacks a confirmation dialog, risking immediate loss of JSON-only notes. |
| **5** | **MEDIUM** | `JsonNotesStorage.kt` | Tight coupling to Android `Context` prevented testing the production class in JVM unit tests, resulting in a duplicate mock. |
| **6** | **MEDIUM** | `NotesViewModel.kt` | Leaky abstraction downcasting `(repository as? NotesRepositoryImpl)?.initialize()`, and hardcoded instantiation without DI. |
| **7** | **LOW** | `NotesViewModel.kt` | Redundant manual list re-fetching calls after JSON mutations while flow observation was already in place. |
| **8** | **LOW** | `res/values/strings.xml` | Unused resource strings (`msg_storage_switched`, `error_empty_both`). |

---

## 3. Prioritized Recommendations
1. **[P1] Replace `java.nio.file.Files`** with an API 24-compatible atomic write mechanism (`.tmp`, `fd.sync()`, `.bak`, and `renameTo`) and support automatic `.bak` recovery.
2. **[P2] Fix Flow observation lifecycle in `NotesViewModel`** by tracking and canceling previous collection jobs on storage switch, and properly synchronizing `selectedNote`.
3. **[P3] Add confirmation dialog for SQLite → JSON sync** in `NotesListScreen` to safeguard secondary storage data.
4. **[P4] Directly unit test `JsonNotesStorage`** by providing a `filesDir: File` constructor.
5. **[P5] Clean up repository initialization and ViewModel dependencies** via a proper Factory pattern or DI.

---

## 4. Implemented Improvements

### Improvement 1: API 24/25 Crash Fix & Resilient Atomic File Storage (`JsonNotesStorage.kt`)
- **What was changed:** Replaced `java.nio.file.Files.move` with standard file-system atomic write: writes to `$fileName.tmp`, calls `fos.fd.sync()`, backs up the existing file to `$fileName.bak`, and executes atomic `renameTo`. If a crash occurs mid-write, `readNotesInternal` automatically restores the backup on next launch. Added secondary constructor accepting `File` directory for test decoupling.
- **Why:** Eliminates the fatal `NoClassDefFoundError` on Android 7.0/7.1 devices, guarantees data durability across unexpected process terminations, and enables direct unit testing without Android mocks.

### Improvement 2: Coroutine Flow Leak & Stale State Fix (`NotesViewModel.kt`)
- **What was changed:** 
  1. Maintained a `notesObservationJob: Job?` reference and cancelled any active job before launching a new flow collector in `loadNotesForCurrentStorage()`.
  2. Updated `selectedNote` directly in `saveNote()` with the saved note entity.
  3. Cleared `selectedNote` when switching storage modes or deleting the active note.
  4. Removed redundant imperative JSON list reloads.
- **Why:** Prevents Room SQLite flow emissions from polluting the JSON storage view, eliminates coroutine leaks, and ensures `NoteDetailScreen` immediately displays updated content and timestamps upon returning from `NoteEditScreen`.

---

## 5. Verification Plan
1. **Build:** Execute `./gradlew clean assembleDebug` to confirm clean compilation.
2. **Unit Tests:** Execute `./gradlew testDebugUnitTest` to verify repository, Room DAO, and JSON storage logic pass.
3. **Functional Checks:**
   - Verify CRUD operations in both SQLite and JSON modes.
   - Switch between storages multiple times to confirm strict data separation.
   - Edit an existing note, save, and confirm `NoteDetailScreen` immediately reflects the new title and content.
   - Test one-way synchronization from SQLite to JSON and verify both storages match.
   - Verify validation errors when submitting empty fields.
   - Switch device language to Russian and verify full UI localization.
