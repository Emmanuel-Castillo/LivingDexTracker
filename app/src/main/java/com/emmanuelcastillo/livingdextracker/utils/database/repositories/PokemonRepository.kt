package com.emmanuelcastillo.livingdextracker.utils.database.repositories

import com.emmanuelcastillo.livingdextracker.utils.database.Pokemon
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonVariant
import com.emmanuelcastillo.livingdextracker.utils.database.daos.PokemonDao

class PokemonRepository(private val pokemonDao: PokemonDao) {

    suspend fun getPokemonById(id: Int) = pokemonDao.getPokemonById(id)

    suspend fun getPokemonByVariantId(id: Int) = pokemonDao.getPokemonByVariantId(id)

    suspend fun getPokemonByGameEntryId(id: Int) = pokemonDao.getPokemonByGameEntryId(id)

    suspend fun getAllPokemonFromPokedex(dexId: Int) = pokemonDao.getAllPokemonFromGame(dexId)

    suspend fun insertPokemon(pokemon: Pokemon) = pokemonDao.insertPokemon(pokemon)

    suspend fun insertPokemonVariant(pokemonVariant: PokemonVariant) = pokemonDao.insertPokemonVariant(pokemonVariant)

    suspend fun getAllRegionalVariants() = pokemonDao.getAllRegionalVariants()
}