package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class Media(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val noteId: Int,         // ID de la nota a la que pertenece
    val uri: String,         // Ruta del archivo generado (foto/audio/video)
    val type: String         // "image", "audio", "video"
)
