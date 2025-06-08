package com.emmanuelcastillo.livingdextracker.utils.database

import com.emmanuelcastillo.livingdextracker.utils.api.PokedexResponse
import com.emmanuelcastillo.livingdextracker.utils.api.PokemonResponse
import com.emmanuelcastillo.livingdextracker.utils.api.PokemonSpeciesResponse
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmanuelcastillo.livingdextracker.utils.api.RetrofitClient
import com.emmanuelcastillo.livingdextracker.utils.database.daos.EncounterDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonGameDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.UserPokemonDao
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PrepopulationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

// In the args, list the entity classes for the db and update version number when updating any schema
@Database(
    entities = [Pokedex::class, PokemonRegion::class, LocationAnchor::class, UserPokemon::class, PokemonGameEntry::class, PokemonGame::class, GameLocation::class, PokemonEncounter::class, PokemonVariant::class, RegionalVariant::class, Pokemon::class, RegionalVariantAvailability::class],
    version = 30,
    exportSchema = false
)
abstract class LivingDexTrackerDatabase : RoomDatabase() {

    // Abstract dao functions, Room will generate implementation when building app
    // userPokemonDao: Inserts, gets, and deletes UserPokemon rows
    // encounterDao: Inserts and gets PokemonEncounter rows
    // pokemonDao: Inserts and gets Pokemon, PokemonVariants rows
    // pokemonGameDao: Inserts
    abstract fun userPokemonDao(): UserPokemonDao

    abstract fun encounterDao(): EncounterDao

    abstract fun pokemonDao(): PokemonDao

    abstract fun pokemonGameDao(): PokemonGameDao

    // allows access to the methods to create/get the db and use the class name as qualifier
    companion object {

        @Volatile
        // value of Instance will remain the same for any and all execution threads, as its saved to memory
        private var INSTANCE: LivingDexTrackerDatabase? = null

        // race condition can occur when two threads present, use synchronized {} to avoid it
        fun getDatabase(context: Context): LivingDexTrackerDatabase {
            Log.d(
                "Database",
                "Database path: ${context.applicationContext.getDatabasePath("livingdex_tracker_database")}"
            )
            Log.d("Database", "Fetching database instance $INSTANCE")
            return INSTANCE ?: synchronized(this) {
                Log.d("Database", "Creating new db instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LivingDexTrackerDatabase::class.java,
                    "livingdex_tracker_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance // Ensure we return the instance
            }
        }
    }
}

const val NUM_REGIONS: Int = 10
const val NUM_POKEDEXES: Int = 16
const val NUM_GAMES: Int = 38
const val NUM_RV: Int = 5
const val NUM_RVA: Int = 58
const val NUM_POKEMON: Int = 1025
const val NUM_POKEMON_VARIANTS: Int = 1084

suspend fun prepopulateDatabase(
    db: LivingDexTrackerDatabase,
    _prepopulationState: MutableStateFlow<PrepopulationState>
) {
    try {
        withContext(Dispatchers.IO) {
            Log.d("Database", "Checking to prepopulate data to $db...")
            val pokemonDao = db.pokemonDao()
            val pokemonGameDao = db.pokemonGameDao()

            val regionCount = pokemonGameDao.getCountOfPokemonRegions()
            if (regionCount != NUM_REGIONS) {
                _prepopulationState.value = PrepopulationState.Loading("Prepopulating Regions...")
                pokemonGameDao.insertAllPokemonRegions(prepopulatePokemonRegions())
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database region prepopulation complete"
                )
            } else {
                Log.d("Database", "Pokemon Regions already populated, gameCount: $regionCount")
            }

            val pokedexCount = pokemonGameDao.getCountOfPokedexes()
            if (pokedexCount != NUM_POKEDEXES) {
                _prepopulationState.value = PrepopulationState.Loading("Prepopulating Pokedexes...")
                pokemonGameDao.insertAllPokedexes(prepopulatePokedexes())
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database pokedex prepopulation complete"
                )
            } else {
                Log.d("Database", "Pokedexes already populated, gameCount: $pokedexCount")
            }

            val gameCount = pokemonGameDao.getCountOfPokemonGames()
            if (gameCount != NUM_GAMES) {
                _prepopulationState.value = PrepopulationState.Loading("Prepopulating Games...")
                pokemonGameDao.insertAllPokemonGames(prepopulatePokemonGames())
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database game prepopulation complete"
                )
            } else {
                Log.d("Database", "Pokemon Games already populated, gameCount: $gameCount")
            }

