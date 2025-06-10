package com.emmanuelcastillo.livingdextracker.utils.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonEncounter

@Dao
interface EncounterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEncounter(encounter: PokemonEncounter): Long

    @Query(
        """
    SELECT PE.*, GL.locationName, LA.anchorName FROM  PokemonEncounter AS PE
     INNER JOIN PokemonGameEntry AS PGE USING (gameEntryId)
     INNER JOIN GameLocation AS GL USING (locationId)
     INNER JOIN LocationAnchor AS LA USING (anchorId)
    WHERE PGE.gameEntryId = :gameEntryId
"""
    )
    suspend fun getPokemonEncountersByGame(
        gameEntryId: Int
    ): List<GameEntryEncounter>

    @Query("DELETE FROM PokemonEncounter")
    suspend fun deleteAllEncounters()

    @Query("DELETE FROM LocationAnchor")
    suspend fun deleteAllAnchors()

    @Query("DELETE FROM GameLocation")
    suspend fun deleteAllLocations()
}

data class GameEntryEncounter(
    val encounterId: Int,
    val method: String,
    val timeOfDay: String,
    val itemNeeded: String?,
    val requisite: String?,
    val chance: String,
    val gameEntryId: Int,
    val locationId: Int,
    val anchorId: Int,
    val gameExclusive: Int?,
    val locationName: String,
    val anchorName: String
)