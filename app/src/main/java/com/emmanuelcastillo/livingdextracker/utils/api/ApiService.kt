package com.emmanuelcastillo.livingdextracker.utils.api

import PokedexResponse
import PokemonResponse
import PokemonSpeciesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokedex/{id}")
    fun getPokedexById(@Path("id") postId: Int): Call<PokedexResponse>

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") postId: Int): Call<PokemonResponse>

    @GET("pokemon-species/{id}")
    fun getPokemonSpeciesById(@Path("id") postId: Int): Call<PokemonSpeciesResponse>
}