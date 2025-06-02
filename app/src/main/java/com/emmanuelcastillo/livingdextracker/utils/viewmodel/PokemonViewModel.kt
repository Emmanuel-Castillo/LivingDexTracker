package com.emmanuelcastillo.livingdextracker.utils.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emmanuelcastillo.livingdextracker.ui.composables.MapMarker
import com.emmanuelcastillo.livingdextracker.ui.theme.findTypeColor
import com.emmanuelcastillo.livingdextracker.utils.database.LivingDexTrackerDatabase
import com.emmanuelcastillo.livingdextracker.utils.database.UserPokemon
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonWithVariants
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.EncountersRepository
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.PokemonRepository
import com.emmanuelcastillo.livingdextracker.utils.database.repositories.UserPokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.State

@SuppressLint("StaticFieldLeak")
class
PokemonViewModel(application: Application, gameId: Int, gameEntryId: Int) : AndroidViewModel(application) {

    private val _pokemon = MutableStateFlow<PokemonWithVariants?>(null)
    val pokemon: StateFlow<PokemonWithVariants?> = _pokemon.asStateFlow()

    private val _capturedId = MutableStateFlow<Int?>(null)
    val capturedId: StateFlow<Int?> = _capturedId.asStateFlow()

    private val _pokemonEncounters = MutableStateFlow<EncounterDetails?>(null)
    val pokemonEncounters: StateFlow<EncounterDetails?> = _pokemonEncounters.asStateFlow()

    private val _encounterLocationsList = MutableStateFlow<MutableList<String>>(mutableListOf())
    val encounterLocationsList: StateFlow<List<String>> = _encounterLocationsList.asStateFlow()

    private val _focusedLocations = MutableStateFlow<MutableList<String>>(mutableListOf())
    val focusedLocations: StateFlow<List<String>> = _focusedLocations.asStateFlow()

    private val _fetchingDataStatus = MutableStateFlow<PokemonDataStatus>(PokemonDataStatus.Idle)
    val fetchingDataStatus: StateFlow<PokemonDataStatus> = _fetchingDataStatus.asStateFlow()

    private val _screenReady = MutableStateFlow(false)
    val screenReady: StateFlow<Boolean> = _screenReady.asStateFlow()

    private val _startingGradiantColor = MutableStateFlow(Color.White)
    val startingGradiantColor : StateFlow<Color> = _startingGradiantColor.asStateFlow()

    val CONTEXT = application.applicationContext
    val GAME_ID = gameId
    val GAME_ENTRY_ID = gameEntryId

    init {
        viewModelScope.launch {
            Log.d("PokemonVM", "GameEntryId: $gameEntryId")
            grabbingPokemonDetails(gameEntryId)
        }
    }

    suspend fun grabbingPokemonDetails(gameEntryId: Int) {
        _fetchingDataStatus.value = PokemonDataStatus.Idle

        // Grab variant from db
        _fetchingDataStatus.value = PokemonDataStatus.Loading("Loading Pokemon...")
        grabbingVariant(gameEntryId)
        if (_fetchingDataStatus.value is PokemonDataStatus.Error) {
            return
        }
        Log.d("PokemonVM", "Variant grabbed! ${_pokemon.value}")

        // Grab if game entry is captured
        _fetchingDataStatus.value = PokemonDataStatus.Loading("Check if captured...")
        grabbingIfEntryIsCaptured(gameEntryId)
        if (_fetchingDataStatus.value is PokemonDataStatus.Error) {
            return
        }
        Log.d("PokemonVM", "CapturedId grabbed! ${_capturedId.value}")

        // Grab encounters for variant in game from db
        _fetchingDataStatus.value = PokemonDataStatus.Loading("Loading Encounters...")
        grabbingEncounters(gameEntryId)
        if (_fetchingDataStatus.value is PokemonDataStatus.Error) {
            return
        }
        Log.d("PokemonVM", "Encounters grabbed! ${_pokemonEncounters.value}")

        _fetchingDataStatus.value = PokemonDataStatus.Done
        _screenReady.value = true
    }

    suspend fun grabbingVariant(gameEntryId: Int) {
        try {
            val db = LivingDexTrackerDatabase.getDatabase(CONTEXT)
            val pokemonDao = db.pokemonDao()
            val pokemonRepository = PokemonRepository(pokemonDao)

            val cachedPokemon = pokemonRepository.getPokemonByGameEntryId(gameEntryId)
            _pokemon.value = cachedPokemon
            _startingGradiantColor.value = findTypeColor(cachedPokemon.type1)
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            _fetchingDataStatus.value = PokemonDataStatus.Error("Error: ${e.message}")
        }
    }

    suspend fun grabbingIfEntryIsCaptured(gameEntryId: Int) {
        try {
            val db = LivingDexTrackerDatabase.getDatabase(CONTEXT)
            val userPokemonDao = db.userPokemonDao()
            val caughtEntry = userPokemonDao.checkIfCaughtGameEntry(gameEntryId)

            if (caughtEntry != null) {
                _capturedId.value = caughtEntry.userPokemonId
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            _fetchingDataStatus.value = PokemonDataStatus.Error("Error: ${e.message}")
        }
    }

    suspend fun grabbingEncounters(gameEntryId: Int) {
        withContext(Dispatchers.IO) {
            val db = LivingDexTrackerDatabase.getDatabase(CONTEXT)
            val encounterDao = db.encounterDao()
            val encounterRepository = EncountersRepository(encounterDao)
            val encounterList = encounterRepository.getPokemonEncountersByGameEntry(
                gameEntryId
            )
            Log.d("Pokemon Locations", encounterList.toString())

            // Location map
            val pokemonLocationMap = EncounterDetails(
                locations = mutableMapOf()
            )

            for (encounter in encounterList) {
                val gameExclusivity = encounter.gameExclusive
                val locationKey = encounter.locationName
                val locationAnchorKey = encounter.anchorName
                val methodKey = encounter.method

                // Grab location from location map or create location entry
                val location = pokemonLocationMap.locations.getOrPut(locationKey) {
                    LocationDetails(
                        anchors = mutableMapOf()
                    )
                }

                // Grab location anchor or create entry
                val locationAnchor = location.anchors.getOrPut(locationAnchorKey) {
                    AnchorDetails(
                        methods = mutableMapOf(),
                    )
                }

                // Get method if already exists for this anchor, else create new anchor method
                val anchorMethod =
                    locationAnchor.methods.getOrPut(methodKey) { mutableListOf() }

                // Check if anchorMethod has same requisite and game exclusivity as encounter
                val anchorWithSameReqAndGE  =
                    anchorMethod.find { (it.requisite == encounter.requisite) and (it.itemNeeded == encounter.itemNeeded) and (it.gameExclusive == encounter.gameExclusive) }
                if (anchorWithSameReqAndGE != null) {
                    anchorWithSameReqAndGE.timeOfDayAndChance.put(
                        encounter.timeOfDay,
                        encounter.chance
                    )
                } else {
                    // Else, add new method to the anchorMethod list
                    // Group time_of_day, chance, and requisite together
                    val methodDetails =
                        MethodDetails(
                            mutableMapOf(Pair(encounter.timeOfDay, encounter.chance)),
                            encounter.requisite,
                            encounter.itemNeeded,
                            encounter.gameExclusive
                        )

                    // Add method details to methodAnchor list
                    anchorMethod.add(methodDetails)

                }
                Log.d("Encounters", "methodDetails (${locationAnchorKey}): ${anchorMethod}")
            }

            Log.d("Encounters", "${gameEntryId}: ${pokemonLocationMap}")
            _pokemonEncounters.value = pokemonLocationMap

            // Assing map markers
            pokemonLocationMap.locations.forEach { locationName, locationDetails ->
                _encounterLocationsList.value.add(locationName)
            }
        }
    }

    fun toggleCapture() {
        viewModelScope.launch {
            val db = LivingDexTrackerDatabase.getDatabase(CONTEXT)
            val userPokemonDao = db.userPokemonDao()
            val userPokemonRepository = UserPokemonRepository(userPokemonDao)

            if (_capturedId.value == null) {
                // Capture pokemon
                val newUserPokemon = UserPokemon(
                    gameEntryId = GAME_ENTRY_ID,
                    gameId = GAME_ID
                )

                _capturedId.value = userPokemonRepository.insertCaughtPokemon(newUserPokemon).toInt()
            } else {
                userPokemonRepository.deleteUsingGameEntryId(GAME_ENTRY_ID, GAME_ID)
                _capturedId.value = null
            }
        }
    }

    // Add location to list of focused locations for map
    fun addToFocusedLocations(locName: String) {
        _focusedLocations.value = (_focusedLocations.value + locName).toMutableList()
        Log.d("PokemonVM", focusedLocations.value.toString())
    }

    fun removeFromFocusedLocations(locName: String) {
        _focusedLocations.value = (_focusedLocations.value - locName).toMutableList()
        Log.d("PokemonVM", focusedLocations.value.toString())
    }
}

data class MethodDetails(
    val timeOfDayAndChance: MutableMap<String, String>,
    val requisite: String?,
    val itemNeeded: String?,
    val gameExclusive: Int?
)

data class AnchorDetails(
    val methods: MutableMap<String, MutableList<MethodDetails>> // Groups by method
)

data class LocationDetails(
    var anchors: MutableMap<String, AnchorDetails>// Groups by anchor
)

data class EncounterDetails(
    val locations: MutableMap<String, LocationDetails> // Groups by location
)

sealed class PokemonDataStatus {
    object Idle : PokemonDataStatus()
    data class Loading(val message: String) : PokemonDataStatus()
    data class Error(val message: String) : PokemonDataStatus()
    object Done : PokemonDataStatus()
}
