package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val notesRepository: NotesRepository
    val mediaRepository: MediaRepository
    val reminderRepository: ReminderRepository   // ← AGREGADO
}

/**
 * Implementation that provides instance of repositories
 */
class AppDataContainer(private val context: Context) : AppContainer {

    private val database: InventoryDatabase by lazy {
        InventoryDatabase.getDatabase(context)
    }

    // Repositorio de notas
    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(database.noteDao())
    }

    // Repositorio de multimedia
    override val mediaRepository: MediaRepository by lazy {
        OfflineMediaRepository(database.mediaDao())
    }

    // Repositorio de recordatorios  ← AGREGADO
    override val reminderRepository: ReminderRepository by lazy {
        OfflineReminderRepository(database.reminderDao())
    }
}
