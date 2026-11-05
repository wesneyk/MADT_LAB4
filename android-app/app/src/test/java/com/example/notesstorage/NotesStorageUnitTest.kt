package com.example.notesstorage

import com.example.notesstorage.database.InMemoryMockNoteDao
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.NoteValidator
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.repository.NotesRepositoryImpl
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * JSON storage, sync and validation are tested with the real classes
 * (JsonNotesStorage, NotesRepositoryImpl, NoteValidator) in a temporary folder.
 *
 * The SQLite tests use InMemoryMockNoteDao instead of the real Room engine:
 * Room needs an Android device or Robolectric, so it is not covered by these JVM tests.
 */
class NotesStorageUnitTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var noteDao: InMemoryMockNoteDao
    private lateinit var jsonStorage: JsonNotesStorage
    private lateinit var repository: NotesRepositoryImpl

    @Before
    fun setUp() {
        noteDao = InMemoryMockNoteDao()
        jsonStorage = JsonNotesStorage(tempFolder.root)
        repository = NotesRepositoryImpl(noteDao, jsonStorage)
    }

    private fun note(
        id: String,
        title: String = "Title $id",
        content: String = "Content $id",
        updated: Long = 2000L
    ) = Note(id = id, title = title, content = content, createdAt = 1000L, updatedAt = updated)

    private fun jsonFile(name: String = "notes.json") = File(tempFolder.root, name)

    // ---- JSON storage (real JsonNotesStorage) ----

    @Test
    fun jsonStorage_createReadUpdateDelete() = runBlocking {
        val original = note("json-1")
        assertTrue(jsonStorage.insertNote(original))
        assertEquals("Title json-1", jsonStorage.getNoteById("json-1")?.title)

        val updated = original.copy(title = "Changed", content = "Changed content", updatedAt = 3000L)
        assertTrue(jsonStorage.updateNote(updated))
        assertEquals("Changed", jsonStorage.getNoteById("json-1")?.title)

        assertTrue(jsonStorage.deleteNote("json-1"))
        assertNull(jsonStorage.getNoteById("json-1"))
    }

    @Test
    fun jsonStorage_updateAndDeleteOfMissingNoteReturnFalse() = runBlocking {
        assertFalse(jsonStorage.updateNote(note("missing")))
        assertFalse(jsonStorage.deleteNote("missing"))
    }

    @Test
    fun jsonStorage_keepsIdsAndTimestamps() = runBlocking {
        jsonStorage.writeNotes(
            listOf(Note(id = "keep", title = "T", content = "C", createdAt = 111L, updatedAt = 222L))
        )
        val read = jsonStorage.readNotes().single()
        assertEquals("keep", read.id)
        assertEquals(111L, read.createdAt)
        assertEquals(222L, read.updatedAt)
    }

    @Test
    fun jsonStorage_writeReplacesContentAndLeavesNoTempFiles() = runBlocking {
        jsonStorage.writeNotes(listOf(note("a"), note("b")))
        jsonStorage.writeNotes(listOf(note("b")))

        assertEquals(listOf("b"), jsonStorage.readNotes().map { it.id })
        assertTrue(jsonFile().exists())
        assertFalse(jsonFile("notes.json.tmp").exists())
        assertFalse(jsonFile("notes.json.bak").exists())
    }

    @Test
    fun jsonStorage_recoversFromBackupIfMainFileIsMissing() = runBlocking {
        jsonStorage.writeNotes(listOf(note("a")))
        // Simulate a crash in the middle of an atomic replace: only the backup is left.
        assertTrue(jsonFile().renameTo(jsonFile("notes.json.bak")))

        assertEquals(listOf("a"), jsonStorage.readNotes().map { it.id })
    }

    // ---- One-way sync SQLite -> JSON (real NotesRepositoryImpl) ----

    @Test
    fun sync_copiesSqliteNotesToJsonWithSameIdsAndReportsCount() = runBlocking {
        repository.insertNote(note("s1", updated = 100L), StorageType.SQLITE)
        repository.insertNote(note("s2", updated = 200L), StorageType.SQLITE)

        val result = repository.syncSqliteToJson()

        assertEquals(2, result.getOrThrow())
        val json = jsonStorage.readNotes()
        assertEquals(setOf("s1", "s2"), json.map { it.id }.toSet())
        assertEquals(200L, json.first { it.id == "s2" }.updatedAt)
    }

    @Test
    fun sync_removesJsonOnlyNotes() = runBlocking {
        jsonStorage.insertNote(note("stale"))
        repository.insertNote(note("s1"), StorageType.SQLITE)

        repository.syncSqliteToJson()

        assertEquals(listOf("s1"), jsonStorage.readNotes().map { it.id })
    }

    @Test
    fun sync_twiceCreatesNoDuplicates() = runBlocking {
        repository.insertNote(note("s1"), StorageType.SQLITE)

        repository.syncSqliteToJson()
        repository.syncSqliteToJson()

        assertEquals(1, jsonStorage.readNotes().size)
    }

    @Test
    fun sync_removesNotesDeletedInSqlite() = runBlocking {
        repository.insertNote(note("keep"), StorageType.SQLITE)
        repository.insertNote(note("gone"), StorageType.SQLITE)
        repository.syncSqliteToJson()

        repository.deleteNote("gone", StorageType.SQLITE)
        repository.syncSqliteToJson()

        assertEquals(listOf("keep"), jsonStorage.readNotes().map { it.id })
    }

    // ---- Storage separation and SQLite CRUD (DAO mock) ----

    @Test
    fun storages_areIndependent() = runBlocking {
        repository.insertNote(note("sql"), StorageType.SQLITE)
        repository.insertNote(note("json"), StorageType.JSON_FILE)

        assertEquals(listOf("sql"), repository.getNotes(StorageType.SQLITE).map { it.id })
        assertEquals(listOf("json"), repository.getNotes(StorageType.JSON_FILE).map { it.id })
    }

    @Test
    fun sqliteCrud_throughRepository_withDaoMock() = runBlocking {
        repository.insertNote(note("r1", title = "T1"), StorageType.SQLITE)
        assertEquals("T1", repository.getNoteById("r1", StorageType.SQLITE)?.title)

        assertTrue(repository.updateNote(note("r1", title = "T2"), StorageType.SQLITE))
        assertEquals("T2", repository.getNoteById("r1", StorageType.SQLITE)?.title)

        assertTrue(repository.deleteNote("r1", StorageType.SQLITE))
        assertNull(repository.getNoteById("r1", StorageType.SQLITE))
    }

    // ---- Validation (real NoteValidator) ----

    @Test
    fun validation_rejectsBlankTitle() {
        val result = NoteValidator.validate("   ", "Some content")

        assertTrue(result.isTitleEmpty)
        assertFalse(result.isContentEmpty)
        assertFalse(result.isValid)
    }

    @Test
    fun validation_rejectsBlankContent() {
        val result = NoteValidator.validate("Title", "  ")

        assertFalse(result.isTitleEmpty)
        assertTrue(result.isContentEmpty)
        assertFalse(result.isValid)
    }

    @Test
    fun validation_treatsTabsAndNewlinesAsBlank() {
        val result = NoteValidator.validate("\t\n", " \n ")

        assertTrue(result.isTitleEmpty)
        assertTrue(result.isContentEmpty)
        assertFalse(result.isValid)
    }

    @Test
    fun validation_acceptsNonEmptyValues() {
        val result = NoteValidator.validate("  My note ", "Text")

        assertTrue(result.isValid)
    }
}
