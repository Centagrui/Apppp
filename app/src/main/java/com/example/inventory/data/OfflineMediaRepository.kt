package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class OfflineMediaRepository(
    private val mediaDao: MediaDao
) : MediaRepository {

    override fun getMediaForNote(noteId: Int): Flow<List<Media>> =
        mediaDao.getMediaForNote(noteId)

    override suspend fun insertMedia(media: Media) =
        mediaDao.insertMedia(media)

    override suspend fun deleteMedia(media: Media) =
        mediaDao.deleteMedia(media)

    override suspend fun deleteMediaByNoteId(noteId: Int) =
        mediaDao.deleteMediaByNoteId(noteId)
}
