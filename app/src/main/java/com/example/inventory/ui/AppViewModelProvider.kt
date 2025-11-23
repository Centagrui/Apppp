package com.example.inventory.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.item.NoteEntryViewModel
import com.example.inventory.ui.item.NoteEditViewModel
import com.example.inventory.ui.media.MediaViewModel
import com.example.inventory.ui.notes.NoteDetailsViewModel
import com.example.inventory.ui.notes.NotesViewModel
import com.example.inventory.ui.reminders.ReminderViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {

        // Crear notas
        initializer {
            NoteEntryViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }

        // Editar notas
        initializer {
            NoteEditViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }

        // Lista de notas
        initializer {
            NotesViewModel(
                notesRepository = inventoryApplication().container.notesRepository
            )
        }

        // Detalles de nota
        initializer {
            NoteDetailsViewModel(
                notesRepository = inventoryApplication().container.notesRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }

        // Multimedia (NO requiere noteId en el constructor)
        initializer {
            MediaViewModel(
                mediaRepository = inventoryApplication().container.mediaRepository
            )
        }

        // Recordatorios
        initializer {
            ReminderViewModel(
                repository = inventoryApplication().container.reminderRepository
            )
        }
    }
}

fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
