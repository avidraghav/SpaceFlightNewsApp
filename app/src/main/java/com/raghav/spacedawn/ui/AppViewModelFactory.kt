package com.raghav.spacedawn.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raghav.spacedawn.repository.AppRepository

class AppViewModelFactory(
    private val app: Application,
    private val repository: AppRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
