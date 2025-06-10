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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.PokemonGame
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

    var filterMenuExpanded by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize()) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            FadingText("LivingDex Tracker")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.Black),
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
                                { Text("Gen $i") },
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
                )
            }
        }
    }
}

@Composable
fun GameCarousel(
    numCaughtMap: Map<Int, NumCaughtAndTotal>,
    games: List<PokemonGame>,
    navController: NavController,
) {
    //    A state object that can be hoisted to control and observe scrolling.
    //    In most cases, this will be created via rememberLazyListState.
    val listState = rememberLazyListState()
    //    Create and remember a FlingBehavior for decayed snapping in Lazy Lists.
    //    This will snap the item according to snapPosition.
    val snappingLayout = rememberSnapFlingBehavior(lazyListState = listState)

    val navigateToLivingDexScreen = { endpoint: String ->
        navController.navigate("game/$endpoint")
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // Default values for portrait mode
    val spaceBetweenPosters: Dp
    val horizontalPaddingValue: Dp
    val focusedPosterPaddingMult: Int
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            when {
                screenWidth <= 720 -> {
                    spaceBetweenPosters = 8.dp
                    focusedPosterPaddingMult = 3
                    horizontalPaddingValue = screenWidth.dp / 2.5f
                } // Medium & small phones
                screenWidth <= 960 -> {
                    spaceBetweenPosters = 16.dp
                    focusedPosterPaddingMult = 2
                    horizontalPaddingValue = screenWidth.dp / 2.45f
                } // Large phones
                else -> {
                    spaceBetweenPosters = 20.dp
                    focusedPosterPaddingMult = 3
                    horizontalPaddingValue = screenWidth.dp / 2.45f
                } // Tablets

            }
        }

        else -> {
            when {
                screenWidth <= 360 -> {
                    spaceBetweenPosters = 8.dp
                    focusedPosterPaddingMult = 3
                    horizontalPaddingValue = screenWidth.dp / 5f
                }

                screenWidth <= 412 -> {
                    spaceBetweenPosters = 12.dp
                    focusedPosterPaddingMult = 3
                    horizontalPaddingValue = screenWidth.dp / 6.5f
                } // Large phones
                else -> {
                    spaceBetweenPosters = 16.dp
                    focusedPosterPaddingMult = 3
                    horizontalPaddingValue = screenWidth.dp / 4f
                } // Tablets

            }
        }
    }


    // If game carousel gets filtered, scroll back to the first game
    LaunchedEffect(games) {
        listState.scrollToItem(0)
    }

    LazyRow(
        state = listState,
        flingBehavior = snappingLayout,
        contentPadding = PaddingValues(horizontal = horizontalPaddingValue),
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenPosters),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
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

            Box(
                modifier = Modifier
                    .padding(if (isFocused) (spaceBetweenPosters * focusedPosterPaddingMult) else 0.dp) // Ensures spacing on both sides
                    .zIndex(if (isFocused) 1f else 0f) // Keep focus item on top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.offset(y = if (isFocused) (-20).dp else 0.dp)
                ) {
                    GamePosterItem(
                        imageFileName,
                        navGameId,
                        isFocused = isFocused,
                        navigate = navigateToLivingDexScreen,
                        stats
                    )

                }
            }


        }
    }
}

@Composable
fun isItemFocused(index: Int, listState: LazyListState): Boolean {
    //    The object of LazyListLayoutInfo calculated during the last layout pass. For example, you can use it to calculate what items are currently visible.
    //    Note that this property is observable and is updated after every scroll or remeasure.
    val layoutInfo = remember { derivedStateOf { listState.layoutInfo } }
    val visibleItems = layoutInfo.value.visibleItemsInfo

    val center = layoutInfo.value.viewportStartOffset + layoutInfo.value.viewportSize.width / 2

    return visibleItems
        .minByOrNull { abs((it.offset + it.size / 2) - center) }
        ?.index == index
}

@Composable
fun GamePosterItem(
    imageFileName: String?,
    navGameId: Int?,
    isFocused: Boolean,
    navigate: (String) -> Unit,
    stats: NumCaughtAndTotal?
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Default portrait values
    val scaleFactor: Float
    var basePosterHeight = screenHeight * 0.38f // 30% of screen height


    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            scaleFactor = 1.4f
            basePosterHeight = screenHeight * 0.45f
        }

        else -> {
            scaleFactor = 1.2f
        }
    }

    // Scaling the poster item if focused
    val scale by animateFloatAsState(
        targetValue = if (isFocused) scaleFactor else 1f,
        label = "ScaleAnimation"
    )

    // Animating a vertical up/down infinite for the poster
    val hoverOffset by rememberInfiniteTransition().animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Modifier for poster
    val modifier = Modifier
        .zIndex(if (isFocused) 1f else 0f)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            translationY = if (isFocused) hoverOffset else 0f
        }
        .offset(
            y = if (isFocused) 10.dp else 0.dp
        )
        .height(basePosterHeight)
        .clickable {
            navGameId?.let { navigate(navGameId.toString()) }
        }
//        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
//        .border(1.dp, isSystemInDarkTheme().let { Color.White }, RoundedCornerShape(8.dp))


    //    load the bitmap once in a non-blocking way outside of recomposition
    val context = LocalContext.current
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageFileName) {
        value = withContext(Dispatchers.IO) {
            try {
                imageFileName?.replace(":", "")?.let { fileName ->
                    context.assets.open("Pokemon_Main_Series_Posters/$fileName.webp").use {
                        BitmapFactory.decodeStream(it)?.asImageBitmap()
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = imageFileName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .border(
                        2.dp,
                        isSystemInDarkTheme().let { Color.White }, RoundedCornerShape(8.dp)
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    )


            )
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        if (isFocused) {
            stats?.let {
                if (stats.totalFromGame != 0) {
                    Text(
                        stats.numCaughtFromGame.toString() + "/" + stats.totalFromGame.toString(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-25).dp)
                    )
                }
            }
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


