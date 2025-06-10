package com.emmanuelcastillo.livingdextracker

import LoadingScreen
import LocationRow
import TypeIcon
import android.app.Application
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.emmanuelcastillo.livingdextracker.ui.composables.GalarMap
import com.emmanuelcastillo.livingdextracker.ui.composables.HisuiMap
import com.emmanuelcastillo.livingdextracker.ui.composables.KantoMap
import com.emmanuelcastillo.livingdextracker.ui.composables.PaldeaMap
import com.emmanuelcastillo.livingdextracker.ui.composables.SinnohMap
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.EncounterDetails
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonDataStatus
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonViewModel
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonWithFormattedData
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories.PokemonViewModelFactory
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@Composable
fun PokemonScreen(
    navController: NavHostController,
    gameId: Int,
    gameEntryId: Int,
) {

    Log.d("PokemonScreen", "Made it to screen! gameId: $gameId, gameEntryId: $gameEntryId")
    val context = LocalContext.current.applicationContext
    val configuration = LocalConfiguration.current

    val pokemonViewModel: PokemonViewModel =
        viewModel(
            factory = PokemonViewModelFactory(
                application = context as Application,
                gameId,
                gameEntryId
            )
        )
    val pokemon by pokemonViewModel.pokemon.collectAsState()
    val capturedId by pokemonViewModel.capturedId.collectAsState()
    val pokemonEncounters by pokemonViewModel.pokemonEncounters.collectAsState()
    val encounterLocationsList by pokemonViewModel.encounterLocationsList.collectAsState()
    val focusedLocations by pokemonViewModel.focusedLocations.collectAsState()
    val fetchingDataStatus by pokemonViewModel.fetchingDataStatus.collectAsState()
    val screenReady by pokemonViewModel.screenReady.collectAsState()
    val startingGradiantColor by pokemonViewModel.startingGradiantColor.collectAsState()
    var playCry by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (screenReady) {
            // Background Gradiant
            val endingGradientColor = if (isSystemInDarkTheme()) Color.Black else Color.White
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gradiant = Brush.verticalGradient(
                    colors = listOf(startingGradiantColor, endingGradientColor),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY // Ensures it extends to the bottom
                )
                drawRect(brush = gradiant, size = size)
            }

            val cryUrl = pokemon!!.cry

            // Scrollable Content
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var navRowPadding = 20.dp
                when (configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> navRowPadding = 8.dp
                    else -> 20.dp
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .padding(navRowPadding)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Text("Go Back")
                    }

                    Button(
                        onClick = {
                            pokemonViewModel.toggleCapture()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (capturedId != null) Color.Green else Color.Gray)
                    ) {
                        Text(
                            if (capturedId != null) "Captured!" else "Capture",
                            color = if (capturedId != null) Color.Black else Color.White
                        )
                    }
                }

                when (configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> DisplayPokemonInfoLandscape(
                        pokemon!!,
                        pokemonViewModel = pokemonViewModel,
                        pokemonEncounters = pokemonEncounters
                    )

                    else -> DisplayPokemonInfoPortrait(
                        pokemon!!,
                        pokemonViewModel = pokemonViewModel,
                        gameId = gameId,
                        encounterLocationsList = encounterLocationsList,
                        focusedLocations = focusedLocations,
                        pokemonEncounters = pokemonEncounters
                    )
                }
            }

            // Play the cry once when the details are loaded
//            LaunchedEffect(cryUrl) {
//                if (playCry) {
//                    cryUrl.let {
//                        MediaPlayer().apply {
//                            setDataSource(context, Uri.parse(it))
//                            prepare()
//                            start()
//                            setOnCompletionListener { mp ->
//                                mp.release() // Release mp after playback
//                            }
//                        }
//                    }
//                    playCry = false
//                }
//
//            }
        } else {
            val loadingBody = @Composable {
                Text(
                    when (fetchingDataStatus) {
                        is PokemonDataStatus.Idle -> "Loading Data..."
                        is PokemonDataStatus.Loading -> (fetchingDataStatus as PokemonDataStatus.Loading).message
                        is PokemonDataStatus.Done -> "Fetched Data!"
                        else -> ""
                    }
                )
            }
            LoadingScreen(loadingBody)

        }
    }
}

