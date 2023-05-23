package com.raghav.spacedawn.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(launch: ReminderModelClass)

    @Query("Select * FROM reminders")
    fun getAllReminders(): LiveData<List<ReminderModelClass>>

    @Query("SELECT EXISTS (SELECT id FROM reminders WHERE id = :ids)")
    fun exists(ids: String): Boolean

    @Delete
    suspend fun deleteReminder(launch: ReminderModelClass)
}
