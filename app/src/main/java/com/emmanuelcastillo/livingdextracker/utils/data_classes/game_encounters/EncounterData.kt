package com.emmanuelcastillo.livingdextracker.utils.data_classes.game_encounters

data class EncounterData(
    val pokemon: PokemonData,
    val encounter_method: EncounterMethodData,
    val location: LocationData
)