            val regionalVariantCount = pokemonDao.getCountOfRegionalVariants()
            if (regionalVariantCount != NUM_RV) {
                _prepopulationState.value =
                    PrepopulationState.Loading("Prepopulating Regional Variants...")
                pokemonDao.insertAllRegionalVariants(prepopulateRegionalVariants())
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database regional variants prepopulation complete"
                )
            } else {
                Log.d(
                    "Database",
                    "Regional Variants already populated, regionalVariantCount: $regionalVariantCount"
                )
            }

            val rvaCount = pokemonDao.getCountOfRVA()
            if (rvaCount != NUM_RVA) {
                pokemonDao.insertAllRVA(prepopulateRegionalVariantAvailability())
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database regional variant availability prepopulation complete"
                )
            } else {
                Log.d(
                    "Database",
                    "Regional Variant Availability already populated, regionalVariantCount: $rvaCount"
                )
            }

            val regionalVariants = pokemonDao.getAllRegionalVariants()
            Log.d(
                "Database",
                "Fetching regional variants before prepopulateAllPokemon()"
            )
            val pokemonCount = pokemonDao.getCountOfPokemon()
            val pokemonVariantsCount = pokemonDao.getCountOfPokemonVariants()
            if ((pokemonCount != NUM_POKEMON) || (pokemonVariantsCount != NUM_POKEMON_VARIANTS)) {
                _prepopulationState.value = PrepopulationState.Loading("Prepopulating Pokemon...")
                prepopulateAllPokemon(pokemonDao, regionalVariants, _prepopulationState)
                Log.d(
                    "Database",
                    "Back in prepopulateDatabase(): Database pokemon prepopulation complete"
                )
            } else {
                Log.d("Database", "Pokemon already populated, pokemonCount: $pokemonCount")
            }

            Log.d("Database", "End of prepopulateDatabase()")
        }
        if (_prepopulationState.value !is PrepopulationState.Error) {
            _prepopulationState.value = PrepopulationState.Done
        }
    } catch (e: Exception) {
        Log.e("Database", "Error during prepopulation", e)
        _prepopulationState.value =
            PrepopulationState.Error("Failed during prepopulation: ${e.message ?: "Unknown error"}")
    }
}

private fun prepopulatePokemonRegions(): List<PokemonRegion> {
    Log.d("Database", "In prepopulatePokemonRegions(): Prepopulating Pokemon Regions")
    val regions = listOf(
        PokemonRegion("Kanto"),
        PokemonRegion("Johto"),
        PokemonRegion("Hoenn"),
        PokemonRegion("Sinnoh"),
        PokemonRegion("Unova"),
        PokemonRegion("Kalos"),
        PokemonRegion("Alola"),
        PokemonRegion("Galar"),
        PokemonRegion("Hisui"),
        PokemonRegion("Paldea")
    )
    return regions
}

