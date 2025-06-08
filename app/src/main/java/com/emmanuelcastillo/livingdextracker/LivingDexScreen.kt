import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emmanuelcastillo.livingdextracker.ui.composables.PokemonGrid
import com.emmanuelcastillo.livingdextracker.utils.BDSP
import com.emmanuelcastillo.livingdextracker.utils.LGA
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.DataStatus
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.LivingDexViewModel
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.factories.LivingDexViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

@Composable
fun LivingDexScreen(navController: NavController, gameId: Int) {

    val configuration = LocalConfiguration.current

    // View Models (grabbing living dex, and acquire user caught pokemon for particular game)
    val pokedexModel: LivingDexViewModel = viewModel(
        factory = LivingDexViewModelFactory(
            LocalContext.current.applicationContext as Application,
            gameId = gameId
        )
    )

    // States from the view model
    val game by pokedexModel.game.collectAsState()
    val pokedex by pokedexModel.pokedex.collectAsState()
    val copyCapturedPokemonSet by pokedexModel.copyCapturedPokemonSet.collectAsState()

    // Loading state from view model
    val fetchingDataStatus by pokedexModel.fetchingStatus.collectAsState()

    // UI STATES
    //      - selectable: UI state to make pokemon cells editable for insertion/deletion of UserPokemon entry
    val screenReady by pokedexModel.screenReady.collectAsState()
    var selectable by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    // When returning to this screen, grab updated user pokemon
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                pokedexModel.getCaughtPokemon(gameId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (screenReady and (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            game?.let { GetGameBackground(it) }
        }

        Column {
            val paddingValues: PaddingValues
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> paddingValues =
                    PaddingValues(horizontal = 60.dp, vertical = 20.dp)

                else -> paddingValues = PaddingValues(20.dp)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Button(onClick = { navController.navigate("home") }) {
                    Text("Go back")
                }
                if (screenReady) {
                    Button(onClick = {
                        selectable = !selectable
                        pokedexModel.setCopySetToOriginalSet()
                    }) {
                        Text(if (!selectable) "Edit" else "Cancel")
                    }
                    if (selectable) {
                        Button(onClick = {
                            selectable = !selectable
                            if (!selectable) {
                                // Only save when exiting edit mode
                                pokedexModel.adjustUserPokemon()
                            }
                        }) {
                            Text("Save")
                        }
                    }
                }
            }

            if (screenReady) {
                pokedex?.let {
                    val addToSet = { pair: Pair<Int?, Int> ->
                        pokedexModel.addToCopySet(
                            pair
                        )
                    }
                    val removeFromSet = { pokemonGameEntryId: Int ->
                        pokedexModel.removeFromCopySet(pokemonGameEntryId)
                    }
                    Box(
                        Modifier
                            .padding(paddingValues)
                            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    ) {
                        PokemonGrid(
                            it,
                            copyCapturedPokemonSet,
                            navController,
                            selectable,
                            gameId,
                            addToSet,
                            removeFromSet,
                        )
                    }

                }
            } else {
                val loadingBody = @Composable {
                    if (fetchingDataStatus is DataStatus.Error) {
                        Text(
                            (fetchingDataStatus as DataStatus.Error).message,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.size(16.dp))
                        Button(onClick = {
                            pokedexModel.grabAllData(gameId)
                        }) {
                            Text("Try again")
                        }
                    } else {
                        when (fetchingDataStatus) {
                            is DataStatus.Idle -> Text("Idle...")
                            is DataStatus.Loading -> {
                                val checklist by (fetchingDataStatus as DataStatus.Loading).checklist.collectAsState()
                                val displayOrder = listOf("Game", "Pokedex", "UserPokemon")
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (key in displayOrder) {
                                        val set = checklist[key] ?: false
                                        Row(
                                            Modifier.fillMaxWidth(.8f),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(key)
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = ".".repeat(100),
                                                maxLines = 1,
                                                overflow = TextOverflow.Clip,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            if (!set) CircularProgressIndicator(
                                                Modifier.size(18.dp),
                                                strokeWidth = 2.dp
                                            ) else Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Checkmark"
                                            )
                                        }
                                    }
                                }

                            }

                            is DataStatus.Done -> Text("Loading Data...")
                            else -> {}
                        }
                    }
                }
                LoadingScreen(loadingBody)
            }
        }
    }
}

@Composable
fun GetGameBackground(game: PokemonGame) {
    val context = LocalContext.current

    val bdspBackgrounds = listOf("grid_bg/bd_bg.webp", "grid_bg/sp_bg.webp")
    val lgaBackgrounds = listOf("grid_bg/lga_bg.webp")
    val backgroundEndpoint = when (game.gameId) {
        34, 35 -> bdspBackgrounds.random()
        36 -> lgaBackgrounds.random()
        else -> lgaBackgrounds.random()
    }
    val imageBitmap = remember {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(backgroundEndpoint)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Pokemon ${game.name}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
    } else {
        Text("Image not found")
    }
}