@Composable
fun DisplayPokemonInfoLandscape(
    pokemon: PokemonWithFormattedData,
    pokemonViewModel: PokemonViewModel,
    pokemonEncounters: EncounterDetails?
) {
    // Night mode mods
    val rowBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White

    Row(modifier = Modifier.fillMaxWidth()) {
        val infoBoxModifier = Modifier
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .background(
                color = rowBackgroundColor.copy(alpha = .4f),
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()

        val infoRowModifier = Modifier
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .background(
                color = rowBackgroundColor.copy(alpha = .4f),
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()

        Column(
            Modifier
                .fillMaxWidth(.6f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                // com.emmanuelcastillo.livingdextracker.utils.api.Name and image
                Column(
                    modifier = Modifier.fillMaxWidth(.4f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GetPokemonImage(pokemon.nationalDexId, pokemon.rvId)
                    Text(
                        "#${pokemon.nationalDexId}: ${pokemon.variantName}",
                        fontSize = 16.sp,
                    )
                }

                Box(infoBoxModifier) {
                    // Display Pokemon Type and Abilities
                    Column(
                        Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = infoRowModifier,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Types:")
                                pokemon.types.forEach { it ->
                                    TypeIcon(it)
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Abilities:")
                                pokemon.abilities.forEach { it ->
                                    Text(it)
                                }
                            }


                        }

                        Row(
                            modifier = infoRowModifier,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Height:")
                                Text(pokemon.heightMetric)
                                Text(pokemon.heightImperial)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Weight:")
                                Text(pokemon.weightMetric)
                                Text(pokemon.weightImperial)
                            }
                        }
                    }
                }

            }
        }

        Box(infoBoxModifier) {
            // Display all Pokemon details
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Display Pokemon Base Stats
                Column(
                    modifier = infoRowModifier.padding(16.dp, 8.dp)
                ) {
                    PokemonBaseStats(pokemon.stats)
                }

                // Display Pokemon Locations
                Column(
                    modifier = infoRowModifier.padding(8.dp)
                ) {
//                data class EncounterDetails(
//                    val locations: MutableMap<String, LocationDetails>
//                )
                    if (pokemonEncounters?.locations.isNullOrEmpty()) {
                        Text("No locations found...")
                    } else {
                        Text("Locations:")

                        val addToFocusedLocations =
                            { locName: String ->
                                pokemonViewModel.addToFocusedLocations(
                                    locName
                                )
                            }
                        val removeFromFocusedLocations = { locName: String ->
                            pokemonViewModel.removeFromFocusedLocations(locName)
                        }
                        pokemonEncounters?.locations?.forEach {
                            LocationRow(
                                it,
                                addToFocusedLocations,
                                removeFromFocusedLocations
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayPokemonInfoPortrait(
    pokemon: PokemonWithFormattedData,
    pokemonViewModel: PokemonViewModel,
    gameId: Int,
    encounterLocationsList: List<String>,
    focusedLocations: List<String>,
    pokemonEncounters: EncounterDetails?
) {
    // Night mode mods
    val rowBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {

        item { // Display Pokemon Sprite and com.emmanuelcastillo.livingdextracker.utils.api.Name
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GetPokemonImage(pokemon.nationalDexId, pokemon.rvId)
                    Text(
                        "#${pokemon.nationalDexId}: ${pokemon.variantName}",
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontSize = 24.sp,
                    )
                }

            }
        }

        val rowModifier = Modifier
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .background(
                color = rowBackgroundColor.copy(alpha = .4f),
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()

        item {
            // Display Pokemon Height and Weight
            Row(
                modifier = rowModifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Height:")
                    Text(pokemon.heightMetric)
                    Text(pokemon.heightImperial)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Weight:")
                    Text(pokemon.weightMetric)
                    Text(pokemon.weightImperial)
                }
            }
        }

        item { // Display Pokemon Type and Abilities
            Row(
                modifier = rowModifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Types:")
                    pokemon.types.forEach { it ->
                        TypeIcon(it)
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Abilities:")
                    pokemon.abilities.forEach { it ->
                        Text(it)
                    }
                }
            }
        }

        item {
            // Display Pokemon Base Stats
            Column(
                modifier = rowModifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                PokemonBaseStats(pokemon.stats)
            }

        }

        item {
            if (!pokemonEncounters?.locations.isNullOrEmpty()) {
                // Display maps
                when (gameId) {
                    30, 31 -> KantoMap(encounterLocationsList, false, focusedLocations)
                    32, 33 -> GalarMap(encounterLocationsList, false, focusedLocations)
                    34, 35 -> SinnohMap(encounterLocationsList, false, focusedLocations)
                    36 -> HisuiMap(encounterLocationsList, false, focusedLocations)
                    37, 38 -> PaldeaMap(encounterLocationsList, false, focusedLocations)
                }
            }

        }

        item {
            // Display Pokemon Locations
            Column(
                modifier = rowModifier.padding(8.dp)
            ) {
//                data class EncounterDetails(
//                    val locations: MutableMap<String, LocationDetails>
//                )
                if (pokemonEncounters?.locations.isNullOrEmpty()) {
                    Text("No locations found...")
                } else {
                    Text("Locations:")

                    val addToFocusedLocations =
                        { locName: String ->
                            pokemonViewModel.addToFocusedLocations(
                                locName
                            )
                        }
                    val removeFromFocusedLocations = { locName: String ->
                        pokemonViewModel.removeFromFocusedLocations(locName)
                    }
                    pokemonEncounters?.locations?.forEach {
                        LocationRow(
                            it,
                            addToFocusedLocations,
                            removeFromFocusedLocations
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GetPokemonImage(pokemonId: Int, rvId: Int) {
    val size = 180.dp
    var fileName = "pokemon_sprites/${
        pokemonId
    }.webp"
    if (rvId != 1) {
        fileName = "pokemon_sprites/${
            pokemonId
        }-${rvId}.webp"
    }
    val context = LocalContext.current
    val imageBitmap = remember {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(
                fileName
            )
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Pokemon $pokemonId",
            modifier = Modifier
                .size(size),
            contentScale = ContentScale.Crop
        )
    } else {
        Text("Image not found")
    }

}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by rememberSaveable {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color.Black
                } else
                    Color(0xFF505050)
            ) // Base background
    ) {
        // Progress background layer
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
        )

        // Foreground content (text labels)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

}

@Composable
fun PokemonBaseStats(
    stats: List<Pair<String, Int>>,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = stats.maxOf { it.second }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base Stats:",
        )
        Spacer(modifier = Modifier.height(4.dp))

        for (i in stats.indices) {
            val stat = stats[i]
            PokemonStat(
                statName = stat.first,
                statValue = stat.second,
                statMaxValue = maxBaseStat,
                statColor = if (isSystemInDarkTheme()) {
                    Color.DarkGray
                } else
                    Color.Black,
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
