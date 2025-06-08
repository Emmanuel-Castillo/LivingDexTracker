package com.emmanuelcastillo.livingdextracker.utils.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.provider.ContactsContract.Data
import android.util.Log
import com.emmanuelcastillo.livingdextracker.utils.database.daos.GameEntryDetails
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.DataStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun populateEncountersToDatabase(
    context: Context,
    dexId: Int,
    _fetchingStatus: MutableStateFlow<DataStatus>
): Boolean {
    try {
        val db = LivingDexTrackerDatabase.getDatabase(context)

        // 0. Determine game encounters file
        var filePath = ""
        var gameIds = emptyList<Int>()

        Log.d("LivingDexVM", "In populateEncountersToDatabase w/ dexId#$dexId")
        when (dexId) {
            13 -> {
                filePath = "lgpe_encounters.json"
                gameIds = listOf(30, 31)
            }

            14 -> {
                filePath = "swsh_encounters.json"
                gameIds = listOf(32, 33)
            }

            4 -> {
                filePath = "bdsp_encounters.json"
                gameIds = listOf(34, 35)
            }

            15 -> {
                filePath = "lga_encounters.json"
                gameIds = listOf(36)
            }

            16 -> {
                filePath = "sv_encounters.json"
                gameIds = listOf(37, 38)
            }
        }

        Log.d("PopulateDatabase", "filePath == $filePath")
        if (filePath == "") {
            return false
        }

        Log.d("PopulateDatabase", "In populateEncountersToDatabase() w/ dexId: $dexId")

        // 1. Loading JSON from Assets folder
        val jsonString =
            context.assets.open("encounters/$filePath").bufferedReader().use { it.readText() }

        // 2. Parson JSON into objects
        val gson = Gson()
        val encounterMap: Map<String, List<EncounterData>> =
            gson.fromJson(
                jsonString,
                object : TypeToken<Map<String, List<EncounterData>>>() {}.type
            )


        // Utility sets of data to prevent redundant db calls
        // Retrieve GameEntryDetails (from respected Pokedex) from db
        val locationSet = emptySet<Pair<Int, String>>().toMutableSet()
        val anchorSet = emptySet<Triple<Int, Int, String>>().toMutableSet()
        val pokemonGameEntryList = db.pokemonGameDao().getAllEntriesByPokedexId(dexId)

        for ((_, encounters) in encounterMap) {
            for (data in encounters) {

                // To save a PokemonEncounter instance, GameLocation and LocationAnchors must be saved first
                // Grab location from data and see if exists in set. If none exist, create new location
                // b/c for each new location added, it will then be stored in the set
                val locationName = data.location.name
                Log.d("PopulateDatabase", "Location com.emmanuelcastillo.livingdextracker.utils.api.Name: $locationName")
                val location = locationSet.find { it.second == locationName }
                var locationId: Int?
                if (location == null) {
                    val newLocation = GameLocation(
                        locationName = locationName,
                        region = data.location.region
                    )
                    locationId = db.pokemonGameDao().insertGameLocation(
                        newLocation
                    ).toInt()
                    locationSet.add(Pair(locationId, locationName))
                } else {
                    locationId = location.first
                }
                Log.d("PopulateDatabase", "Location id: $locationId")

                // Doing the same setup for location anchors
                val anchorName = data.location.area_anchor
                Log.d("PopulateDatabase", "Anchor com.emmanuelcastillo.livingdextracker.utils.api.Name: $anchorName")
                val anchor =
                    anchorSet.find { (it.third == anchorName) and (it.second == locationId) }
                var anchorId: Int?
                if (anchor == null) {
                    val newAnchor = LocationAnchor(
                        locationId = locationId,
                        anchorName = anchorName
                    )
                    anchorId = db.pokemonGameDao().insertLocationAnchor(newAnchor).toInt()
                    anchorSet.add(Triple(anchorId, locationId, anchorName))
                } else {
                    anchorId = anchor.first
                }
                Log.d("PopulateDatabase", "Anchor id: $anchorId")

                // Grab pokemon id and regional variant id
                val natDexId = data.pokemon.national_dex_id
                val rvId = data.pokemon.rvId

                val pokemonGameEntries: List<GameEntryDetails> =
                    pokemonGameEntryList.filter { (it.nationalDexId == natDexId) and (it.rvId == rvId) }

                // Figure out game exclusivity
                var exclusive_gameIds: List<Int?> = listOf(null)
                if (!data.location.game_version.containsAll(gameIds)) {
                    exclusive_gameIds = data.location.game_version
                }

                for (entry in pokemonGameEntries) {
                    for (exclusiveId in exclusive_gameIds) {
                        val newEncounter = PokemonEncounter(
                            method = data.encounter_method.method,
                            timeOfDay = data.encounter_method.time_of_day,
                            itemNeeded = data.encounter_method.item_needed,
                            requisite = data.encounter_method.requisite,
                            chance = data.encounter_method.chance,
                            gameEntryId = entry.gameEntryId,
                            locationId = locationId,
                            anchorId = anchorId,
                            gameExclusive = exclusiveId
                        )
                        Log.d(
                            "newEncounter", newEncounter.toString()
                        )
                        db.encounterDao().insertEncounter(newEncounter)
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("Database", "Error during population of encounter details: ", e)
        _fetchingStatus.value = DataStatus.Error("Failed to load Pokedex encounters: ${e.message}")
        return false
    }
    return true
}


data class EncounterData(
    val pokemon: PokemonData,
    val encounter_method: EncounterMethodData,
    val location: LocationData
)

data class PokemonData(
    val national_dex_id: Int,
    val name: String,
    val rvId: Int,
)

data class EncounterMethodData(
    val method: String,
    val time_of_day: String,
    val item_needed: String?,
    val requisite: String?,
    val chance: String
)

data class LocationData(
    val name: String,
    val area_anchor: String,
    val region: String,
    val game_version: List<Int>
)