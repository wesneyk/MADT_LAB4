# Session 2: Independent Code Review (Google AI Studio), prompt

Used because all Antigravity models reached their usage limits before the review started (see docs/WORK_LOG.md).
The request text is taken word for word from the user's work instructions (section 4). The "Context" block was added by the Claude Code agent, because AI Studio cannot open or edit the local project.
Attach `docs/evidence/ai_studio/session2_review_input_sources.txt` to a **new** AI Studio chat and send the text below.

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
- The attached file session2_review_input_sources.txt contains the complete source code of the project. Each file starts with a line `===== FILE: <path relative to android-app> =====`.
- Assignment requirements: CRUD for notes (title and content); SQLite as the primary storage and a second storage (here a JSON file in internal storage); the user chooses the storage; synchronization from the primary to the secondary storage; validation that prevents empty notes; all displayed text localizable (English and Russian resources).
- Build setup: Gradle 9.6.0, AGP 9.4.1 with built-in Kotlin, KSP, compileSdk 37, minSdk 24, JDK from Android Studio.
- You cannot edit files or run the build in this chat, so:
  1. First present the review: all findings ranked by severity and practical impact, then the prioritized list of recommended changes.
  2. Then implement exactly the two highest-priority improvements by returning the complete new content of every file you change or add. Use exactly this format for each file: a line `===== FILE: <path relative to android-app> =====`, followed by one code block containing the whole file. No partial snippets, no "..." placeholders, no files that you did not change.
  3. Explain what was changed and why, and list the checks I should run to confirm the project still builds and works.
  4. Finally, give the full review report (findings, priorities, the two changes with reasons, verification plan) as one Markdown block for the file docs/CODE_REVIEW.md.
- I will place your files into the project unchanged, build it, run the unit tests and the app on the Pixel_6 emulator, and send you the results so you can confirm or correct the changes.
