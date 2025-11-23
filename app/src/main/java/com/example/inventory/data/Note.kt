package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")   // ← IMPORTANTE: el nombre debe coincidir con NoteDao
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val fecha: Long = 0L,
    val hora: Long = 0L,
    val multimediaUris: List<String> = emptyList()
)
