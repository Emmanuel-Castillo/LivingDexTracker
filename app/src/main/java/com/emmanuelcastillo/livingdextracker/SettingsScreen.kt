package com.emmanuelcastillo.livingdextracker

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.emmanuelcastillo.livingdextracker.utils.PreferencesManager
import com.emmanuelcastillo.livingdextracker.utils.database.LivingDexTrackerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {

    val context = LocalContext.current.applicationContext
    val dataReset = remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { deleteAllEncounters(context, dataReset) }) {
            Text("Delete Encounters")
        }
        if (dataReset.value) {
            Text("Data is reset!")
        }
    }

}

fun deleteAllEncounters(context: Context, dataReset: MutableState<Boolean>) {
    CoroutineScope(Dispatchers.IO).launch {
        val db = LivingDexTrackerDatabase.getDatabase(context)
        val encounterDao = db.encounterDao()
        encounterDao.deleteAllEncounters()
        encounterDao.deleteAllAnchors()
        encounterDao.deleteAllLocations()

        val sharedPrefs = PreferencesManager(context).deleteAllFlags()
        if (sharedPrefs) {
            dataReset.value = true
        }
    }
}