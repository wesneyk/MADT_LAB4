package com.example.notesstorage.repository

import com.example.notesstorage.database.NoteDao
import com.example.notesstorage.database.NoteEntity
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface NotesRepository {
    fun getNotesFlow(storageType: StorageType): Flow<List<Note>>
    suspend fun getNotes(storageType: StorageType): List<Note>
    suspend fun getNoteById(id: String, storageType: StorageType): Note?
    suspend fun insertNote(note: Note, storageType: StorageType): Boolean
    suspend fun updateNote(note: Note, storageType: StorageType): Boolean
    suspend fun deleteNote(id: String, storageType: StorageType): Boolean
    suspend fun syncSqliteToJson(): Result<Int>
}

class NotesRepositoryImpl(
    private val noteDao: NoteDao,
    private val jsonStorage: JsonNotesStorage
) : NotesRepository {

    private val jsonNotesStateFlow = MutableStateFlow<List<Note>>(emptyList())

    suspend fun initialize() {
        refreshJsonCache()
    }

    private suspend fun refreshJsonCache() {
        val notes = jsonStorage.readNotes()
        jsonNotesStateFlow.value = notes
    }

    override fun getNotesFlow(storageType: StorageType): Flow<List<Note>> {
        return when (storageType) {
            StorageType.SQLITE -> {
                noteDao.getAllNotesFlow().map { entities ->
                    entities.map { it.toDomain() }
                }
            }
            StorageType.JSON_FILE -> {
                jsonNotesStateFlow.asStateFlow()
            }
        }
    }

    override suspend fun getNotes(storageType: StorageType): List<Note> = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> noteDao.getAllNotes().map { it.toDomain() }
            StorageType.JSON_FILE -> {
                refreshJsonCache()
                jsonNotesStateFlow.value
            }
        }
    }

    override suspend fun getNoteById(id: String, storageType: StorageType): Note? = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> noteDao.getNoteById(id)?.toDomain()
            StorageType.JSON_FILE -> jsonStorage.getNoteById(id)
        }
    }

    override suspend fun insertNote(note: Note, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.insertNote(NoteEntity.fromDomain(note)) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.insertNote(note)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun updateNote(note: Note, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.updateNote(NoteEntity.fromDomain(note)) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.updateNote(note)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun deleteNote(id: String, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.deleteById(id) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.deleteNote(id)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun syncSqliteToJson(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val sqliteNotes = noteDao.getAllNotes().map { it.toDomain() }
            val ok = jsonStorage.writeNotes(sqliteNotes)
            if (ok) {
                refreshJsonCache()
                Result.success(sqliteNotes.size)
            } else {
                Result.failure(Exception("Failed to safely write notes into JSON file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
