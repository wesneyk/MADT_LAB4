# Session 1: Code Generation (Antigravity), first prompt

Prepared by the Claude Code agent (preparatory session) from the assignment text and the user's instructions.
It is sent to a **new** Antigravity conversation as the very first message.

---

/grill-me

Before writing any code, run a grill-me planning interview (the equivalent of /plan) for the project below.
Study the requirements, build a technical plan, list the components and the risks, and ask me only the
questions that really need my decision (technology choices or important design decisions). Use sensible
defaults for everything else. Do not generate code until the planning is finished and I approve the plan.

Project: "Notes Storage", a native Android application.
Location: create the Android Studio project in C:\lab_4\android-app. The repository root is C:\lab_4;
do not touch C:\lab_4\docs, C:\lab_4\deliverables or C:\lab_4\.git.

Requirements:
1. CRUD for notes: create, view (open a note to read it), edit, delete. Delete asks for confirmation and the list refreshes afterwards.
2. User-visible note fields: title and content. Internally also a UUID id, createdAt and updatedAt (not shown in a way that clutters the UI).
3. Two independent storages: SQLite via Room (primary: Entity, DAO, Database, Repository) and a JSON file in internal storage, filesDir/notes.json (secondary).
4. The user picks the storage with a clear switch "SQLite (Primary)" / "JSON File (Secondary)"; default is SQLite. The list and all CRUD operations work with the selected storage, and switching reloads the list from that source.
5. A "Sync SQLite → JSON" button: one-way sync, SQLite is the source of truth. Keep UUIDs and timestamps, create no duplicates, remove from JSON the notes deleted in SQLite, and show the result to the user. Write the file safely (temporary file and atomic replace) so a failure during sync never loses the old data.
6. Validation: trim() title and content; if either is empty, saving is refused with a clear error message. It must work for both storages.
7. All user-visible text in string resources: res/values/strings.xml (English) and res/values-ru/strings.xml (Russian). This includes the app name, screen titles, buttons, field labels, storage names, error and success messages, the delete confirmation, sync messages, empty states and content descriptions. No hard-coded UI text in Kotlin or Compose; use stringResource().

Preferred stack: Kotlin, Jetpack Compose (Material 3), Room, MVVM, Repository pattern, Kotlin Coroutines (all database and file I/O off the main thread).
Package structure: data, database, storage, repository, model, viewmodel, ui. No business logic in MainActivity.
UI: main screen with the title "Notes", the SQLite/JSON switch, the sync button, the note list (card: title, short content preview, edit and delete actions) and a button to create a note; a note details view; an add/edit screen with Title, Content, Save and Cancel; clear feedback (for example snackbars) after operations. It must look correct on common phone screen sizes.
Error handling everywhere storage can fail. Unit tests, where possible, for Room CRUD, JSON CRUD, sync and validation.

Environment (do not upgrade tools unless required):
- Android Studio build 261.26222.65 (2026) with its bundled JDK at C:\Program Files\Android\Android Studio\jbr (the java on PATH is Java 8, do not use it for Gradle).
- Android SDK platforms 35, 36 and 37, build-tools 36.0.0; emulators Pixel_6 and Pixel_4.
- Earlier projects on this machine build successfully with Gradle 9.6.0, AGP 9.4.1 (built-in Kotlin), compileSdk 37, targetSdk 37, minSdk 24.

I do not edit code manually: every code change must be made by you. Building and running happen in Android Studio.
