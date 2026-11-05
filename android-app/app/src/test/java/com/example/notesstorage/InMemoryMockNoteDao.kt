package com.example.notesstorage.database

import androidx.room.RoomDatabase
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.notesstorage.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryMockNoteDao : NoteDao {
    private val mutex = Mutex()
    private val notes = mutableMapOf<String, NoteEntity>()
    private val _flow = MutableStateFlow<List<NoteEntity>>(emptyList())

    private fun updateFlow() {
        _flow.value = notes.values.sortedByDescending { it.updatedAt }
    }

    override fun getAllNotesFlow(): Flow<List<NoteEntity>> = _flow.asStateFlow()

    override suspend fun getAllNotes(): List<NoteEntity> = mutex.withLock {
        notes.values.sortedByDescending { it.updatedAt }
    }

    override suspend fun getNoteById(id: String): NoteEntity? = mutex.withLock {
        notes[id]
    }

    override suspend fun insertNote(note: NoteEntity): Long = mutex.withLock {
        notes[note.id] = note
        updateFlow()
        1L
    }

    override suspend fun updateNote(note: NoteEntity): Int = mutex.withLock {
        if (notes.containsKey(note.id)) {
            notes[note.id] = note
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun deleteNote(note: NoteEntity): Int = mutex.withLock {
        val removed = notes.remove(note.id)
        if (removed != null) {
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun deleteById(id: String): Int = mutex.withLock {
        val removed = notes.remove(id)
        if (removed != null) {
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun insertAll(notesList: List<NoteEntity>) = mutex.withLock {
        notesList.forEach { notes[it.id] = it }
        updateFlow()
    }

    override suspend fun deleteAll() = mutex.withLock {
        notes.clear()
        updateFlow()
    }
}
