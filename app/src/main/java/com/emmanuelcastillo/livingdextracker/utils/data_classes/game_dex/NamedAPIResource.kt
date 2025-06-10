package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_dex

import com.google.gson.annotations.SerializedName

data class NamedAPIResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
