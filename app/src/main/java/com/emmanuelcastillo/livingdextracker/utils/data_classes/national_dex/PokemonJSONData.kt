package com.emmanuelcastillo.livingdextracker.utils.data_classes.national_dex

import com.google.gson.annotations.SerializedName

data class PokemonJSONData(
    @SerializedName("national_dex_id") val nationalDexId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("variants") val variants: List<VariantJSONData>
)
