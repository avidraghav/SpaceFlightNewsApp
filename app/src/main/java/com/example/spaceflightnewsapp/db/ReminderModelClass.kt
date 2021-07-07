package com.example.spaceflightnewsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "reminders")
data class ReminderModelClass(
    @PrimaryKey
    val id: String,
    val name: String,
    val dateTime: String,
    val pendingIntentId : Int
): Serializable
