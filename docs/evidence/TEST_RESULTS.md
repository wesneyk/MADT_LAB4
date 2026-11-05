# Test results: Notes Storage (after the Session 2 code review changes)

Run by the Claude Code agent on 2026-11-05, 21:18–21:30 (UTC+02:00). The agent did not change any source code.

- Code under test: `android-app` after Session 1 (Antigravity) and the two Session 2 changes (Google AI Studio), see `docs/evidence/ai_studio/session2_review_changes.diff`.
- Build: Gradle wrapper from the command line with JAVA_HOME = Android Studio JBR (`gradlew clean assembleDebug testDebugUnitTest`). The build in the Android Studio UI is checked separately by the user.
- Device: Android Emulator AVD "Pixel_6", sdk_gphone64_x86_64, Android 14 (API 34).
- Method: the app was driven with `adb shell input` and `uiautomator dump`. Every "Actual result" comes from the screen text or `run-as … cat files/notes.json`. Screenshots are real `adb screencap` captures in `docs/evidence/screenshots/`.

| # | Test | Expected result | Actual result | Status | Evidence |
|---|------|-----------------|---------------|--------|----------|
| 1 | Gradle build | `assembleDebug` succeeds | BUILD SUCCESSFUL in 38s, 1 compiler warning in test code (parameter name in InMemoryMockNoteDao) | PASS | build output in docs/WORK_LOG.md |
| 2 | Unit tests | All tests pass | `NotesStorageUnitTest`: 4 tests, 0 failures, 0 errors. Note: the Room and JSON tests run against mock classes, not the real Room database or the real `JsonNotesStorage` (review finding 5) | PASS | app/build/test-results |
| 3 | App launch | Main screen without crash | "Notes" title, SQLite/JSON switch, "Sync SQLite → JSON" button, FAB; crash buffer empty | PASS | emulator_launch_after_session1_fix.png |
| 4 | Default storage | SQLite selected on start | "SQLite (Primary)" selected after every launch | PASS | 48_app_after_restart_sqlite.png |
| 5 | Validation: title and content empty | Saving blocked, error messages shown | "Title cannot be empty", "Content cannot be empty"; stayed on "New Note" | PASS | 30_app_validation_empty_fields.png |
| 6 | Validation: title of spaces only | Saving blocked (trim) | "Title cannot be empty" with title "   " and content "Some content" | PASS | 31_app_validation_blank_title.png |
| 7 | Validation: content of spaces only | Saving blocked (trim) | "Content cannot be empty" with title "Test Note" and content "  " | PASS | 32_app_validation_blank_content.png |
| 8 | Test 1, Create (SQLite) | Note "Test Note" / "This is a test note." saved and listed | Note appears at the top of the SQLite list | PASS | 33_app_create_note_form.png, 34_app_create_note_saved_sqlite.png |
| 9 | Test 2, Read | Note listed; opening it shows title and content | Detail screen "Note Details": title, Created/Updated time, content | PASS | 35_app_view_note_detail.png |
| 10 | Test 3, Update | Title and content changed and saved | Detail screen immediately shows "Test Note Edited" / "This is a test note. The content was changed.", Updated time changed; list updated | PASS | 36_app_edit_note_form.png, 37_app_edit_note_saved_detail.png, 38_app_list_after_edit.png |
| 11 | Test 4, Delete (SQLite) | Confirmation dialog; note disappears | Dialog "Delete Note?" with Cancel/Delete; after Delete, "Meeting Notes" gone, snackbar "Note deleted successfully" | PASS | 41_app_delete_confirmation.png, 42_app_delete_done_sqlite.png |
| 12 | Test 5, SQLite CRUD | Create, read, update and delete work with SQLite selected | Tests 8–11 were all run with SQLite selected | PASS | 33–42 |
| 13 | Test 6, JSON CRUD | Create, read, update and delete work with JSON File selected, and `notes.json` changes accordingly | Created "JSON Note" (file: 2 notes), edited to "JSON Note Edited" (file updated), deleted (file: 1 note) | PASS | 44_app_json_create.png, 45_app_json_edit.png, 47_app_json_delete.png |
| 14 | Test 7, Synchronization | SQLite notes copied to JSON with the same UUIDs; result shown | Snackbar "Synced successfully: 2 notes exported to JSON"; `notes.json` held "Test Note Edited" [b1f46060…] and "Meeting Notes" [8cc4c9c6…], same ids as SQLite; JSON tab shows both | PASS | 39_app_sync_success.png, 40_app_json_tab_after_sync.png |
| 15 | Repeated sync | No duplicates | Second sync: `notes.json` still 2 notes | PASS | docs/WORK_LOG.md |
| 16 | Deletion in SQLite, then sync | Deleted note removed from JSON | Before sync: 2 notes in JSON; after sync: 1 ("Test Note Edited") | PASS | 43_app_sync_removes_deleted.png |
| 17 | Storage isolation (review fix 2) | JSON-only note never shown in the SQLite list, also after repeated switching | 5 SQLite↔JSON toggles: JSON list = 2 notes, SQLite list = 1 note every time | PASS | 46_app_storage_isolation_sqlite.png |
| 18 | Persistence after restart | Notes remain after the app process is restarted | `am force-stop` + relaunch: SQLite and JSON lists unchanged | PASS | 48_app_after_restart_sqlite.png, 48b_app_after_restart_json.png |
| 19 | English text | All UI text in English by default | List, edit, detail, dialog and snackbar texts in English | PASS | 34–47 |
| 20 | Russian localization | All UI text in Russian | App locale ru-RU (`cmd locale set-app-locales`): "Заметки", "SQLite (Основное)", "JSON Файл (Вторичное)", "Синхронизация SQLite → JSON", validation errors, delete dialog and content descriptions in Russian; note texts stay as typed | PASS | 49_app_russian_list.png, 50_app_russian_validation.png, 51_app_russian_delete_dialog.png |
| 21 | Review fix 1 on Android 7.0/7.1 (API 24/25) | No crash when writing `notes.json` | Not run: no API 24/25 system image is installed (available: API 30, 33, 34, 37.2). Writing `notes.json` works on API 34 (tests 13–16) | NOT RUN | – |

Not covered here: the build and run inside the Android Studio UI (to be done and screenshotted by the user), and instrumented UI tests (the project has none).