private fun prepopulatePokedexes(): List<Pokedex> {
    Log.d("Database", "In prepopulatePokedexes(): Prepopulating Pokedexes")
    val pokedexes = listOf(
        Pokedex(pokedexId = 1, pokedexName = "Kanto (RBGY) Pokedex"),
        Pokedex(pokedexId = 2, pokedexName = "Johto (GSC) Pokedex"),
        Pokedex(pokedexId = 3, pokedexName = "Hoenn (RSE) Pokedex"),
        Pokedex(pokedexId = 4, pokedexName = "Sinnoh (DP) Pokedex"),
        Pokedex(pokedexId = 5, pokedexName = "Sinnoh (Pt) Pokedex"),
        Pokedex(pokedexId = 6, pokedexName = "Johto (HGSS) Pokedex"),
        Pokedex(pokedexId = 7, pokedexName = "Unova (BW) Pokedex"),
        Pokedex(pokedexId = 8, pokedexName = "Unova (B2W2) Pokedex"),
        Pokedex(pokedexId = 9, pokedexName = "Kalos Pokedex"),
        Pokedex(pokedexId = 10, pokedexName = "Hoenn (ORAS) Pokedex"),
        Pokedex(pokedexId = 11, pokedexName = "Alola (SM) Pokedex"),
        Pokedex(pokedexId = 12, pokedexName = "Alola (USUM) Pokedex"),
        Pokedex(pokedexId = 13, pokedexName = "Kanto (LGPE) Pokedex"),
        Pokedex(pokedexId = 14, pokedexName = "Galar Pokedex"),
        Pokedex(pokedexId = 15, pokedexName = "Hisui Pokedex"),
        Pokedex(pokedexId = 16, pokedexName = "Paldea Pokedex"),
    )
    return pokedexes
}

private fun prepopulatePokemonGames(): List<PokemonGame> {
    Log.d("Database", "In prepopulatePokemonGames(): Prepopulating Pokemon Games")
    val mainSeriesPokemonGames = listOf(
        PokemonGame(1, "Pokémon Red", 1, "Kanto", 2, pokedexId = 1),
        PokemonGame(2, "Pokémon Blue", 1, "Kanto", 2, pokedexId = 1),
        PokemonGame(3, "Pokémon Green", 1, "Kanto", 2, pokedexId = 1),
        PokemonGame(4, "Pokémon Yellow", 1, "Kanto", 2, pokedexId = 1),
        PokemonGame(5, "Pokémon Gold", 2, "Johto", 3, pokedexId = 2),
        PokemonGame(6, "Pokémon Silver", 2, "Johto", 3, pokedexId = 2),
        PokemonGame(7, "Pokémon Crystal", 2, "Johto", 3, pokedexId = 2),
        PokemonGame(8, "Pokémon Ruby", 3, "Hoenn", 4, pokedexId = 3),
        PokemonGame(9, "Pokémon Sapphire", 3, "Hoenn", 4, pokedexId = 3),
        PokemonGame(10, "Pokémon Emerald", 3, "Hoenn", 4, pokedexId = 3),
        PokemonGame(11, "Pokémon FireRed", 3, "Kanto", 2, pokedexId = 1),
        PokemonGame(12, "Pokémon LeafGreen", 3, "Kanto", 2, pokedexId = 1),
        PokemonGame(13, "Pokémon Diamond", 4, "Sinnoh", 5, pokedexId = 4),
        PokemonGame(14, "Pokémon Pearl", 4, "Sinnoh", 5, pokedexId = 4),
        PokemonGame(15, "Pokémon Platinum", 4, "Sinnoh", 6, pokedexId = 5),
        PokemonGame(16, "Pokémon HeartGold", 4, "Johto", 7, pokedexId = 6),
        PokemonGame(17, "Pokémon SoulSilver", 4, "Johto", 7, pokedexId = 6),
        PokemonGame(18, "Pokémon Black", 5, "Unova", 8, pokedexId = 7),
        PokemonGame(19, "Pokémon White", 5, "Unova", 8, pokedexId = 7),
        PokemonGame(20, "Pokémon Black 2", 5, "Unova", 9, pokedexId = 8),
        PokemonGame(21, "Pokémon White 2", 5, "Unova", 9, pokedexId = 8),
        PokemonGame(22, "Pokémon X", 6, "Kalos", 12, pokedexId = 9),
        PokemonGame(23, "Pokémon Y", 6, "Kalos", 12, pokedexId = 9),
        PokemonGame(24, "Pokémon Omega Ruby", 6, "Hoenn", 15, pokedexId = 10),
        PokemonGame(25, "Pokémon Alpha Sapphire", 6, "Hoenn", 15, pokedexId = 10),
        PokemonGame(26, "Pokémon Sun", 7, "Alola", 16, pokedexId = 11),
        PokemonGame(27, "Pokémon Moon", 7, "Alola", 16, pokedexId = 11),
        PokemonGame(28, "Pokémon Ultra Sun", 7, "Alola", 21, pokedexId = 12),
        PokemonGame(29, "Pokémon Ultra Moon", 7, "Alola", 21, pokedexId = 12),
        PokemonGame(30, "Pokémon Let's Go Pikachu", 7, "Kanto", 26, pokedexId = 13),
        PokemonGame(31, "Pokémon Let's Go Eevee", 7, "Kanto", 26, pokedexId = 13),
        PokemonGame(32, "Pokémon Sword", 8, "Galar", 27, pokedexId = 14),
        PokemonGame(33, "Pokémon Shield", 8, "Galar", 27, pokedexId = 14),
        PokemonGame(34, "Pokémon Brilliant Diamond", 8, "Sinnoh", 5, pokedexId = 4),
        PokemonGame(35, "Pokémon Shining Pearl", 8, "Sinnoh", 5, pokedexId = 4),
        PokemonGame(36, "Pokémon Legends: Arceus", 8, "Hisui", 30, pokedexId = 15),
        PokemonGame(37, "Pokémon Scarlet", 9, "Paldea", 31, pokedexId = 16),
        PokemonGame(38, "Pokémon Violet", 9, "Paldea", 31, pokedexId = 16)
    )
    return mainSeriesPokemonGames
}

