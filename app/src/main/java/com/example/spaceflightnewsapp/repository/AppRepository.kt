package com.example.spaceflightnewsapp.repository

import com.example.spaceflightnewsapp.db.ReminderDatabase
import com.example.spaceflightnewsapp.db.ReminderModelClass
import com.example.spaceflightnewsapp.network.LaunchLibrary
import com.example.spaceflightnewsapp.network.RetrofitInstance
import com.example.spaceflightnewsapp.network.SpaceFlightAPI

class AppRepository(
    val db: ReminderDatabase

) {
    suspend fun getArticles(skipArticles : Int) = RetrofitInstance.api_spaceflight.getArticles(skipArticles)
    suspend fun searchArticle(searchQuery : String,skipArticles: Int) = RetrofitInstance.api_spaceflight.searchArticles(searchQuery,skipArticles)
    suspend fun getLaunches(skipLaunches: Int) = RetrofitInstance.api_launchlibrary.getLaunches(skipLaunches)

    suspend fun insert(reminder: ReminderModelClass) = db.getRemindersDao().saveReminder(reminder)
    fun getAllReminders() = db.getRemindersDao().getAllReminders()
    fun getId(id: String) =db.getRemindersDao().exists(id)
    suspend fun deleteReminder(reminder: ReminderModelClass) = db.getRemindersDao().deleteReminder(reminder)
}