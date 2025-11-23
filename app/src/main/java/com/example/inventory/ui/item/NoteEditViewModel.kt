package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteEditViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState: StateFlow<NoteUiState> = _noteUiState

    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            notesRepository.getNoteStream(noteId).collect { note ->
                if (note != null) {
                    _noteUiState.value = NoteUiState(
                        noteDetails = note.toNoteDetails()
                    )
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails.copy(title = newTitle)
        )
    }

    fun updateContent(newContent: String) {
        _noteUiState.value = _noteUiState.value.copy(
            noteDetails = _noteUiState.value.noteDetails.copy(content = newContent)
        )
    }

    suspend fun saveNote() {
        val note = _noteUiState.value.noteDetails.toNote()
        notesRepository.updateNote(note)
    }

    fun saveNoteAndNavigate(navigateBack: () -> Unit) {
        viewModelScope.launch {
            saveNote()
            navigateBack()
        }
    }
}
