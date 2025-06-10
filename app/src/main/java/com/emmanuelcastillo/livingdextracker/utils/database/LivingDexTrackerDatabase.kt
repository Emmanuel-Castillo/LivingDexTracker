package com.emmanuelcastillo.livingdextracker.utils.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmanuelcastillo.livingdextracker.utils.data_classes.national_dex.NationalDexData
import com.emmanuelcastillo.livingdextracker.utils.database.daos.EncounterDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonGameDao
import com.emmanuelcastillo.livingdextracker.utils.database.daos.UserPokemonDao
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.GameLocation
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.LocationAnchor
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.Pokedex
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.Pokemon
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonEncounter
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGameEntry
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonRegion
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonVariant
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.RegionalVariant
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.RegionalVariantAvailability
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.UserPokemon
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PrepopulationState
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

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
    _prepopulationState: MutableStateFlow<PrepopulationState>,
    context: Context
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


            val pokemonCount = pokemonDao.getCountOfPokemon()
            val pokemonVariantsCount = pokemonDao.getCountOfPokemonVariants()
            if ((pokemonCount != NUM_POKEMON) || (pokemonVariantsCount != NUM_POKEMON_VARIANTS)) {
                _prepopulationState.value = PrepopulationState.Loading("Prepopulating Pokemon...")
                prepopulatePokemonToDatabase(context, _prepopulationState)
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




data class VariantJSONData(
    @SerializedName("variant_id") val variantId: Int,
    @SerializedName("variant_name") val variantName: String,
    @SerializedName("is_default") val isDefault: Boolean,
    @SerializedName("type1") val type1: String,
    @SerializedName("type2") val type2: String?,
    @SerializedName("ability_1") val ability1: String,
    @SerializedName("ability_2") val ability2: String?,
    @SerializedName("hidden_ability") val hiddenAbility: String?,
    @SerializedName("height_decimetres") val height: Int,
    @SerializedName("weight_hectograms") val weight: Int,
    @SerializedName("hp")val hp: Int,
    @SerializedName("atk") val attack: Int,
    @SerializedName("defense")val defense: Int,
    @SerializedName("sp_atk") val specialAttack: Int,
    @SerializedName("sp_def") val specialDefense: Int,
    @SerializedName("speed")val speed: Int,
    @SerializedName("cry")val cry: String,
    @SerializedName("sprite")val sprite: String,
    @SerializedName("rv_Id") val rvId: Int
)

suspend fun prepopulatePokemonToDatabase(
    context: Context,
    _prepopulationState: MutableStateFlow<PrepopulationState>
) {
    try {
        val db = LivingDexTrackerDatabase.getDatabase(context)
        val pokemonDao = db.pokemonDao()

        // 1. Loading JSON from Assets folder
        val jsonString =
            context.assets.open("dex/pokemon_national_dex.json").bufferedReader().use { it.readText() }

        // 2. Parson JSON into objects
        val gson = Gson()
        val dexData: NationalDexData =
            gson.fromJson(
                jsonString,
                NationalDexData::class.java
            )

        val dexLength = dexData.pokemon.size
        for (pokemon in dexData.pokemon) {
            _prepopulationState.value = PrepopulationState.PrepopulatingPokemon(
                name = pokemon.name,
                current = pokemon.nationalDexId,
                total = dexLength
            )
            pokemonDao.insertPokemon(Pokemon(nationalDexId = pokemon.nationalDexId, name = pokemon.name))
            for (variant in pokemon.variants) {
                pokemonDao.insertPokemonVariant(
                    PokemonVariant(
                        variantId = variant.variantId,
                        variantName = variant.variantName,
                        nationalDexId = pokemon.nationalDexId,
                        rvId = variant.rvId,
                        isDefault = variant.isDefault,
                        type1 = variant.type1,
                        type2 = variant.type2,
                        ability1 = variant.ability1,
                        ability2 = variant.ability2,
                        hiddenAbility = variant.hiddenAbility,
                        heightDecimetres = variant.height,
                        weightHectograms = variant.weight,
                        hpBaseStat = variant.hp,
                        atkBaseStat = variant.attack,
                        defBaseStat = variant.defense,
                        spAtkBaseStat = variant.specialAttack,
                        spDefBaseStat = variant.specialDefense,
                        speedBaseStat = variant.speed,
                        cry = variant.cry,
                        sprite = variant.sprite
                    )
                )
            }
        }

    } catch (e: Exception) {
        Log.e("Database", "Error during population of encounter details: $e")
        delay(1000)
        _prepopulationState.value =
            PrepopulationState.Error("Unexpected error while prepopulating Pokémon: $e")
    }
}