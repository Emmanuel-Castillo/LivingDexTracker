package com.emmanuelcastillo.livingdextracker.utils.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emmanuelcastillo.livingdextracker.utils.PreferencesManager
import com.emmanuelcastillo.livingdextracker.utils.data_classes.game_dex.DexJSONData
import com.emmanuelcastillo.livingdextracker.utils.database.LivingDexTrackerDatabase
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGameEntry
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.UserPokemon
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonCaughtFromGame
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonInGame
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonWithVariants
import com.emmanuelcastillo.livingdextracker.utils.database.populateEncountersToDatabase
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.PokemonGameRepository
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.PokemonRepository
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.UserPokemonRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StaticFieldLeak")
class LivingDexViewModel(
    application: Application,
    private val preferencesManager: PreferencesManager,
    gameId: Int
) : AndroidViewModel(application) {


    private val _fetchingStatus = MutableStateFlow<DataStatus>(DataStatus.Idle)
    val fetchingStatus: StateFlow<DataStatus> = _fetchingStatus.asStateFlow()

    private val _fetchedDataChecklist = MutableStateFlow(
        hashMapOf(
            Pair("Game", false),
            Pair("Pokedex", false),
            Pair("UserPokemon", false)
        )
    )
    val fetchedDataChecklist: StateFlow<HashMap<String, Boolean>> =
        _fetchedDataChecklist.asStateFlow()

    private val _game = MutableStateFlow<PokemonGame?>(null)
    val game: StateFlow<PokemonGame?> = _game.asStateFlow()

    private val _pokedex = MutableStateFlow<Pokedex?>(null)
    val pokedex: StateFlow<Pokedex?> = _pokedex.asStateFlow()

    private val _capturedPokemonList = MutableStateFlow<List<PokemonCaughtFromGame>>(emptyList())
    val capturedPokemonList: StateFlow<List<PokemonCaughtFromGame>> =
        _capturedPokemonList.asStateFlow()

    private val _capturedPokemonSet = MutableStateFlow<MutableSet<Pair<Int?, Int>>>(mutableSetOf())
    val capturedPokemonSet: StateFlow<Set<Pair<Int?, Int>>> = _capturedPokemonSet.asStateFlow()

    private val _copyCapturedPokemonSet =
        MutableStateFlow<MutableSet<Pair<Int?, Int>>>(mutableSetOf())
    val copyCapturedPokemonSet: StateFlow<Set<Pair<Int?, Int>>> =
        _copyCapturedPokemonSet.asStateFlow()

    private val _screenReady = MutableStateFlow(false)
    val screenReady = _screenReady.asStateFlow()

    val context = application.applicationContext

    init {
        grabAllData(gameId)
    }

    fun updateChecklist(key: String) {
        _fetchedDataChecklist.value = _fetchedDataChecklist.value.toMutableMap().apply {
            this[key] = true
        } as HashMap<String, Boolean>
    }

    fun grabAllData(gameId: Int) {
        _fetchedDataChecklist.value =
            hashMapOf(
                Pair("Game", false),
                Pair("Pokedex", false),
                Pair("UserPokemon", false)
            )

        Log.d("LivingDexVM", "grabAllData($gameId)")
        viewModelScope.launch {
            _fetchingStatus.value = DataStatus.Loading(fetchedDataChecklist)
            getPokemonGame(gameId)
            if (_fetchingStatus.value is DataStatus.Error) {
                return@launch
            }
            delay(250)
            updateChecklist("Game")


            getPokedex()
            if (_fetchingStatus.value is DataStatus.Error) {
                return@launch
            }
            delay(250)
            updateChecklist("Pokedex")

            // Check if game encounters grabbed
            val dexId = _game.value!!.pokedexId
            Log.d("LivingDexVM", "calling isGameEncounterPrepopulated($dexId)")
            val isEncountersPrepopulated = preferencesManager.isGameEncountersPrepopulated(dexId)
            if (!isEncountersPrepopulated) {
                Log.d("LivingDexVM", "Need to populate encounters to database for pokedex #$dexId")
                val encountersPopulated =
                    populateEncountersToDatabase(context, dexId, _fetchingStatus)
                if (_fetchingStatus.value !is DataStatus.Error) {
                    // Set encounter flags for games w/ newly inserted encounters
                    if (encountersPopulated) {
                        preferencesManager.setGameEncountersPrepopulated(dexId, true)
                    }
                } else {
                    return@launch
                }
            }

            getCaughtPokemon(gameId)
            if (_fetchingStatus.value is DataStatus.Error) {
                return@launch
            }
            delay(250)
            updateChecklist("UserPokemon")

//            Log.d("LivingDexVM", "fetchingDataStatus: ${fetchingDataStatus.value}")
            if (_fetchedDataChecklist.value.all { it.value } and (_fetchingStatus.value !is DataStatus.Error)) {
                delay(250)
                _fetchingStatus.value = DataStatus.Done
                _screenReady.value = true
            }
        }
    }


    suspend fun getPokemonGame(id: Int) {
        try {
            Log.d("LivingDexVM", "getPokemonGame($id)")
            val db = LivingDexTrackerDatabase.getDatabase(context)
            val pokemonGameDao = db.pokemonGameDao()
            val pokemonGameRepository = PokemonGameRepository(pokemonGameDao)

            val fetchedGame = pokemonGameRepository.getGameById(id)

            _game.value = fetchedGame
            Log.d("LivingDexVM", "Game: ${_game.value}")
        } catch (e: Exception) {
            _fetchingStatus.value = DataStatus.Error(e.toString())
        }

    }

    suspend fun getPokedex() {
        Log.d("LivingDexVM", "getPokedex()")
        // viewModelScope is a built-in coroutine scope in Android that is tied to the lifecycle of a ViewModel
        try {
            val gameId = game.value!!.gameId
            val pokedexApiId = game.value!!.pokedexApiId
            val pokedexId = game.value!!.pokedexId

            val cachedPokedex = grabDexFromDB(pokedexId)

            // If ALL Pokemon from game are in the Pokemon entity, return the list
            if (cachedPokedex.pokemon.isNotEmpty()) {
                Log.d("LivingDexVM", "Returning cached list from Room")
                _pokedex.value = cachedPokedex
            } else {
                grabDexFromAPI(pokedexApiId, gameId, pokedexId)
            }
        } catch (e: Exception) {
            val errorMessage = "Error fetching Pokedex from Database: ${e.message}"
            println(errorMessage)
            viewModelScope.launch {
                delay(3000)
                _fetchingStatus.value = DataStatus.Error(errorMessage)
            }
        }
    }

    private suspend fun grabDexFromDB(pokedexId: Int): Pokedex {
        val db = LivingDexTrackerDatabase.getDatabase(context)
        val pokemonGameDao = db.pokemonGameDao()
        val pokemonDao = db.pokemonDao()
        val pokemonRepository = PokemonRepository(pokemonDao)

        var returningPokedex: Pokedex = Pokedex(
            pokemon = emptyList<PokemonMap>().toMutableList()
        )
        val gameEntryCount = pokemonGameDao.getCountOfGameEntry(pokedexId)
        if (gameEntryCount == 0) {
            return returningPokedex
        }

        // Query database to check for Pokemon from particular game
        val cachedList = pokemonRepository.getAllPokemonFromPokedex(pokedexId)
        Log.d("CachedList", cachedList.toString())

        val pokedexIdOffset = 1
        val pokedex = Pokedex(
            pokemon = MutableList(
                size = gameEntryCount,
            ) { _ ->
                PokemonMap(
                    name = "",
                    variants = mutableListOf()
                )
            }
        )
        if (cachedList.isNotEmpty()) {
            for (pokemon in cachedList) {
                pokedex.pokemon[pokemon.pokedexOrderNumber.minus(pokedexIdOffset)].name =
                    pokemon.name
                pokedex.pokemon[pokemon.pokedexOrderNumber.minus(pokedexIdOffset)].variants.add(
                    pokemon
                )
            }
            for (variantList in pokedex.pokemon) {
                variantList.variants.sortBy { it.priority }
            }
            returningPokedex = pokedex
        }
        return returningPokedex
    }

    private suspend fun grabDexFromAPI(pokedexApiId: Int, gameId: Int, pokedexId: Int) {
        try {
            val db = LivingDexTrackerDatabase.getDatabase(context)
            val pokemonGameDao = db.pokemonGameDao()
            val pokemonDao = db.pokemonDao()
            val pokemonRepository = PokemonRepository(pokemonDao)

            val wholePokedex: Pokedex = Pokedex(
                pokemon = emptyList<PokemonMap>().toMutableList()
            )
            val regionalVariantsAvailable = pokemonGameDao.getRegionalVariantsAvailable(gameId)
            Log.d(
                "API",
                "Regional Variants Available in ${gameId}: ${regionalVariantsAvailable}"
            )

            // Pokemon X and Y do NOT have whole pokedex saved in API (split to three)
            val numPokedexes = if (gameId in 22..23) 3 else 1

            // Calling pokedex APIs consecutively
            withContext(Dispatchers.IO) {
                for (i in 0..<numPokedexes) {
                    val currDexApiId = pokedexApiId + i
                    Log.d("API", "Fetching from JSON fuke using pokedexApiId = $currDexApiId")
                    val jsonString =
                        context.assets.open("dex/dex_${currDexApiId}.json").bufferedReader()
                            .use { it.readText() }

                    val gson = Gson()
                    val dexData: DexJSONData = gson.fromJson(
                        jsonString,
                        DexJSONData::class.java
                    )

                    val pokemonEntries = dexData.entries
                    val pokedexSize = pokemonEntries.size

                    val currPokedex = Pokedex(
                        pokemon = MutableList(
                            size = pokemonEntries.size,
                        ) { _ ->
                            PokemonMap(
                                name = "",
                                variants = mutableListOf()
                            )
                        }
                    )

                    var currentSizeIndex = 0

                    // Default for inserting pokemon to list starting at order number 1
                    // Will change if order number starts at 0 (Victini in BW)
                    var entryIndexOffset = 1

                    var partialPokedexIndexOffset = 0
                    if (currDexApiId == 13) {
                        partialPokedexIndexOffset = 150
                    }
                    if (currDexApiId == 14) {
                        partialPokedexIndexOffset = 303
                    }
                    Log.d(
                        "partialOffset",
                        "partialPokedexIndexOffset: $partialPokedexIndexOffset"
                    )

                    for (entry in pokemonEntries) {
                        val entryUrl = entry.pokemon_species.url
                        val entryNumber = entry.entry_number
                        if (entryNumber == 0) {
                            entryIndexOffset = 0
                        }

                        val entryId = entryUrl.split('/')[6].toInt()
                        Log.d("API", "Fetching Pokemon (natDexId = $entryId)")

                        val pokemonVariantEntryList =
                            emptyList<PokemonInGame>().toMutableList()

                        val pokemonWithVariants: List<PokemonWithVariants>

                        withContext(Dispatchers.IO) {
                            // Grab Pokemon from Room determine which variants belong in that game (create PokemonGameEntry rows)
                            // Grabs Pokemon from db
                            pokemonWithVariants =
                                pokemonRepository.getPokemonById(entryId)
                        }
                        Log.d("API", pokemonWithVariants.toString())

                        if (pokemonWithVariants.isNotEmpty()) {
                            val pokemonName = pokemonWithVariants[0].name

                            // Add new game entry for regional variants available in game
                            for (pokemon in pokemonWithVariants) {
                                if (regionalVariantsAvailable.any { it.rvId == pokemon.rvId }) {

                                    Log.d(
                                        "API",
                                        "${pokemon.name} w/ rvId: ${pokemon.rvId} in the game"
                                    )
                                    // Debug: check if game entry exist to add new one
                                    var variantInGame =
                                        pokemonDao.hasGameEntry(
                                            pokemon.variantId,
                                            pokedexId
                                        )
                                    if (variantInGame != null) {
                                        Log.d(
                                            "API",
                                            "${pokemon.name} already has game entry in gameId: ${gameId}!"
                                        )
                                        pokemonVariantEntryList.add(variantInGame)
                                        continue
                                    }

                                    Log.d(
                                        "API",
                                        "Creating game entry for ${pokemon.name} in gameId: ${gameId}"
                                    )
                                    val newGameEntry = PokemonGameEntry(
                                        pokedexId = pokedexId,
                                        variantId = pokemon.variantId,
                                        pokedexOrderNumber = (if (entryIndexOffset == 1) entryNumber else entryNumber.plus(
                                            1
                                        ).plus(partialPokedexIndexOffset))
                                    )
                                    pokemonDao.insertGameEntry(newGameEntry)
                                    variantInGame =
                                        pokemonDao.hasGameEntry(
                                            pokemon.variantId,
                                            pokedexId
                                        )

                                    // Then add to pokedex
                                    if (variantInGame != null) {
                                        pokemonVariantEntryList.add(variantInGame)
                                    }
                                }
                            }

                            Log.d("API", pokemonVariantEntryList.toString())
                            pokemonVariantEntryList.sortBy { it.priority }
                            currPokedex.pokemon[entryNumber.minus(entryIndexOffset)] =
                                PokemonMap(
                                    name = pokemonName,
                                    variants = pokemonVariantEntryList
                                )

                            currentSizeIndex += 1

                            // Post value to livingDexList once ALL pokemon details have been collected
                            if (currentSizeIndex == pokedexSize) {
                                Log.d("API", "Current Pokedex#${i} retrieved!")
                                wholePokedex.pokemon += currPokedex.pokemon
                                if (i + 1 == numPokedexes) {
                                    Log.d(
                                        "API",
                                        "Grabbed every Pokemon. Posting..." + currPokedex.toString()
                                    )
                                    _pokedex.value = wholePokedex
                                }
                            } else {
                                Log.d("API", "$currentSizeIndex != $pokedexSize")
                            }
                        }   // End of entries loop
                    }
                }

            }


        } catch (e: Exception) {
            val errorMsg =
                "Error retrieving Pokedex from JSON: ${e.message}"
            Log.e("Error", errorMsg)
            viewModelScope.launch {
                delay(500)
                _fetchingStatus.value = DataStatus.Error(errorMsg)
            }
        }
    }

    fun getCaughtPokemon(gameId: Int) {
        viewModelScope.launch {
            try {
//            Log.d("LivingDexVM", "getCaughtPokemon($gameId)")
                val db = LivingDexTrackerDatabase.getDatabase(context)
                val userPokemonDao = db.userPokemonDao()
                val userPokemonRepository = UserPokemonRepository(userPokemonDao)
                _capturedPokemonList.value = userPokemonRepository.getPokemonCaughtFromGame(gameId)
                updateCapturedSets()
//                Log.d("LivingDexVM", "CaughtPokemon: ${copyCapturedPokemonSet.value}")
            } catch (e: Exception) {
                _fetchingStatus.value = DataStatus.Error(e.toString())
            }
        }
    }

    private fun updateCapturedSets() {
        _capturedPokemonSet.value =
            capturedPokemonList.value.map { it.userPokemonId to it.gameEntryId }.toMutableSet()
        _copyCapturedPokemonSet.value = capturedPokemonSet.value.toMutableSet()
    }

    fun setCopySetToOriginalSet() {
        _copyCapturedPokemonSet.value = _capturedPokemonSet.value.toMutableSet()
    }

    fun addToCopySet(value: Pair<Int?, Int>) {
        _copyCapturedPokemonSet.value = _copyCapturedPokemonSet.value.toMutableSet().apply {
            add(value)
        }
    }

    fun removeFromCopySet(pokemonGameEntryId: Int) {
        _copyCapturedPokemonSet.value = _copyCapturedPokemonSet.value.toMutableSet().apply {
            removeIf { it.second == pokemonGameEntryId }
        }
    }

    fun adjustUserPokemon() {
        val database = LivingDexTrackerDatabase.getDatabase(context)
        val dao = database.userPokemonDao()
        val repository = UserPokemonRepository(dao)
        val newCapturedPokemon = copyCapturedPokemonSet.value - capturedPokemonSet.value
        val removedPokemon = capturedPokemonSet.value - copyCapturedPokemonSet.value

        viewModelScope.launch {
            // Add new captured Pokemon
            // capturedPokemonSet = capturedPokemonList.map { it.userPokemonId to it.gameEntryId }.toMutableSet()
            newCapturedPokemon.forEach {
                val newUserPokemon = UserPokemon(
                    gameEntryId = it.second,
                    gameId = game.value!!.gameId
                )
                repository.insertCaughtPokemon(newUserPokemon)
            }

            // Remove unchecked Pokemon
            removedPokemon.forEach {
                it.first?.let { it1 ->
                    repository.deleteCaughtPokemon(
                        userPokemonId = it1,
                    )
                }
            }
            getCaughtPokemon(game.value!!.gameId)
            updateCapturedSets()
        }
    }

    fun checkIfCaptured(pokemonGameEntryId: Int): Boolean {
        return _copyCapturedPokemonSet.value.any { it.second == pokemonGameEntryId }
    }
}

sealed class DataStatus {
    object Idle : DataStatus()
    data class Loading(val checklist: StateFlow<HashMap<String, Boolean>>) : DataStatus()
    data class Error(val message: String) : DataStatus()
    object Done : DataStatus()
}

data class Pokedex(
    var pokemon: MutableList<PokemonMap>
)

data class PokemonMap(
    var name: String,
    val variants: MutableList<PokemonInGame>
)