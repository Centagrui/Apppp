package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Note
import com.example.inventory.data.NotesRepository

class NoteEntryViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    // ⬅️ NoteUiState SIEMPRE TIENE UN NoteDetails NO NULO
    var noteUiState by mutableStateOf(NoteUiState())
        private set

    // ------------------------------------
    // ACTUALIZACIÓN DE CAMPOS
    // ------------------------------------
    fun updateTitle(newValue: String) {
        noteUiState = noteUiState.copy(
            noteDetails = noteUiState.noteDetails.copy(title = newValue)
        )
    }

    fun updateContent(newValue: String) {
        noteUiState = noteUiState.copy(
            noteDetails = noteUiState.noteDetails.copy(content = newValue)
        )
    }

    fun updateMultimediaUris(uris: List<String>) {
        noteUiState = noteUiState.copy(
            noteDetails = noteUiState.noteDetails.copy(multimediaUris = uris)
        )
    }

    // ------------------------------------
    // VALIDACIÓN
    // ------------------------------------
    val isEntryValid: Boolean
        get() = noteUiState.noteDetails.title.isNotBlank()

    // ------------------------------------
    // CONVERSIÓN DE NOTEDETAILS → NOTE
    // ------------------------------------
    private fun NoteDetails.toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            multimediaUris = multimediaUris
        )
    }

    // ------------------------------------
    // GUARDAR NOTA Y RETORNAR ID
    // ------------------------------------
    suspend fun saveNoteAndReturnId(): Int {
        val note = noteUiState.noteDetails.toNote()
        return notesRepository.insertNoteReturnId(note)
    }
}
