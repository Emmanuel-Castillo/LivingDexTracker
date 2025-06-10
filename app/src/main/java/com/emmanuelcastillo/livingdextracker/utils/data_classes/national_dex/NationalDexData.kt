package com.emmanuelcastillo.livingdextracker.utils.data_classes.national_dex

import com.google.gson.annotations.SerializedName

data class NationalDexData(
    @SerializedName("pokemon") val pokemon: List<PokemonJSONData>
)
