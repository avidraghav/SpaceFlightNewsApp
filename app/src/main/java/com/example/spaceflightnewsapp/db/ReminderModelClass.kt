package com.example.spaceflightnewsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "reminders")
data class ReminderModelClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val name: String,
    val date: String,
    val pendingIntentId : Long
): Serializable
