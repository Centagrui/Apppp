package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getMediaForNote(noteId: Int): Flow<List<Media>>

    suspend fun insertMedia(media: Media)

    suspend fun deleteMedia(media: Media)

    suspend fun deleteMediaByNoteId(noteId: Int)
}
