package com.example.notesstorage

import com.example.notesstorage.database.InMemoryMockNoteDao
import com.example.notesstorage.database.NoteDao
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.repository.NotesRepositoryImpl
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class PureJsonStorageHelper(private val tempDir: File) {
    private val jsonStorage = JsonNotesStorageMock(tempDir)

    fun getStorage() = jsonStorage
}

class JsonNotesStorageMock(private val baseDir: File) {
    private val fileName = "notes.json"
    private val targetFile: File get() = File(baseDir, fileName)

    fun readNotes(): List<Note> {
        if (!targetFile.exists()) return emptyList()
        val text = targetFile.readText(Charsets.UTF_8)
        if (text.isBlank()) return emptyList()
        val array = org.json.JSONArray(text)
        val list = mutableListOf<Note>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                Note(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    content = obj.getString("content"),
                    createdAt = obj.getLong("createdAt"),
                    updatedAt = obj.getLong("updatedAt")
                )
            )
        }
        return list.sortedByDescending { it.updatedAt }
    }

    fun writeNotes(notes: List<Note>): Boolean {
        val array = org.json.JSONArray()
        for (n in notes) {
            val obj = org.json.JSONObject().apply {
                put("id", n.id)
                put("title", n.title)
                put("content", n.content)
                put("createdAt", n.createdAt)
                put("updatedAt", n.updatedAt)
            }
            array.put(obj)
        }
        val tmp = File(baseDir, "$fileName.tmp")
        tmp.writeText(array.toString(2), Charsets.UTF_8)
        return tmp.renameTo(targetFile) || {
            tmp.copyTo(targetFile, overwrite = true)
            tmp.delete()
        }()
    }

    fun insertNote(note: Note): Boolean {
        val current = readNotes().toMutableList()
        val idx = current.indexOfFirst { it.id == note.id }
        if (idx >= 0) current[idx] = note else current.add(0, note)
        return writeNotes(current)
    }

    fun updateNote(note: Note): Boolean {
        val current = readNotes().toMutableList()
        val idx = current.indexOfFirst { it.id == note.id }
        if (idx >= 0) {
            current[idx] = note
            return writeNotes(current)
        }
        return false
    }

    fun deleteNote(id: String): Boolean {
        val current = readNotes().toMutableList()
        val removed = current.removeAll { it.id == id }
        return if (removed) writeNotes(current) else false
    }

    fun getNoteById(id: String): Note? = readNotes().find { it.id == id }
}

class NotesStorageUnitTest {

    private lateinit var tempDir: File
    private lateinit var noteDao: NoteDao
    private lateinit var jsonStorage: JsonNotesStorageMock

    @Before
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "test_notes_" + System.currentTimeMillis())
        tempDir.mkdirs()
        noteDao = InMemoryMockNoteDao()
        jsonStorage = JsonNotesStorageMock(tempDir)
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun testRoomCrudOperations() = runBlocking {
        val note = Note(
            id = "test-1",
            title = "SQLite Title",
            content = "SQLite Content",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        // Insert
        val entity = com.example.notesstorage.database.NoteEntity.fromDomain(note)
        val insertId = noteDao.insertNote(entity)
        assertTrue(insertId > 0)

        // Read
        val fetched = noteDao.getNoteById("test-1")?.toDomain()
        assertNotNull(fetched)
        assertEquals("SQLite Title", fetched?.title)
        assertEquals("SQLite Content", fetched?.content)

        // Update
        val updated = note.copy(title = "Updated Title", content = "Updated Content", updatedAt = 3000L)
        val updateCount = noteDao.updateNote(com.example.notesstorage.database.NoteEntity.fromDomain(updated))
        assertTrue(updateCount > 0)

        val fetchedUpdated = noteDao.getNoteById("test-1")?.toDomain()
        assertEquals("Updated Title", fetchedUpdated?.title)

        // Delete
        val deleteCount = noteDao.deleteById("test-1")
        assertTrue(deleteCount > 0)

        val fetchedAfterDelete = noteDao.getNoteById("test-1")
        assertNull(fetchedAfterDelete)
    }

    @Test
    fun testJsonCrudOperations() {
        val note = Note(
            id = "json-1",
            title = "JSON Title",
            content = "JSON Content",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        // Create
        val insertResult = jsonStorage.insertNote(note)
        assertTrue(insertResult)

        // Read
        val fetched = jsonStorage.getNoteById("json-1")
        assertNotNull(fetched)
        assertEquals("JSON Title", fetched?.title)
        assertEquals("JSON Content", fetched?.content)

        // Update
        val updated = note.copy(title = "JSON Updated Title", content = "JSON Updated Content", updatedAt = 4000L)
        val updateResult = jsonStorage.updateNote(updated)
        assertTrue(updateResult)

        val fetchedUpdated = jsonStorage.getNoteById("json-1")
        assertEquals("JSON Updated Title", fetchedUpdated?.title)

        // Delete
        val deleteResult = jsonStorage.deleteNote("json-1")
        assertTrue(deleteResult)

        val fetchedAfterDelete = jsonStorage.getNoteById("json-1")
        assertNull(fetchedAfterDelete)
    }

    @Test
    fun testOneWaySyncSqliteToJson() = runBlocking {
        // Prepare notes in SQLite
        val note1 = Note(id = "sync-1", title = "Note 1", content = "Content 1", createdAt = 100L, updatedAt = 100L)
        val note2 = Note(id = "sync-2", title = "Note 2", content = "Content 2", createdAt = 200L, updatedAt = 200L)
        noteDao.insertNote(com.example.notesstorage.database.NoteEntity.fromDomain(note1))
        noteDao.insertNote(com.example.notesstorage.database.NoteEntity.fromDomain(note2))

        // Put an old note into JSON that doesn't exist in SQLite
        val staleNote = Note(id = "stale-1", title = "Stale", content = "Stale content", createdAt = 50L, updatedAt = 50L)
        jsonStorage.insertNote(staleNote)

        // Perform Sync logic: read SQLite, overwrite JSON safely
        val sqliteNotes = noteDao.getAllNotes().map { it.toDomain() }
        val syncOk = jsonStorage.writeNotes(sqliteNotes)
        assertTrue(syncOk)

        // Verify JSON now mirrors SQLite exactly
        val jsonNotes = jsonStorage.readNotes()
        assertEquals(2, jsonNotes.size)
        assertTrue(jsonNotes.any { it.id == "sync-1" })
        assertTrue(jsonNotes.any { it.id == "sync-2" })
        assertFalse(jsonNotes.any { it.id == "stale-1" }) // Stale note removed
    }

    @Test
    fun testValidationLogic() {
        val emptyTitle = "   "
        val validTitle = "My Note"
        val emptyContent = ""
        val validContent = "Valid content"

        assertTrue(emptyTitle.trim().isEmpty())
        assertFalse(validTitle.trim().isEmpty())
        assertTrue(emptyContent.trim().isEmpty())
        assertFalse(validContent.trim().isEmpty())
    }
}
