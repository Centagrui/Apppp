package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class OfflineReminderRepository(
    private val dao: ReminderDao
) : ReminderRepository {

    override suspend fun insertReminder(reminder: Reminder) {
        dao.insertReminder(reminder)
    }

    override fun getRemindersByNoteId(noteId: Int): Flow<List<Reminder>> {
        return dao.getRemindersByNoteId(noteId)
    }

    override suspend fun deleteReminder(id: Int) {
        dao.deleteReminder(id)
    }
}
