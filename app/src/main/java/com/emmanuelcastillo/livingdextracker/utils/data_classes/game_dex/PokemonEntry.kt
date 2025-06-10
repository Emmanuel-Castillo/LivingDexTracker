package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_dex

import com.google.gson.annotations.SerializedName

data class PokemonEntry(
    @SerializedName("entry_number") val entry_number: Int,
    @SerializedName("pokemon_species") val pokemon_species: NamedAPIResource
)
