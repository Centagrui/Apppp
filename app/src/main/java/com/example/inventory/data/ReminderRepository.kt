package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insertReminder(reminder: Reminder)
    fun getRemindersByNoteId(noteId: Int): Flow<List<Reminder>>
    suspend fun deleteReminder(id: Int)
}
