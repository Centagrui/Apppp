package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noteId: Int,
    val timeMillis: Long
)
