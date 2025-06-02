package com.emmanuelcastillo.livingdextracker.utils.database.daos

import android.location.Location
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emmanuelcastillo.livingdextracker.utils.database.GameLocation
import com.emmanuelcastillo.livingdextracker.utils.database.LocationAnchor
import com.emmanuelcastillo.livingdextracker.utils.database.Pokedex
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonRegion

@Dao
interface PokemonGameDao {

    @Query("SELECT COUNT(*) FROM PokemonRegion")
    suspend fun getCountOfPokemonRegions(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPokemonRegions(regions: List<PokemonRegion>)

    @Query("SELECT COUNT(*) FROM PokemonGame")
    suspend fun getCountOfPokemonGames(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPokemonGames(games: List<PokemonGame>)

    @Query("SELECT * FROM PokemonGame")
    suspend fun getAllPokemonGames(): List<PokemonGame>

    @Query("SELECT * FROM PokemonGame WHERE gameId = :id")
    suspend fun getGameById(id: Int): PokemonGame

    @Query("SELECT RV.rvId, RV.priority FROM RegionalVariantAvailability AS RV WHERE gameId = :id")
    suspend fun getRegionalVariantsAvailable(id: Int): List<RVA>

    @Query("SELECT COUNT(*) FROM PokemonGameEntry WHERE pokedexId = :dexId")
    suspend fun getCountOfGameEntry(dexId: Int): Int

    @Query("SELECT PGE.gameEntryId , PV.nationalDexId, PV.rvId FROM PokemonVariants AS PV  " +
            "INNER JOIN PokemonGameEntry AS PGE USING(variantId) " +
            "WHERE PGE.pokedexId = :dexId")
    suspend fun getAllEntriesByPokedexId(dexId: Int): List<GameEntryDetails>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGameLocation(location: GameLocation): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocationAnchor(anchor: LocationAnchor): Long

    @Query("SELECT COUNT(*) FROM Pokedex")
    suspend fun getCountOfPokedexes(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPokedexes(pokedexes: List<Pokedex>)
}

data class RVA(
    val rvId: Int,
    val priority: Int
)

data class GameEntryDetails(
    val gameEntryId: Int,
    val nationalDexId: Int,
    val rvId: Int
)

