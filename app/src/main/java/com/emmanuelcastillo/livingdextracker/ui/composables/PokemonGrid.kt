package com.emmanuelcastillo.livingdextracker.ui.composables

import GridCell
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.Pokedex

@Composable
fun PokemonGrid(
    livingDexList: Pokedex,
    copyCapturedPokemonSet: Set<Pair<Int?, Int>>,
    navController: NavController,
    selectable: Boolean,
    gameId: Int,
    addToSet: (Pair<Int?, Int>) -> Unit,
    removeFromSet: (Int) -> Unit,
) {

    val configuration = LocalConfiguration.current

    // Default portrait values
    val numCols: Int = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 9
        else -> 6
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(numCols),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(livingDexList.pokemon) { pokemonMap ->
            if (pokemonMap.variants.size > 0) {
                val pokemon = pokemonMap.variants[0]
                val pokemonSprite = pokemon.sprite
                val pokemonGameEntryId = pokemon.gameEntryId

                // check if the item.id is in the copyCapturedPokemonIds Set
                // capturedPokemonSet = capturedPokemonList.map { it.userPokemonId to it.gameEntryId }.toMutableSet()
                var isCaptured = copyCapturedPokemonSet.any { it.second == pokemonGameEntryId }

                val cellOnClick = {
                    Log.d("GridCell", "cellOnClick -> pokemon/$gameId/$pokemonGameEntryId")
                    navController.navigate("pokemon/$gameId/$pokemonGameEntryId")
                }

                val cellCheckOnChange = {
                    isCaptured = !isCaptured
                    // Creating a pair to add/delete from set
                    if (isCaptured) {
                        addToSet(Pair(null, pokemonGameEntryId))
                    } else {
                        removeFromSet(pokemonGameEntryId)
                    }
                }

                pokemonSprite.let {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    ) {
                        GridCell(
                            name = pokemonSprite,
                            spriteUrl = it,
                            selectable = selectable,
                            captured = isCaptured,
                            onCheckChange = { cellCheckOnChange() },
                            onClick = { cellOnClick() },
                            pokemonId = pokemon.nationalDexId,
                            pokemonRvId = pokemon.rvId
                        )
                    }
                }
            }

        }
    }
}