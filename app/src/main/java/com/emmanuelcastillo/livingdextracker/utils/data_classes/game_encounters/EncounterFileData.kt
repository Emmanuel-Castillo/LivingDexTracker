package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_encounters

import com.google.gson.annotations.SerializedName

data class EncounterFileData(
    @SerializedName("encounters") val encounters: Map<String, List<EncounterData>>
)
