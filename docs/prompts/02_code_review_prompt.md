# Session 2: Independent Code Review (Antigravity), prompt

The request text is taken word for word from the user's work instructions (section 4). The "Context" block was added by the Claude Code agent: the project location, how to build, and where to save the report.
It is sent to a **new** Antigravity conversation inside the **lab_4** project (not WordCounter).

---

Perform a thorough independent code review of this Android Notes Storage application.

Review the complete project, including architecture, Kotlin code, Jetpack Compose UI, Room/SQLite implementation, JSON storage, CRUD operations, repository layer, synchronization, validation, localization, error handling, and tests.

Identify bugs, architectural weaknesses, potential data-loss scenarios, security issues, and violations of the assignment requirements.

Rank all findings according to severity and practical impact.

Provide a prioritized list of recommended changes.

After presenting the review, implement exactly the two highest-priority improvements directly in the project using AI-assisted code editing.

Do not manually edit source code through Android Studio.

After implementing the changes, verify that the project remains buildable and that existing functionality is preserved.

Clearly explain what was changed and why.

Context:
- The project is in C:\lab_4\android-app. Assignment requirements: CRUD for notes (title and content); SQLite as primary storage and a second storage (here a JSON file); the user chooses the storage; sync from the primary to the secondary storage; validation against empty notes; all displayed text localizable (English and Russian resources).
- Build with the Android Studio JDK: JAVA_HOME = C:\Program Files\Android\Android Studio\jbr, then `gradlew.bat assembleDebug testDebugUnitTest` in C:\lab_4\android-app. The Pixel_6 emulator (emulator-5554) is running for checks on the device.
- Save the full review (all findings ranked by severity, the prioritized list, the two implemented changes with reasons, and the verification results) to C:\lab_4\docs\CODE_REVIEW.md. Do not change other files in C:\lab_4\docs and do not touch C:\lab_4\.git.
