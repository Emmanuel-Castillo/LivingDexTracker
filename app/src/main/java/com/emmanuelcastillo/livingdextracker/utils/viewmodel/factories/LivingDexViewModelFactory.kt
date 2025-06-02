package com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmanuelcastillo.livingdextracker.utils.PreferencesManager
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.LivingDexViewModel

class LivingDexViewModelFactory (private val application: Application, val gameId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating ViewModel with application: $application")
        if (modelClass.isAssignableFrom(LivingDexViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val preferencesManager = PreferencesManager(application.applicationContext)
            return LivingDexViewModel(application, preferencesManager, gameId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}