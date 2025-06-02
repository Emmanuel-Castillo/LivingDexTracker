package com.emmanuelcastillo.livingdextracker.utils.database.repositories

import com.emmanuelcastillo.livingdextracker.utils.database.daos.EncounterDao

class EncountersRepository(private val encounterDao: EncounterDao) {

    suspend fun getPokemonEncountersByGameEntry(gameEntryId: Int) = encounterDao.getPokemonEncountersByGame(gameEntryId)
}