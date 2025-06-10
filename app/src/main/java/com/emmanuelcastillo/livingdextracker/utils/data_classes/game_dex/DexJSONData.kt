package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_dex

import com.google.gson.annotations.SerializedName

data class DexJSONData(
    @SerializedName("entries") val entries: List<PokemonEntry>
)

