package com.emmanuelcastillo.livingdextracker.utils.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonGameEntry
import com.emmanuelcastillo.livingdextracker.utils.database.UserPokemon

// UserPokemon Schema
// Entity to store user caught pokemon
//@Entity(tableName = "UserPokemon",foreignKeys = [
//    ForeignKey(
//        entity = PokemonGameEntry::class,
//        parentColumns = ["gameEntryId"],
//        childColumns = ["gameEntryId"]
//    )
//])
//data class UserPokemon(
//    @PrimaryKey(autoGenerate = true)
//    val userPokemonId: Int = 0,
//    val gameEntryId: Int    // References PokemonGameEntry.gameEntryId
//)

@Dao
interface UserPokemonDao {

    @Query("SELECT * FROM UserPokemon WHERE gameEntryId = :id")
    suspend fun checkIfCaughtGameEntry(id: Int): UserPokemon?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // suspend keyword lets the function run on a separate thread
    suspend fun insertCaughtPokemon(pokemon: UserPokemon): Long

    @Query("DELETE FROM UserPokemon WHERE userPokemonId = :id ")
    suspend fun deleteCaughtPokemon(id: Int)

    @Query("DELETE FROM UserPokemon WHERE gameEntryId = :gameEntryId AND gameId = :gameId")
    suspend fun deleteUsingGameEntryId(gameEntryId: Int, gameId: Int)

    // colon notation to reference arguments in the function
    @Query("SELECT UP.userPokemonId, PGE.*, PV.nationalDexId  \n" +
            "FROM PokemonGameEntry AS PGE\n" +
            "INNER JOIN PokemonVariants AS PV USING (variantId)\n" +
            "INNER JOIN UserPokemon AS UP USING (gameEntryId)\n" +
            "INNER JOIN PokemonGame AS PG USING (gameId)\n" +
            "WHERE PG.gameId = :gameId\n")
    suspend fun getPokemonCaughtFromGame(gameId: Int): List<PokemonCaughtFromGame>

    @Query("SELECT " +
            "(SELECT COUNT(*)" +
            "FROM UserPokemon AS UP " +
            "WHERE UP.gameId = :gameId) AS numCaughtFromGame, " +
            "(SELECT COUNT(*) " +
            "FROM PokemonGameEntry AS PGE " +
            "INNER JOIN PokemonGame AS PG USING (pokedexId) " +
            "WHERE gameId = :gameId) AS totalFromGame")
    suspend fun getNumCaughtAndTotalPokemonFromGame(gameId: Int): NumCaughtAndTotal
}

data class PokemonCaughtFromGame(
    val userPokemonId: Int,
    val gameEntryId: Int,
    val pokedexId: Int,
    val variantId: Int,
    val pokedexOrderNumber: Int,
    val nationalDexId: Int
)

data class NumCaughtAndTotal(
    val numCaughtFromGame: Int,
    val totalFromGame: Int
)