private fun prepopulateRegionalVariants(): List<RegionalVariant> {
    Log.d(
        "Database",
        "In prepopulateRegionalVariants(): Prepopulating Pokemon Regional Variants"
    )
    val regionalVariants = listOf(
        RegionalVariant(1, null, null),
        RegionalVariant(2, "Alolan", "Alola"),
        RegionalVariant(3, "Galarian", "Galar"),
        RegionalVariant(4, "Hisuian", "Hisui"),
        RegionalVariant(5, "Paldean", "Paldea")
    )
    return regionalVariants
}

private fun prepopulateRegionalVariantAvailability(): List<RegionalVariantAvailability> {
    Log.d(
        "Database",
        "In prepopulateRegionalVariantAvailability(): Prepopulating Pokemon Regional Variant Availability"
    )
//    RegionalVariant(1, null),
//    RegionalVariant(2, "Alolan"),
//    RegionalVariant(3, "Galarian"),
//    RegionalVariant(4, "Hisuian"),
//    RegionalVariant(5, "Paldean")
    val rvaList = mutableListOf(
        // Sun
        RegionalVariantAvailability(1, 26, 2, 1),
        RegionalVariantAvailability(2, 26, 1, 2),
        // Moon
        RegionalVariantAvailability(3, 27, 2, 1),
        RegionalVariantAvailability(4, 27, 1, 2),
        // Ultra Sun
        RegionalVariantAvailability(5, 28, 2, 1),
        RegionalVariantAvailability(6, 28, 1, 2),
        // Ultra Moon
        RegionalVariantAvailability(7, 29, 2, 1),
        RegionalVariantAvailability(8, 29, 1, 2),
        // LGP
        RegionalVariantAvailability(9, 30, 1, 1),
        RegionalVariantAvailability(10, 30, 2, 2),
        // LGE
        RegionalVariantAvailability(11, 31, 1, 1),
        RegionalVariantAvailability(12, 31, 2, 2),
        // SW
        RegionalVariantAvailability(13, 32, 3, 1),
        RegionalVariantAvailability(14, 32, 1, 2),
        RegionalVariantAvailability(15, 32, 2, 3),
        // SH
        RegionalVariantAvailability(16, 33, 3, 1),
        RegionalVariantAvailability(17, 33, 1, 2),
        RegionalVariantAvailability(18, 33, 2, 3),
        // BD
        RegionalVariantAvailability(19, 34, 1, 1),
        // SP
        RegionalVariantAvailability(20, 35, 1, 1),
        // LGA
        RegionalVariantAvailability(21, 36, 4, 1),
        RegionalVariantAvailability(22, 36, 1, 2),
        RegionalVariantAvailability(23, 36, 2, 3),
        // S
        RegionalVariantAvailability(24, 37, 5, 1),
        RegionalVariantAvailability(25, 37, 1, 2),
        RegionalVariantAvailability(26, 37, 2, 3),
        RegionalVariantAvailability(27, 37, 3, 4),
        RegionalVariantAvailability(28, 37, 4, 5),
        // V
        RegionalVariantAvailability(29, 38, 5, 1),
        RegionalVariantAvailability(30, 38, 1, 2),
        RegionalVariantAvailability(31, 38, 2, 3),
        RegionalVariantAvailability(32, 38, 3, 4),
        RegionalVariantAvailability(33, 38, 4, 5),
    )
    val currSize = rvaList.size
    for (i in 1..25) {
        rvaList.add(
            RegionalVariantAvailability(
                currSize + i,
                gameId = i,
                rvId = 1,
                priority = 1,
            )
        )
    }
    return rvaList
}

