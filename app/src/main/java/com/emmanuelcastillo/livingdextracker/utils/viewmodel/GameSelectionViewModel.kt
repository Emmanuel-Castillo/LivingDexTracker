package com.emmanuelcastillo.livingdextracker.utils.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emmanuelcastillo.livingdextracker.utils.database.LivingDexTrackerDatabase
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.database.daos.NumCaughtAndTotal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameSelectionViewModel (application: Application) : AndroidViewModel(application) {

    private val _allGames = MutableStateFlow<List<PokemonGame>>(emptyList())
    val allGames: StateFlow<List<PokemonGame>> = _allGames

    private val _filteredGames = MutableStateFlow<List<PokemonGame>>(emptyList())
    val filteredGames: StateFlow<List<PokemonGame>> = _filteredGames

    private val _numCaughtMap = MutableStateFlow<Map<Int, NumCaughtAndTotal>>(emptyMap())
    val numCaughtMap : StateFlow<Map<Int, NumCaughtAndTotal>> = _numCaughtMap

    // Grab all games from db
    init {
        viewModelScope.launch {
            val context = application.applicationContext
            val db = LivingDexTrackerDatabase.getDatabase(context)
            val games = db.pokemonGameDao().getAllPokemonGames()
            _allGames.value = games
            _filteredGames.value = games

            // Return a map with the key-value pertaining to game.gameId and NumCaughtAndTotal
            val map = games.associate { game ->
                game.gameId to db.userPokemonDao().getNumCaughtAndTotalPokemonFromGame(game.gameId)
            }
            _numCaughtMap.value = map
        }
    }

    fun filterByGeneration(gen: Int?) {
        _filteredGames.value = gen?.let {
            _allGames.value.filter { it.generation == gen }
        } ?: _allGames.value
    }

    fun getNumCaught(gameId: Int): NumCaughtAndTotal? {
        return _numCaughtMap.value[gameId]
    }
}