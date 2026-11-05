package com.example.notesstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesstorage.R
import com.example.notesstorage.database.AppDatabase
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.repository.NotesRepository
import com.example.notesstorage.repository.NotesRepositoryImpl
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUiState(
    val currentStorage: StorageType = StorageType.SQLITE,
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val selectedNote: Note? = null
)

sealed class UiEvent {
    data class ShowSnackbar(val messageResId: Int, val formatArgs: List<Any> = emptyList()) : UiEvent()
    object NavigateBack : UiEvent()
}

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository = NotesRepositoryImpl(
        AppDatabase.getInstance(application).noteDao(),
        JsonNotesStorage(application)
    )

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private var notesObservationJob: Job? = null

    init {
        viewModelScope.launch {
            (repository as? NotesRepositoryImpl)?.initialize()
            loadNotesForCurrentStorage()
        }
    }

    fun switchStorage(storageType: StorageType) {
        if (_uiState.value.currentStorage == storageType) return
        _uiState.update { it.copy(currentStorage = storageType, selectedNote = null) }
        loadNotesForCurrentStorage()
    }

    private fun loadNotesForCurrentStorage() {
        val currentStorage = _uiState.value.currentStorage
        notesObservationJob?.cancel()
        notesObservationJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getNotesFlow(currentStorage).collect { noteList ->
                    _uiState.update { it.copy(notes = noteList, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_operation_failed,
                        listOf(e.localizedMessage ?: "")
                    )
                )
            }
        }
    }

    fun loadNoteDetails(id: String) {
        viewModelScope.launch {
            val note = repository.getNoteById(id, _uiState.value.currentStorage)
            _uiState.update { it.copy(selectedNote = note) }
        }
    }

    fun saveNote(
        id: String?,
        rawTitle: String,
        rawContent: String,
        onSuccess: () -> Unit,
        onError: (titleError: Boolean, contentError: Boolean) -> Unit
    ) {
        val trimmedTitle = rawTitle.trim()
        val trimmedContent = rawContent.trim()

        val isTitleEmpty = trimmedTitle.isEmpty()
        val isContentEmpty = trimmedContent.isEmpty()

        if (isTitleEmpty || isContentEmpty) {
            onError(isTitleEmpty, isContentEmpty)
            return
        }

        viewModelScope.launch {
            val currentStorage = _uiState.value.currentStorage
            val now = System.currentTimeMillis()
            val noteToSave: Note
            val success = if (id == null) {
                noteToSave = Note(
                    title = trimmedTitle,
                    content = trimmedContent,
                    createdAt = now,
                    updatedAt = now
                )
                repository.insertNote(noteToSave, currentStorage)
            } else {
                val existing = repository.getNoteById(id, currentStorage)
                noteToSave = Note(
                    id = id,
                    title = trimmedTitle,
                    content = trimmedContent,
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now
                )
                repository.updateNote(noteToSave, currentStorage)
            }

            if (success) {
                _uiState.update { it.copy(selectedNote = noteToSave) }
                val msgResId = if (id == null) R.string.msg_note_created else R.string.msg_note_updated
                _eventFlow.emit(UiEvent.ShowSnackbar(msgResId))
                onSuccess()
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_save_failed))
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            val currentStorage = _uiState.value.currentStorage
            val success = repository.deleteNote(id, currentStorage)
            if (success) {
                _uiState.update { state ->
                    if (state.selectedNote?.id == id) state.copy(selectedNote = null) else state
                }
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_note_deleted))
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_delete_failed))
            }
        }
    }

    fun syncSqliteToJson() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            val result = repository.syncSqliteToJson()
            _uiState.update { it.copy(isSyncing = false) }

            result.onSuccess { count ->
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_sync_success,
                        listOf(count)
                    )
                )
            }.onFailure { err ->
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_sync_failure,
                        listOf(err.localizedMessage ?: "")
                    )
                )
            }
        }
    }
}