private fun fetchNationalPokedex(): Response<PokedexResponse>? {
    return try {
        Log.d("Database", "Fetching National Pokedex")
        val call = RetrofitClient.instance.getPokedexById(1)
        Log.d("Database", "Call = $call")
        val response = call.execute()
        if (!response.isSuccessful) {
            Log.e(
                "Database",
                "Failed to fetch National Dex. HTTP ${response.code()}: ${
                    response.errorBody()?.string()
                }"
            )
        }
        response
    } catch (e: Exception) {
        Log.e("Error", "Unable to retrieve National Dex $e")
        null
    }
}

private suspend fun prepopulateAllPokemon(
    pokemonDao: PokemonDao,
    regionalVariants: List<RegionalVariant>,
    _prepopulationState: MutableStateFlow<PrepopulationState>
) {
    try {
        Log.d("Database", "In prepopulateAllPokemon(): Prepopulating Pokemon")
        val response = fetchNationalPokedex()
        if (response == null) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("Failed to connect to the API to fetch the National Dex.")
            return
        }

        if (!response.isSuccessful) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("API error: HTTP ${response.code()} - ${response.message()}")
            return
        }

        val body = response.body()
        if (body == null || body.pokemon_entries.isEmpty()) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("Received empty or malformed data from the National Dex API.")
            return
        }

        Log.d("Database", "National Dex fetched! ${body.pokemon_entries.size} entries")
        val nationalDexSize = body.pokemon_entries.size
        for (entry in body.pokemon_entries) {

            // Check if _prepopulationState has reached an error
            if (_prepopulationState.value is PrepopulationState.Error) {
                return
            }

            val dexId = entry.entry_number
            val pokemonName = fixPokemonName(dexId, entry.pokemon_species.name)

            pokemonDao.insertPokemon(
                Pokemon(nationalDexId = dexId, name = pokemonName)
            )

            prepopulatePokemonVariants(
                pokemonName,
                dexId,
                pokemonDao,
                regionalVariants,
                _prepopulationState,
                nationalDexSize
            )
        }
        Log.d("Database", "Database pokemon prepopulation complete")
    } catch (e: Exception) {
        Log.e("Error", "Error prepopulating all Pokemon: $e")
        delay(1000)
        _prepopulationState.value =
            PrepopulationState.Error("Unexpected error while prepopulating Pokémon: ${e}")
    }
}

