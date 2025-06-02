package com.emmanuelcastillo.livingdextracker.utils.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emmanuelcastillo.livingdextracker.utils.PreferencesManager
import com.emmanuelcastillo.livingdextracker.utils.database.LivingDexTrackerDatabase
import com.emmanuelcastillo.livingdextracker.utils.database.prepopulateDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application, private val preferencesManager: PreferencesManager) : AndroidViewModel(application) {

    private val _appReady = MutableStateFlow(false)
    val appReady: StateFlow<Boolean> = _appReady.asStateFlow()

    private val _prepopulationState = MutableStateFlow<PrepopulationState>(PrepopulationState.Idle)
    val prepopulationState: StateFlow<PrepopulationState> = _prepopulationState.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    // When MainViewModel is declared, check to see if isDbPrepopulated is set. If so, set appReady to true. Otherwise, prepopulate the db
    init {
        viewModelScope.launch {
            // Check if db needs to be prepopulated
            val isDbPrepopulated = preferencesManager.isDbPrepopulated()
            Log.d("MainViewModel", "Prefs path: ${context.filesDir.parent}/shared_prefs/app_prefs.xml")

            Log.d("MainViewModel", "isDbPrepopulated before: ${isDbPrepopulated}")

            if (!isDbPrepopulated) {
                prepopDB()
            } else {
                Log.d("MainViewModel", "Database is prepopulated already!")
                _appReady.value = true
            }

            val postCheck = preferencesManager.isDbPrepopulated()
            Log.d("MainViewModel", "isDbPrepopulated after: ${postCheck}")
        }
    }

    fun prepopDB() {
        // Coroutine scope associated w/ the ViewModel
        viewModelScope.launch {
            val db = LivingDexTrackerDatabase.getDatabase(context)
            // Launch a coroutine using Dispatchers.IO to initialize the database and populate if necessary
            withContext(Dispatchers.IO) {
                Log.d("MainViewModel", "Need to prepopulate the database!")
                prepopulateDatabase(db, _prepopulationState)
            }
            if (_prepopulationState.value is PrepopulationState.Done) {
                val success = preferencesManager.setDbPrepopulated(true)
                Log.d("MainViewModel", "Preference write success: $success")

                _appReady.value = true
            }
        }
    }
}

// State class to inform the user the information being saved to db
sealed class PrepopulationState {
    object Idle : PrepopulationState()
    data class Loading(val message: String) : PrepopulationState()
    data class PrepopulatingPokemon(val name: String, val current: Int, val total: Int) : PrepopulationState()
    data class Error(val message: String) : PrepopulationState()
    object Done : PrepopulationState()
}
