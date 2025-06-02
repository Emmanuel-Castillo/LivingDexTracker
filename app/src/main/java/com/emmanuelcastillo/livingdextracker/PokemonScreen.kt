import android.app.Application
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.emmanuelcastillo.livingdextracker.ui.composables.GalarMap
import com.emmanuelcastillo.livingdextracker.ui.composables.HisuiMap
import com.emmanuelcastillo.livingdextracker.ui.composables.KantoMap
import com.emmanuelcastillo.livingdextracker.ui.composables.PaldeaMap
import com.emmanuelcastillo.livingdextracker.ui.composables.SinnohMap
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonDataStatus
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.PokemonViewModel
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories.PokemonViewModelFactory
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonScreen(
    navController: NavHostController,
    gameId: Int,
    gameEntryId: Int,
) {

    val context = LocalContext.current.applicationContext

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

    LaunchedEffect(focusedLocations) {

        Log.d("PokemonScreen", focusedLocations.toString())
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (screenReady) {
            // Background Gradiant
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gradiant = Brush.verticalGradient(
                    colors = listOf(startingGradiantColor, Color.White),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY // Ensures it extends to the bottom
                )
                drawRect(brush = gradiant, size = size)
            }

            // Night mode mods
            val rowBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White

            // Grab details from Pokemon
            val id = pokemon!!.nationalDexId
            val name = pokemon!!.variantName
            val cryUrl = pokemon!!.cry

            val heightMeters = pokemon!!.heightDecimetres.div(10.0)
                .let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP).toDouble() }
            val heightTotalInches = heightMeters.times(39.37).roundToInt()
            val heightFeet = heightTotalInches.div(12)
            val heightInches = heightTotalInches.mod(12)

            val weightKilograms = pokemon?.weightHectograms?.div(10.00)
                ?.let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP).toDouble() }
            val weightPounds = weightKilograms?.times(2.205)
                ?.let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP).toDouble() }

            val types = mutableListOf(pokemon!!.type1)
            pokemon!!.type2?.let { types.add(it) }

            val abilities = mutableListOf(pokemon!!.ability1)
            pokemon!!.ability2?.let { abilities.add(it) }
            pokemon!!.hiddenAbility?.let { abilities.add(it) }

            val stats = listOf(
                Pair("HP", pokemon!!.hpBaseStat),
                Pair("Attack", pokemon!!.atkBaseStat),
                Pair("Defense", pokemon!!.defBaseStat),
                Pair("Sp. Attack", pokemon!!.spAtkBaseStat),
                Pair("Sp. Defense", pokemon!!.spDefBaseStat),
                Pair("Speed", pokemon!!.speedBaseStat),
            )

            // Scrollable Content
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {

                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
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
                }

                item { // Display Pokemon Sprite and Name
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            GetPokemonImage(id, pokemon!!.rvId)
                            Text(
                                "#$id: $name",
                                modifier = Modifier.padding(vertical = 16.dp),
                                fontSize = 24.sp,
                                color = Color.Black
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
                            Text(heightMeters.toString() + "m")
                            Text(heightFeet.toString() + "ft " + heightInches.toString() + "in")
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Weight:")
                            Text(weightKilograms.toString() + "kg")
                            Text(weightPounds.toString() + "lbs")
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
                            types.forEach { it ->
                                TypeIcon(it)
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Abilities:")
                            abilities.forEach { it ->
                                val ability = it.split("-")
                                    .joinToString(" ") { it0 -> it0.replaceFirstChar { it.uppercaseChar() } }
                                Text(ability)
                            }
                        }
                    }
                }

                item {
                    // Display Pokemon Base Stats
                    Column(
                        modifier = rowModifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        stats.forEach { it ->
                            val statName = it.first
                            val statValue = it.second
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$statName: ",
                                    textAlign = TextAlign.Left
                                )
                                Text(
                                    text = statValue.toString(),
                                    textAlign = TextAlign.Right
                                )
                            }
                        }
                    }
                }

//                stickyHeader {
                item {
//                    Box(rowModifier, contentAlignment = Alignment.Center) {
//                        when (gameId) {
//                        }
//                    }

                    // For much bigger maps
                    when (gameId) {
                                  30, 31 -> KantoMap(encounterLocationsList, false, focusedLocations)
                        32, 33 -> GalarMap(encounterLocationsList, false, focusedLocations)
                        34, 35 -> SinnohMap(encounterLocationsList, false, focusedLocations)
                        36 -> HisuiMap(encounterLocationsList, false, focusedLocations)

                        37, 38 -> PaldeaMap(encounterLocationsList, false, focusedLocations)
                    }
                }

//                }

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
            // Play the cry once when the details are loaded
            LaunchedEffect(cryUrl) {
                cryUrl.let {
                    MediaPlayer().apply {
                        setDataSource(context, Uri.parse(it))
                        prepare()
                        start()
                        setOnCompletionListener { mp ->
                            mp.release() // Release mp after playback
                        }
                    }
                }
            }
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
fun GetPokemonImage(pokemonId: Int, rvId: Int) {
    var fileName = "pokemon_sprites/${
        pokemonId
    }.png"
    if (rvId != 1) {
        fileName = "pokemon_sprites/${
            pokemonId
        }-${rvId}.png"
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
            modifier = Modifier.size(180.dp)
        )
    } else {
        Text("Image not found")
    }

}