fun fixPokemonName(dexId: Int, name: String): String {
    return when (dexId) {
        29 -> "Nidoran♀"
        32 -> "Nidoran♂"
        83 -> "Farfetch'd"
        122 -> "Mr.Mime"
        250 -> "Ho-Oh"
        439 -> "Mime Jr."
        474 -> "Porygon-Z"
        669 -> "Flabébé"
        772 -> "Type: Null"
        865 -> "Sirfetch'd"
        866 -> "Mr. Rime"
        else -> name.split("-")
            .joinToString(" ") { word -> word.replaceFirstChar { c -> c.titlecase() } }
    }
}

private fun fetchPokemonSpecies(pokemonId: Int): Response<PokemonSpeciesResponse>? {
    return try {
        Log.d("Database", "Fetching Species Id: $pokemonId")
        val call = RetrofitClient.instance.getPokemonSpeciesById(pokemonId)
        val response = call.execute()
        if (!response.isSuccessful) {
            Log.e(
                "Database",
                "Failed to fetch National Dex. HTTP ${response.code()}: ${
                    response.errorBody()?.string()
                }"
            )
        }
        response
    } catch (e: Exception) {
        Log.e("Error", "Unable to retrieve Species ${e.message}")
        null
    }
}

private fun fetchPokemonSpeciesVariant(variantId: Int): Response<PokemonResponse>? {
    return try {
        Log.d("Database", "Fetching Species Variant Id: $variantId")
        val call = RetrofitClient.instance.getPokemonById(variantId)
        val response = call.execute()
        if (!response.isSuccessful) {
            Log.e(
                "Database",
                "Failed to fetch National Dex. HTTP ${response.code()}: ${
                    response.errorBody()?.string()
                }"
            )
        }
        response
    } catch (e: Exception) {
        Log.e("Error", "Unable to retrieve Species Variant ${e.message}")
        null
    }
}

