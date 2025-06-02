import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.emmanuelcastillo.livingdextracker.R
import com.emmanuelcastillo.livingdextracker.utils.BDSP

@Composable
fun LogosList(gameVersion: String, modifier: Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        val gameImage = getGameImage(gameVersion)
        Image(
            painter = painterResource(id = gameImage),
            contentDescription = "BD Logo",
        )
    }
}

fun getGameImage(gameVersion: String): Int {
    return when (gameVersion) {
        BDSP -> R.drawable.bdsp_logo
        "BD" -> R.drawable.bd_logo
        "SP" -> R.drawable.sp_logo
        else -> R.drawable.bd_logo
    }
}