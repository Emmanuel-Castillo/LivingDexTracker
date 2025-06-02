import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emmanuelcastillo.livingdextracker.utils.database.PokemonGame
import com.emmanuelcastillo.livingdextracker.utils.database.daos.NumCaughtAndTotal
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.GameSelectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

@Composable
fun GameSelectionScreen(navController: NavController, modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val viewModel: GameSelectionViewModel = viewModel()
    val filteredGames by viewModel.filteredGames.collectAsState()
    val numCaughtMap by viewModel.numCaughtMap.collectAsState()

    val filterGamesByGen = { genNum: Int? -> viewModel.filterByGeneration(genNum)}

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            GameSelectionLandscapeScreen(
                navController, filteredGames = filteredGames, numCaughtMap = numCaughtMap,
                filterByGameGen = filterGamesByGen
            )
        }
        else -> GameSelectionPortraitScreen(navController)
    }
}

@Composable
fun GameSelectionLandscapeScreen(navController: NavController, modifier: Modifier = Modifier, filteredGames: List<PokemonGame>, filterByGameGen: (Int?) -> Unit, numCaughtMap: Map<Int, NumCaughtAndTotal>) {
    var filterMenuExpanded by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize()) {
//        FadingText("LivingDex Tracker")
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            filteredGames.let {
                Box(modifier = Modifier.align(Alignment.Start)) {
                    IconButton(onClick = { filterMenuExpanded = !filterMenuExpanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Filter Games")
                    }
                    DropdownMenu(
                        expanded = filterMenuExpanded,
                        onDismissRequest = { filterMenuExpanded = false },
                        offset = DpOffset(8.dp, 0.dp)
                    ) {
                        DropdownMenuItem(
                            { Text("All") },
                            onClick = {
                                filterByGameGen(null)
                                filterMenuExpanded = false
                            },
                        )
                        for (i in 1..9) {
                            DropdownMenuItem(
                                { Text("Gen ${i}") },
                                onClick = {
                                    filterByGameGen(i)
                                    filterMenuExpanded = false
                                },
                            )
                        }
                    }
                }

                GameCarousel(
                    numCaughtMap,
                    it,
                    navController,
                    380.dp
                )
            }
//            Button(onClick = { navController.navigate("settings") }) { Text("Settings") }
        }
    }
}

@Composable
fun GameSelectionPortraitScreen(navController: NavController, modifier: Modifier = Modifier) {
    var filterMenuExpanded by remember { mutableStateOf(false) }


    val viewModel: GameSelectionViewModel = viewModel()
    val filteredGames by viewModel.filteredGames.collectAsState()
    val numCaughtMap by viewModel.numCaughtMap.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        FadingText("LivingDex Tracker")
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            filteredGames.let {
                Box(modifier = Modifier.align(Alignment.Start)) {
                    IconButton(onClick = { filterMenuExpanded = !filterMenuExpanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Filter Games")
                    }
                    DropdownMenu(
                        expanded = filterMenuExpanded,
                        onDismissRequest = { filterMenuExpanded = false },
                        offset = DpOffset(8.dp, 0.dp)
                    ) {
                        DropdownMenuItem(
                            { Text("All") },
                            onClick = {
                                viewModel.filterByGeneration(null)
                                filterMenuExpanded = false
                            },
                        )
                        for (i in 1..9) {
                            DropdownMenuItem(
                                { Text("Gen ${i}") },
                                onClick = {
                                    viewModel.filterByGeneration(i)
                                    filterMenuExpanded = false
                                },
                            )
                        }
                    }
                }
                GameCarousel(
                    numCaughtMap,
                    it,
                    navController,
                    80.dp
                )
            }
            Button(onClick = { navController.navigate("settings") }) { Text("Settings") }
        }
    }
}


