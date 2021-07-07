package com.example.spaceflightnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReminderDao {

@Insert
suspend fun saveReminder(launch : ReminderModelClass)

@Query("Select * FROM reminders ORDER by id DESC")
suspend fun getAllReminders() : List<ReminderModelClass>
@Delete
suspend fun deleteReminder(launch: ReminderModelClass)
}