package com.raghav.spacedawn.repository

import com.raghav.spacedawn.db.ReminderDatabase
import com.raghav.spacedawn.db.ReminderModelClass
import com.raghav.spacedawn.network.RetrofitInstance

class AppRepository(
    val db: ReminderDatabase

) {
    suspend fun getArticles(skipArticles: Int) = RetrofitInstance.api_spaceflight.getArticles(skipArticles)
    suspend fun searchArticle(searchQuery: String, skipArticles: Int) = RetrofitInstance.api_spaceflight.searchArticles(searchQuery, skipArticles)
    suspend fun getLaunches(skipLaunches: Int) = RetrofitInstance.api_launchlibrary.getLaunches(skipLaunches)

    suspend fun insert(reminder: ReminderModelClass) = db.getRemindersDao().saveReminder(reminder)
    fun getAllReminders() = db.getRemindersDao().getAllReminders()
    fun getId(id: String) = db.getRemindersDao().exists(id)
    suspend fun deleteReminder(reminder: ReminderModelClass) = db.getRemindersDao().deleteReminder(reminder)
}
