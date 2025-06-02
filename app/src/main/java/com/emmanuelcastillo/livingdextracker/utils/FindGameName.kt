package com.emmanuelcastillo.livingdextracker.utils

const val LGPE = "LGPE"
const val SWSH = "SWSH"
const val BDSP = "BDSP"
const val LGA = "LGA"
const val SV = "SV"

fun findGameName(gameId: Int): String {
    return when (gameId) {
        30, 31 -> LGPE
        32, 33 -> SWSH
        13, 14, 34, 35 -> BDSP
        36 -> LGA
        37, 38 -> SV
        else -> "Any"
    }
}

