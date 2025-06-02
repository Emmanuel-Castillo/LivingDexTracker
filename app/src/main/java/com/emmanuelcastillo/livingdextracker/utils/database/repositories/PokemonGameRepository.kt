package com.emmanuelcastillo.livingdextracker.utils.database.repositories

import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonGameDao

class PokemonGameRepository(private val pokemonGameDao: PokemonGameDao) {

    suspend fun getGameById(id: Int) = pokemonGameDao.getGameById(id)
}