package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media WHERE noteId = :noteId")
    fun getMediaForNote(noteId: Int): Flow<List<Media>>

    @Insert
    suspend fun insertMedia(media: Media)

    @Delete
    suspend fun deleteMedia(media: Media)

    @Query("DELETE FROM media WHERE noteId = :noteId")
    suspend fun deleteMediaByNoteId(noteId: Int)
}
