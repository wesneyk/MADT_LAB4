# Session 3: second follow-up to the README chat (format and one fact)

Prepared by the Claude Code agent. The corrected README could not be copied as one piece: the inner ``` fences (the json example and the diagram) closed the outer ```markdown block early, so half of the README was rendered instead of staying raw Markdown. One fact was also wrong (JDK 17).

---

Thanks, the content corrections are applied. Two more things:

1. The bundled JetBrains Runtime of this Android Studio is JDK 25.0.3, not JDK 17. JDK 17 is only the Java source/target compatibility in app/build.gradle.kts. Please write it like that.
2. The README could not be copied: the ``` fences inside it (the json example and the architecture diagram) closed your outer ```markdown block early, so half of it was rendered. Return the complete README.md again, wrapped in an outer fence of four tildes (~~~~markdown at the start and ~~~~ at the end), keeping the normal ``` fences inside. Put the architecture diagram inside a ```text block so its lines are kept. Do not change anything else.
