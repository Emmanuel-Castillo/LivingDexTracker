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
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun GridCell(
    name: String,
    spriteUrl: String,
    selectable: Boolean,
    captured: Boolean,

    // notifies the parent when a pokemon is checked/unchecked
    onCheckChange: () -> Unit,
    onClick: () -> Unit
) {
    val cellBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White

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
        Box(modifier = Modifier.size(64.dp).border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .background(
                color = if (captured) Color.Green.copy(alpha = .65f) else cellBackgroundColor.copy(
                    alpha = .65f
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                // If not in edit mode, navigate to pokemon screen
                if (!selectable) {
                    onClick()
                }
                // Else, in edit mode, select/deselect pokemon from captured list
                else {
                    onCheckChange()
                }

            }) {

            var isLoading by remember { mutableStateOf(true) }

            if (isLoading) {
                Image(
                    painter = painterResource(R.drawable.pokeball),
                    contentDescription = "Loading",
                    modifier = Modifier.fillMaxSize(). padding(10.dp)
                )
            }

            AsyncImage(
                model = spriteUrl,
                contentDescription = name,
                modifier = Modifier.matchParentSize(),
                onSuccess = { isLoading = false }
            )
        }
}