private suspend fun prepopulatePokemonVariants(
    pokemonSpeciesName: String,
    natDexId: Int,
    pokemonDao: PokemonDao,
    regionalVariants: List<RegionalVariant>,
    _prepopulationState: MutableStateFlow<PrepopulationState>,
    nationalDexSize: Int
) {
    try {
        Log.d(
            "Database",
            "In prepopulatePokemonVariants(natDexId: $natDexId): Prepopulating Pokemon Variants"
        )
        val response = fetchPokemonSpecies(natDexId)

        if (response == null) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("Failed to fetch species data for $pokemonSpeciesName (Dex ID: $natDexId)")
            return
        }

        if (!response.isSuccessful) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("API error: HTTP ${response.code()} - ${response.message()}")
            return
        }

        val body = response.body()
        if (body == null) {
            delay(1000)
            _prepopulationState.value =
                PrepopulationState.Error("Received empty or malformed data from the Pokemon Species API.")
            return
        }

        // Update loading bar progress indicator
        _prepopulationState.value = PrepopulationState.PrepopulatingPokemon(
            name = pokemonSpeciesName,
            current = natDexId,
            total = nationalDexSize
        )

        val name = body.name.split("-")
            .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
        Log.d("Species", "$name retrieved!")
        val varieties = body.varieties
        if (varieties.isNotEmpty()) {
            for (variety in varieties) {
                val isDefault = variety.is_default

                // Ignore Pikachu cap varieties
                if (!isDefault && natDexId == 25)
                    continue

                // default value if isDefault
                var variantName = pokemonSpeciesName
                var rvId = 1

                Log.d("Variant", "Before parsing: $variantName")
                if (!isDefault) {
                    // Check if regional variant. If so, set rvId found from regionalVariants
                    // Also set correct variantName
                    val splitWords = variety.pokemon.name.split("-")

//                        val regionalVariants = listOf(
//                            RegionalVariant(1, null),
//                            RegionalVariant(2, "Alolan"),
//                            RegionalVariant(3, "Galarian"),
//                            RegionalVariant(4, "Hisuian"),
//                            RegionalVariant(5, "Paldean")
//                        )

                    var rv: RegionalVariant? = null
                    splitWords.forEach { word ->
                        val foundRv = regionalVariants.find {
                            it.nativeRegion?.lowercase()?.equals(word)
                                ?: false
                        }
                        if (foundRv != null) {
                            rv = foundRv
                        }
                    }

                    Log.d("Variant", "Variant found: $rv")

                    if (rv != null) {
                        rvId = rv!!.rvId
                        variantName = rv!!.name + " " + pokemonSpeciesName
                    } else {
                        // Any other variants that aren't regional variants, we skip
                        continue
                    }
                }

                Log.d("Variant", variantName)

                val variantId =
                    variety.pokemon.url.split("/")[6].toInt()
                val varietyResponse = fetchPokemonSpeciesVariant(variantId)

                if (varietyResponse == null) {
                    delay(1000)
                    _prepopulationState.value =
                        PrepopulationState.Error("Failed to fetch variant data for $variantName (Variant ID: $variantId)")
                    return
                }

                if (!varietyResponse.isSuccessful) {
                    delay(1000)
                    _prepopulationState.value =
                        PrepopulationState.Error("API error: HTTP ${response.code()} - ${response.message()}")
                    return
                }

                val varietyBody = varietyResponse.body()
                if (varietyBody == null) {
                    delay(1000)
                    _prepopulationState.value =
                        PrepopulationState.Error("Received empty or malformed data from the Pokemon Species Variant API.")
                    return
                }

                val types = varietyBody.types
                val type1 = types[0].type.name
                    .replaceFirstChar { it.titlecase() }
                var type2: String? = null
                if (types.size > 1) {
                    type2 = types[1].type.name
                        .replaceFirstChar { it.titlecase() }
                }

                val abilities = varietyBody.abilities
                val ability1 =
                    abilities[0].ability.name.split("-")
                        .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
                var abilityCounter = 1
                var ability2: String? = null
                var hiddenAbility: String? = null
                while (abilityCounter < abilities.size) {
                    val nextAbility =
                        abilities[abilityCounter]
                    if (nextAbility.is_hidden) {
                        hiddenAbility =
                            nextAbility.ability.name.split("-")
                                .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
                    } else {
                        ability2 =
                            nextAbility.ability.name.split("-")
                                .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
                    }
                    abilityCounter += 1
                }

                val height = varietyBody.height
                val weight = varietyBody.weight
                val hp = varietyBody.stats[0].base_stat
                val atk = varietyBody.stats[1].base_stat
                val def = varietyBody.stats[2].base_stat
                val spAtk = varietyBody.stats[3].base_stat
                val spDef = varietyBody.stats[4].base_stat
                val speed = varietyBody.stats[5].base_stat
                val cry = varietyBody.cries.latest
                val sprite =
                    varietyBody.sprites.front_default

                CoroutineScope(Dispatchers.IO).launch {
                    pokemonDao.insertPokemonVariant(
                        pokemonVariant = PokemonVariant(
                            variantId = variantId,
                            variantName = variantName,
                            nationalDexId = natDexId,
                            isDefault = isDefault,
                            type1 = type1,
                            type2 = type2,
                            ability1 = ability1,
                            ability2 = ability2,
                            hiddenAbility = hiddenAbility,
                            heightDecimetres = height,
                            weightHectograms = weight,
                            hpBaseStat = hp,
                            atkBaseStat = atk,
                            defBaseStat = def,
                            spAtkBaseStat = spAtk,
                            spDefBaseStat = spDef,
                            speedBaseStat = speed,
                            cry = cry,
                            sprite = sprite,
                            rvId = rvId
                        )
                    )
                }
                Log.d(
                    "Database",
                    "Database Pokemon Variant $variantName prepopulation complete"
                )
            }
            Log.d(
                "Database",
                "Database Pokemon Species $name prepopulation complete"
            )
        }
    } catch (e: Exception) {
        Log.e("Error on prepopulatePokemonVariants()", e.message.toString())
        _prepopulationState.value =
            PrepopulationState.Error("Failed during prepopulation: ${e.message ?: "Unknown error"}")
    }
}

