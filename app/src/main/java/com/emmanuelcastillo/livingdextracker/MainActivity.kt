package com.emmanuelcastillo.livingdextracker

import GameSelectionScreen
import LivingDexScreen
import LoadingScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.emmanuelcastillo.livingdextracker.ui.theme.LivingDexTrackerTheme
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.MainViewModel
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PrepopulationState
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            // ViewModel data
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(application))
            val appReady by mainViewModel.appReady.collectAsState()
            val prepopState by mainViewModel.prepopulationState.collectAsState()
            val retryPrepopDB = { mainViewModel.prepopDB() }

            LivingDexTrackerTheme {
                if (appReady) {
                    MyApp()
                } else {

                    // Create loading body displaying prepopState for LoadingScreen
                    val loadingBody = @Composable { AccessPrepopState(prepopState, retryPrepopDB) }
                    LoadingScreen(loadingBody)
                }
            }
        }
    }
}

@Composable
fun AccessPrepopState(prepopState: PrepopulationState, retryPrepopDB: () -> Unit) {
    return when (prepopState) {
        is PrepopulationState.Loading -> {
            val message = prepopState.message
            CircularProgressIndicator()
            Text("Loading: $message", textAlign = TextAlign.Center)
        }

        is PrepopulationState.PrepopulatingPokemon -> {
            Text(
                "Saving Pokémon\n(${prepopState.current}/${prepopState.total}):\n${prepopState.name}",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.padding(8.dp))
            LinearProgressIndicator(
                progress = { prepopState.current / prepopState.total.toFloat() },
            )
        }
        // State error message, and prompt user to try again
        is PrepopulationState.Error -> {
            Text(prepopState.message)
            Button(onClick = retryPrepopDB) {
                Text("Try again")
            }
        }

        PrepopulationState.Done -> {
            Text("Done!")
        }

        else -> {}
    }
}

@Preview
@Composable
fun LoadingScreenLoadingPreview() {
    LivingDexTrackerTheme {
        val prepopState: PrepopulationState = PrepopulationState.Loading("Pokemon Games...")
        // Create loading body displaying prepopState for LoadingScreen
        val loadingBody = @Composable { AccessPrepopState(prepopState, {}) }
        LoadingScreen(loadingBody)
    }
}

@Preview
@Composable
fun LoadingScreenPrepopulatingPokemonPreview() {
    LivingDexTrackerTheme {
        val prepopState: PrepopulationState = PrepopulationState.PrepopulatingPokemon(
            name = "Mewtwo",
            current = 151,
            total = 1025
        )
        val loadingBody = @Composable { AccessPrepopState(prepopState, {}) }
        LoadingScreen(loadingBody)
    }
}

// MyApp contains a NavHost composable for self contained navigation
// The following pages are contained at the moment:
//  1. GameSelectionScreen (startDestination)
//  2. LivingDexScreen
//  3. PokemonScreen
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(paddingValues)
        ) {
            composable("home") { GameSelectionScreen(navController = navController) }
            composable(
                "game/{gameId}",
                arguments = listOf(navArgument("gameId") { type = NavType.IntType })
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                LivingDexScreen(navController = navController, gameId)
            }
            composable(
                "pokemon/{gameId}/{gameEntryId}",
                arguments = listOf(
                    navArgument("gameId") { type = NavType.IntType },
                    navArgument("gameEntryId") { type = NavType.IntType },
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                val gameEntryId = backStackEntry.arguments?.getInt("gameEntryId") ?: 0
                PokemonScreen(navController, gameId, gameEntryId)
            }
//            composable("settings") { SettingsScreen() }
        }
    }
}
