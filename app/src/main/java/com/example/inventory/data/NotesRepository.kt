package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getAllNotesStream(): Flow<List<Note>>

    fun getNoteStream(id: Int): Flow<Note?>

    suspend fun insertNoteReturnId(note: Note): Int

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)
}
