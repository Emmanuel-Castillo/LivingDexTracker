package com.emmanuelcastillo.livingdextracker.utils.data_classes.national_dex

import com.google.gson.annotations.SerializedName

data class VariantJSONData(
    @SerializedName("variant_id") val variantId: Int,
    @SerializedName("variant_name") val variantName: String,
    @SerializedName("is_default") val isDefault: Boolean,
    @SerializedName("type1") val type1: String,
    @SerializedName("type2") val type2: String?,
    @SerializedName("ability_1") val ability1: String,
    @SerializedName("ability_2") val ability2: String?,
    @SerializedName("hidden_ability") val hiddenAbility: String?,
    @SerializedName("height_decimetres") val height: Int,
    @SerializedName("weight_hectograms") val weight: Int,
    @SerializedName("hp")val hp: Int,
    @SerializedName("atk") val attack: Int,
    @SerializedName("defense")val defense: Int,
    @SerializedName("sp_atk") val specialAttack: Int,
    @SerializedName("sp_def") val specialDefense: Int,
    @SerializedName("speed")val speed: Int,
    @SerializedName("cry")val cry: String,
    @SerializedName("sprite")val sprite: String,
    @SerializedName("rv_Id") val rvId: Int
)
