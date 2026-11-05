package com.example.notesstorage.storage

import android.content.Context
import com.example.notesstorage.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class JsonNotesStorage(
    private val filesDir: File
) {
    constructor(context: Context) : this(context.filesDir)

    private val mutex = Mutex()
    private val fileName = "notes.json"
    private val targetFile: File
        get() = File(filesDir, fileName)

    suspend fun readNotes(): List<Note> = withContext(Dispatchers.IO) {
        mutex.withLock {
            readNotesInternal()
        }
    }

    suspend fun getNoteById(id: String): Note? = withContext(Dispatchers.IO) {
        readNotes().find { it.id == id }
    }

    suspend fun writeNotes(notes: List<Note>): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            writeNotesInternal(notes)
        }
    }

    suspend fun insertNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val existingIndex = currentNotes.indexOfFirst { it.id == note.id }
            if (existingIndex >= 0) {
                currentNotes[existingIndex] = note
            } else {
                currentNotes.add(0, note)
            }
            writeNotesInternal(currentNotes)
        }
    }

    suspend fun updateNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val index = currentNotes.indexOfFirst { it.id == note.id }
            if (index >= 0) {
                currentNotes[index] = note
                writeNotesInternal(currentNotes)
            } else {
                false
            }
        }
    }

    suspend fun deleteNote(id: String): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val removed = currentNotes.removeAll { it.id == id }
            if (removed) {
                writeNotesInternal(currentNotes)
            } else {
                false
            }
        }
    }

    suspend fun clearAll(): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            writeNotesInternal(emptyList())
        }
    }

    private fun readNotesInternal(): List<Note> {
        val file = targetFile
        val backupFile = File(filesDir, "$fileName.bak")

        // Auto-recover from backup file if a crash occurred during atomic rename
        if (!file.exists() && backupFile.exists()) {
            backupFile.renameTo(file)
        }

        if (!file.exists()) return emptyList()

        return try {
            val text = file.readText(Charsets.UTF_8)
            parseJson(text)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun writeNotesInternal(notes: List<Note>): Boolean {
        val jsonArray = JSONArray()
        for (note in notes) {
            val obj = JSONObject().apply {
                put("id", note.id)
                put("title", note.title)
                put("content", note.content)
                put("createdAt", note.createdAt)
                put("updatedAt", note.updatedAt)
            }
            jsonArray.put(obj)
        }
        return safeWrite(jsonArray.toString(2))
    }

    private fun parseJson(jsonString: String): List<Note> {
        val notes = mutableListOf<Note>()
        if (jsonString.isBlank()) return notes
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            notes.add(
                Note(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    content = obj.getString("content"),
                    createdAt = obj.getLong("createdAt"),
                    updatedAt = obj.getLong("updatedAt")
                )
            )
        }
        return notes.sortedByDescending { it.updatedAt }
    }

    private fun safeWrite(content: String): Boolean {
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }

        val tempFile = File(filesDir, "$fileName.tmp")
        val destination = targetFile
        val backupFile = File(filesDir, "$fileName.bak")

        return try {
            FileOutputStream(tempFile).use { fos ->
                fos.write(content.toByteArray(Charsets.UTF_8))
                fos.flush()
                fos.fd.sync()
            }

            if (destination.exists()) {
                if (backupFile.exists()) {
                    backupFile.delete()
                }
                if (!destination.renameTo(backupFile)) {
                    destination.delete()
                }
                if (tempFile.renameTo(destination)) {
                    backupFile.delete()
                    true
                } else {
                    if (backupFile.exists()) {
                        backupFile.renameTo(destination)
                    }
                    false
                }
            } else {
                tempFile.renameTo(destination)
            }
        } catch (t: Throwable) {
            if (tempFile.exists()) {
                tempFile.delete()
            }
            false
        }
    }
}
