package com.emmanuelcastillo.livingdextracker.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val TypeNormal = Color(0xFFA8A77A)
val TypeFire = Color(0xFFEE8130)
val TypeWater = Color(0xFF6390F0)
val TypeElectric = Color(0xFFF7D02C)
val TypeGrass = Color(0xFF7AC74C)
val TypeIce = Color(0xFF96D9D6)
val TypeFighting = Color(0xFFC22E28)
val TypePoison = Color(0xFFA33EA1)
val TypeGround = Color(0xFFE2BF65)
val TypeFlying = Color(0xFFA98FF3)
val TypePsychic = Color(0xFFF95587)
val TypeBug = Color(0xFFA6B91A)
val TypeRock = Color(0xFFB6A136)
val TypeGhost = Color(0xFF735797)
val TypeDragon = Color(0xFF6F35FC)
val TypeDark = Color(0xFF705746)
val TypeSteel = Color(0xFFB7B7CE)
val TypeFairy = Color(0xFFD685AD)

val BDColor = Color(16, 50, 255, 255)
val SPColor = Color(255, 84, 247, 255)
val LGPColor = Color(255, 235, 59, 255)
val LGEColor = Color(161, 99, 43, 255)
val SWColor = Color(0, 204, 255, 255)
val SHColor = Color(255, 0, 135, 255)

fun findTypeColor(typeName: String): Color {
    return when (typeName.lowercase()) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.White // Default if type is unknown
    }
}