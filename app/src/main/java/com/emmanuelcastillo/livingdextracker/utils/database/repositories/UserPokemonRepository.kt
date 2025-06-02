package com.emmanuelcastillo.livingdextracker.utils.database.repositories

import com.emmanuelcastillo.livingdextracker.utils.database.UserPokemon
import com.emmanuelcastillo.livingdextracker.utils.database.daos.UserPokemonDao

class UserPokemonRepository(private val userPokemonDao: UserPokemonDao) {

    suspend fun insertCaughtPokemon(caughtPokemon: UserPokemon): Long = userPokemonDao.insertCaughtPokemon(caughtPokemon)

    suspend fun deleteCaughtPokemon(userPokemonId: Int) = userPokemonDao.deleteCaughtPokemon(userPokemonId)

    suspend fun deleteUsingGameEntryId(gameEntryId: Int, gameId: Int) = userPokemonDao.deleteUsingGameEntryId(gameEntryId, gameId)

    suspend fun getPokemonCaughtFromGame(gameId: Int) = userPokemonDao.getPokemonCaughtFromGame(gameId)

    suspend fun getNumCaughtAndTotalPokemonFromGame(gameId: Int) = userPokemonDao.getNumCaughtAndTotalPokemonFromGame(gameId)
}