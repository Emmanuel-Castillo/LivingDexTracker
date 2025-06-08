package com.emmanuelcastillo.livingdextracker.utils.database.repositories

import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonDao

class PokemonRepository(private val pokemonDao: PokemonDao) {

    suspend fun getPokemonById(id: Int) = pokemonDao.getPokemonById(id)

    suspend fun getPokemonByGameEntryId(id: Int) = pokemonDao.getPokemonByGameEntryId(id)

    suspend fun getAllPokemonFromPokedex(dexId: Int) = pokemonDao.getAllPokemonFromGame(dexId)

}