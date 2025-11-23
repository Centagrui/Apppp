package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE noteId = :noteId")
    fun getRemindersByNoteId(noteId: Int): Flow<List<Reminder>>

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteReminder(id: Int)
}
