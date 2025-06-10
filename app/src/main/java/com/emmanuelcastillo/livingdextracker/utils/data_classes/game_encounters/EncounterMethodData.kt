package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_encounters

data class EncounterMethodData(
    val method: String,
    val time_of_day: String,
    val item_needed: String?,
    val requisite: String?,
    val chance: String
)
