package com.example.spaceflightnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ReminderModelClass::class],
    version = 1
)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getRemindersDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: ReminderDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ReminderDatabase::class.java,
                "reminder_db.db"
            ).build()
    }
}