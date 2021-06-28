package com.example.spaceflightnewsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spaceflightnewsapp.repository.AppRepository

class AppViewModelFactory(
    private val repository: AppRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}
