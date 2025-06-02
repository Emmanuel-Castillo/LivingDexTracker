package com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmanuelcastillo.livingdextracker.utils.PreferencesManager
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.MainViewModel

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating ViewModel with application: $application")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val preferencesManager = PreferencesManager(application.applicationContext)
            return MainViewModel(application, preferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}