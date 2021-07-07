package com.example.spaceflightnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDao {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun saveReminder(launch : ReminderModelClass)

@Query("Select * FROM reminders ORDER by id DESC")
fun getAllReminders() : List<ReminderModelClass>
@Query("SELECT EXISTS (SELECT id FROM reminders WHERE id = :ids)")
fun exists(ids: String) : Boolean
@Delete
suspend fun deleteReminder(launch: ReminderModelClass)
}