package com.emmanuelcastillo.livingdextracker.utils.database.daos

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import com.emmanuelcastillo.livingdextracker.utils.database.Pokemon
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonGameEntry
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonVariant
import com.emmanuelcastillo.livingdextracker.utils.database.RegionalVariant
import com.emmanuelcastillo.livingdextracker.utils.database.RegionalVariantAvailability

@Dao
interface PokemonDao {

    /*


        POKEMON & POKEMONVARIANT DAO OPERATIONS


     */

    // Returning list of PokemonVariants derived from a single Pokemon
    @Query(
        "Select P.name, PV.* FROM PokemonVariants AS PV\n" +
                "INNER JOIN Pokemon AS P USING (nationalDexId)\n" +
                "WHERE P.nationalDexId = :id"
    )
    suspend fun getPokemonById(id: Int): List<PokemonWithVariants>

    // Returning specific PokemonVariant w/ Pokemon name using variant id
    @Query(
        "Select P.name, PV.* FROM PokemonVariants AS PV\n" +
                "INNER JOIN Pokemon AS P USING (nationalDexId)\n" +
                "WHERE PV.variantId = :id"
    )
    suspend fun getPokemonByVariantId(id: Int): PokemonWithVariants

    // Returning specific PokemonVariant w/ Pokemon name using game entry id
    @Query(
        "Select P.name, PV.* FROM PokemonVariants AS PV\n" +
                "INNER JOIN Pokemon AS P USING (nationalDexId)\n" +
                "INNER JOIN PokemonGameEntry AS PGE USING (variantId)\n" +
                "WHERE PGE.gameEntryId = :id"
    )
    suspend fun getPokemonByGameEntryId(id: Int): PokemonWithVariants

    // Returning all Pokemon, their variants, and entry details, from particular pokedex, considering regional variants available in particular game
    @Query(
        "Select PGE.gameEntryId, PGE.pokedexOrderNumber, P.name, RVA.priority, PV.* FROM PokemonVariants AS PV\n" +
                "INNER JOIN Pokemon AS P USING (nationalDexId)\n" +
                "INNER JOIN PokemonGameEntry AS PGE USING (variantId)\n" +
                "INNER JOIN PokemonGame AS PG USING (pokedexId)\n" +
                "INNER JOIN RegionalVariantAvailability AS RVA USING (gameId, rvId)\n" +
                "INNER JOIN Pokedex AS PD USING (pokedexId)\n" +
                "WHERE PD.pokedexId = :dexId\n" +
                "ORDER BY PGE.pokedexOrderNumber ASC, RVA.priority ASC"
    )
    suspend fun getAllPokemonFromGame(dexId: Int): List<PokemonInGame>


    /*


        GAME ENTRY DAO OPERATIONS


     */


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGameEntry(gameEntry: PokemonGameEntry): Long

    @Query("Select PGE.gameEntryId, PGE.pokedexOrderNumber, P.name, RVA.priority, PV.* FROM PokemonVariants AS PV\n" +
            "INNER JOIN Pokemon AS P USING (nationalDexId)\n" +
            "INNER JOIN PokemonGameEntry AS PGE USING (variantId)\n" +
            "INNER JOIN PokemonGame AS PG USING (pokedexId)\n" +
            "INNER JOIN RegionalVariantAvailability AS RVA USING (gameId, rvId)\n" +
            "INNER JOIN Pokedex AS PD USING (pokedexId) " +
            "WHERE PV.variantId = :variantId AND PD.pokedexId = :dexId"
    )
    suspend fun hasGameEntry(variantId: Int, dexId: Int): PokemonInGame?

    // Inserting new Pokemon
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: Pokemon)

    @Query("SELECT COUNT(*) FROM Pokemon")
    suspend fun getCountOfPokemon(): Int

    @Query("SELECT COUNT(*) FROM PokemonVariants")
    suspend fun getCountOfPokemonVariants(): Int

    // Inserting new PokemonForm
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonVariant(pokemonVariant: PokemonVariant)

    @Query("SELECT * from RegionalVariants")
    suspend fun getAllRegionalVariants(): List<RegionalVariant>

    @Query("SELECT COUNT(*) FROM RegionalVariants")
    suspend fun getCountOfRegionalVariants(): Int

    // Inserting regional variants (Done when instantiating db)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRegionalVariants(regionalVariants: List<RegionalVariant>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRVA(rvaList: List<RegionalVariantAvailability>)

    @Query("SELECT COUNT(*) FROM RegionalVariantAvailability")
    suspend fun getCountOfRVA(): Int

    @Query("SELECT * FROM RegionalVariantAvailability")
    suspend fun getAllRVA(): List<RegionalVariantAvailability>

    @Query("DELETE FROM UserPokemon")
    suspend fun deleteAllUserPokemon()
}

data class PokemonWithVariants(
    val name: String,
    val variantId: Int,
    val variantName: String,
    val nationalDexId: Int,
    val rvId: Int,
    val isDefault: Boolean,
    val type1: String,
    val type2: String?,
    val ability1: String,
    val ability2: String?,
    val hiddenAbility: String?,
    val heightDecimetres: Int,
    val weightHectograms: Int,
    val hpBaseStat: Int,
    val atkBaseStat: Int,
    val defBaseStat: Int,
    val spAtkBaseStat: Int,
    val spDefBaseStat: Int,
    val speedBaseStat: Int,
    val cry: String,
    val sprite: String
)

data class PokemonInGame(
    val gameEntryId: Int,
    val pokedexOrderNumber: Int,
    val name: String,
    val priority: Int,
    val variantId: Int,
    val variantName: String,
    val nationalDexId: Int,
    val rvId: Int,
    val isDefault: Boolean,
    val type1: String,
    val type2: String?,
    val ability1: String,
    val ability2: String?,
    val hiddenAbility: String?,
    val heightDecimetres: Int,
    val weightHectograms: Int,
    val hpBaseStat: Int,
    val atkBaseStat: Int,
    val defBaseStat: Int,
    val spAtkBaseStat: Int,
    val spDefBaseStat: Int,
    val speedBaseStat: Int,
    val cry: String,
    val sprite: String
)
