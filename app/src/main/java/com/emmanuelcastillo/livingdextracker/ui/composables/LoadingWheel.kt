import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.ui.theme.LivingDexTrackerTheme

@Composable
fun LoadingScreen(loadingBody: @Composable () -> Unit) {
    val columnBackgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val columnBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .background(
                        columnBackgroundColor.copy(alpha = .65f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, columnBorderColor, shape = RoundedCornerShape(8.dp))
                    .padding(24.dp)
                    .fillMaxWidth(.75f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                loadingBody.invoke()
            }
        }
    }
}

@Preview
@Composable
fun LoadingWheelPreview() {
    val text =
        "Test text asdjflkajsdlkfjasdlflajflajfljadflkjsdalkfjasdklfjasdlfjalfjalsdfjlsdfjladfjlasdjfl"
    val x = @Composable {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 8.dp)
        )
        Text(text, textAlign = TextAlign.Center)
    }
    LivingDexTrackerTheme { LoadingScreen(x) }
}