@Composable
fun GameCarousel(
    numCaughtMap: Map<Int, NumCaughtAndTotal>,
    games: List<PokemonGame>,
    navController: NavController,
    lazyRowPaddingValue: Dp
) {
//    A state object that can be hoisted to control and observe scrolling.
//    In most cases, this will be created via rememberLazyListState.
    val listState = rememberLazyListState()
//    Create and remember a FlingBehavior for decayed snapping in Lazy Lists.
//    This will snap the item according to snapPosition.
    val snappingLayout = rememberSnapFlingBehavior(lazyListState = listState)

    val navigateToLivingDexScreen = { endpoint: String ->
        navController.navigate("game/" + endpoint)
    }

    // If game carousel gets filtered, scroll back to the first game
    LaunchedEffect(games) {
        listState.scrollToItem(0)
    }

    LazyRow(
        state = listState,
        flingBehavior = snappingLayout,
        contentPadding = PaddingValues(horizontal = lazyRowPaddingValue),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(games) { index, game ->
            val isFocused = isItemFocused(index, listState)
            val stats = numCaughtMap[game.gameId]

            var imageFileName by remember(game.gameId) { mutableStateOf<String?>(null) }
            var navGameId by remember(game.gameId) { mutableStateOf<Int?>(null) }
            LaunchedEffect(game.gameId) {
                imageFileName = game.name
                navGameId = game.gameId
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.offset(y = if (isFocused) -40.dp else 0.dp)
            ) {
                if (isFocused) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.padding(bottom = 6.dp))
                        Text(game.name, fontSize = 24.sp)
                        stats?.let {
                            if (stats.totalFromGame == 0) {
                                Text("Pokedex Not Grabbed!")
                            } else {
                                Text(stats.numCaughtFromGame.toString() + "/" + stats.totalFromGame.toString())
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(24.dp))
                GamePosterItem(
                    imageFileName,
                    navGameId,
                    isFocused = isFocused,
                    navigate = navigateToLivingDexScreen
                )
            }

        }
    }
}

@Composable
fun isItemFocused(index: Int, listState: LazyListState): Boolean {
//    The object of LazyListLayoutInfo calculated during the last layout pass. For example, you can use it to calculate what items are currently visible.
//    Note that this property is observable and is updated after every scroll or remeasure.
    val layoutInfo = listState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo

    val center = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2

    return visibleItems
        .minByOrNull { abs((it.offset + it.size / 2) - center) }
        ?.index == index
}

@Composable
fun GamePosterItem(
    imageFileName: String?,
    navGameId: Int?,
    isFocused: Boolean,
    navigate: (String) -> Unit
) {

    val configuration = LocalConfiguration.current

    // Default portrait values
    var posterSize = Pair(350.dp, 240.dp)

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> posterSize = Pair(210.dp, 145.dp)
        else -> posterSize = Pair(350.dp, 240.dp)
    }

    // Scaling the poster item if focused
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.2f else 1f,
        label = "ScaleAnimation"
    )

    // Animating a vertical up/down infinite for the poster
    val hoverOffset by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Modifier for poster
    val modifier = Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            translationY = if (isFocused) hoverOffset else 0f
        }
        .size(height = posterSize.first, width = posterSize.second)
        .padding(8.dp)
        .background(Color.DarkGray)
        .border(1.dp, isSystemInDarkTheme().let { Color.White }, RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .clickable {
            navGameId?.let { navigate(navGameId.toString()) }
        }

//    load the bitmap once in a non-blocking way outside of recomposition
    val context = LocalContext.current
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageFileName) {
        value = withContext(Dispatchers.IO) {
            try {
                imageFileName?.replace(":", "")?.let { fileName ->
                    context.assets.open("Pokemon_Main_Series_Posters/$fileName.png").use {
                        BitmapFactory.decodeStream(it)?.asImageBitmap()
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(modifier) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = imageFileName,
            )
        } ?:
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun FadingText(text: String) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    Text(
        text,
        modifier = Modifier
            .alpha(alpha)
            .padding(top = 40.dp),
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )
}
