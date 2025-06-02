package com.emmanuelcastillo.livingdextracker.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DB_PREPOPULATED = "is_db_prepopulated"
    }

    fun isDbPrepopulated(): Boolean {
        return prefs.getBoolean(KEY_DB_PREPOPULATED, false)
    }

    fun setDbPrepopulated(value: Boolean): Boolean {
        return prefs.edit().putBoolean(KEY_DB_PREPOPULATED, value).commit()
    }

    fun isGameEncountersPrepopulated(pokedexId: Int): Boolean {
        return prefs.getBoolean("encounters_dex#$pokedexId", false)
    }

    fun setGameEncountersPrepopulated(pokedexId: Int, value: Boolean): Boolean {
        return prefs.edit().putBoolean("encounters_dex#$pokedexId", value).commit()
    }

    fun deleteAllFlags(): Boolean {
        return prefs.edit().clear().commit()
    }
}
