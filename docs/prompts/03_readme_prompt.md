# Session 3: User Documentation / README.md, prompt

Prepared by the Claude Code agent from the user's work instructions (section 6) and the assignment ("create user documentation (README.md) and publish it as the GitHub repository description").
It is sent to a **new** AI chat that is used only for documentation.
- Google AI Studio: attach `docs/evidence/ai_studio/session3_readme_input.txt` (current source code, docs/CODE_REVIEW.md, docs/evidence/TEST_RESULTS.md and the repository tree).
- Antigravity (lab_4 project): no attachment is needed, it reads the files in C:\lab_4 directly.

---

Write professional user documentation (README.md) in English for this Android project, "Notes Storage". This session is only about documentation: do not change any source code.

Use only information that is confirmed by the project files: the source code, docs/CODE_REVIEW.md and docs/evidence/TEST_RESULTS.md. Do not claim features or tests that are not implemented or were not run. Where a test was not run, say so.

The README must have these sections:
1. Project Name
2. Project Description
3. Features
4. Technologies Used
5. Application Architecture
6. Data Storage
7. SQLite Database
8. JSON File Storage
9. CRUD Operations
10. Data Synchronization
11. Input Validation
12. Localization
13. Project Structure
14. Installation Instructions
15. How to Run the Application
16. How to Use the Application
17. Testing
18. Code Review Improvements
19. Requirements Compliance

It must explain: what the app is for; how to build and run it; how to create and edit notes; how to choose the storage; how the synchronization works; where the data is stored on the device; which technologies are used.

Facts about the repository:
- Repository root: C:\lab_4 (it will be published to https://github.com/wesneyk/MADT_LAB4 in a later session). The Android Studio project is in the folder android-app/. Documentation is in docs/ (CODE_REVIEW.md, evidence/TEST_RESULTS.md, evidence/screenshots/).
- Build requirements found in the project: Android Studio with its bundled JDK, Gradle 9.6.0 (wrapper), AGP 9.4.1, compileSdk 37, targetSdk 37, minSdk 24.
- Where useful, link to real screenshots with relative paths from the repository root, for example docs/evidence/screenshots/34_app_create_note_saved_sqlite.png, 39_app_sync_success.png, 30_app_validation_empty_fields.png, 49_app_russian_list.png. Use only file names that appear in the repository tree.

Output:
1. The complete README.md as one Markdown block.
2. A short, professional one-line description (at most 350 characters) for the GitHub repository "Description" field.
