import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.emmanuelcastillo.livingdextracker.GetPokemonImage
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun GridCell(
    name: String,
    spriteUrl: String,
    selectable: Boolean,
    captured: Boolean,

    // notifies the parent when a pokemon is checked/unchecked
    onCheckChange: () -> Unit,
    onClick: () -> Unit,
    pokemonId: Int,
    pokemonRvId: Int,
) {
    val cellBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    var imgLoaded by remember { mutableStateOf(false) }
    var showResourceImg by remember { mutableStateOf(false) }
    var showOnlineImg by remember { mutableStateOf(true) }

    if (selectable) {
        Checkbox(
            checked = captured,
            onCheckedChange = { },
            modifier = Modifier
                .size(28.dp)
                .padding(start = 40.dp)
                .zIndex(zIndex = 2F)
        )

    }
    Box(modifier = Modifier
        .size(64.dp)
        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        .background(
            color = if (captured and imgLoaded) Color.Green.copy(alpha = .65f) else cellBackgroundColor.copy(
                alpha = .65f
            ),
            shape = RoundedCornerShape(8.dp)
        )
        .clickable {
            // If not in edit mode, navigate to pokemon screen
            if (!selectable) {
                onClick()
                Log.d("GridCell", "click on cell! ")
            }
            // Else, in edit mode, select/deselect pokemon from captured list
            else {
                onCheckChange()
            }
        }) {

        if (showOnlineImg) {
            AsyncImage(
                model = spriteUrl,
                contentDescription = name,
                modifier = Modifier.matchParentSize(),
                onSuccess = { imgLoaded = true },
                onError = {
                    showResourceImg = true
                    imgLoaded = true
                    showOnlineImg = false
                }
            )
        }


        if (!imgLoaded) {
            Image(
                painter = painterResource(R.drawable.pokeball),
                contentDescription = "Loading",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        } else {
            if (showResourceImg) {
                GetPokemonImage(
                    pokemonId = pokemonId,
                    rvId = pokemonRvId
                )
            }
        }
    }
}
