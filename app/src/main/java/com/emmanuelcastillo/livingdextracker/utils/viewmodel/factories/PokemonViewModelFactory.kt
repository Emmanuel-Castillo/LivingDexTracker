package com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonViewModel

class PokemonViewModelFactory(private val application: Application, val gameId: Int, val gameEntryId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating ViewModel with application: $application")
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(application, gameId, gameEntryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
