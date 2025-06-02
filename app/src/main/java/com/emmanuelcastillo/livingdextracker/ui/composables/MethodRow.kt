import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emmanuelcastillo.livingdextracker.ui.theme.BDColor
import com.emmanuelcastillo.livingdextracker.ui.theme.LGEColor
import com.emmanuelcastillo.livingdextracker.ui.theme.LGPColor
import com.emmanuelcastillo.livingdextracker.ui.theme.SHColor
import com.emmanuelcastillo.livingdextracker.ui.theme.SPColor
import com.emmanuelcastillo.livingdextracker.ui.theme.SWColor
import com.emmanuelcastillo.livingdextracker.utils.BDSP
import com.emmanuelcastillo.livingdextracker.utils.LGPE
import com.emmanuelcastillo.livingdextracker.utils.SWSH
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.MethodDetails

@Composable
fun MethodRow(method: Map.Entry<String, MutableList<MethodDetails>>) {
    val methodName = method.key
    val methodDetails = method.value


//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.background(
//            Color.Black.copy(alpha = .2f),
////            shape = RoundedCornerShape(8.dp)
//        )
//    ) {

    Column(Modifier.background(Color.Black.copy(alpha = .2f), shape = RoundedCornerShape(8.dp))) {
        Text(
            methodName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            textAlign = TextAlign.Left,
            fontSize = 16.sp
        )
        for (methodDetail in methodDetails) {

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp)
//                        .padding(
//                            horizontal = 8.dp,
//                            vertical = 4.dp
//                        )
//                        .border(1.dp, Color.Black)
//                ) {
//                    CanvasByGameVersion(methodDetail.game_version)

            val showcaseMethodWeatherAndReqs =
                if ((!methodDetail.itemNeeded.isNullOrEmpty()) or (!methodDetail.requisite.isNullOrEmpty())) true else false

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black)
            ) {
                TimeOfDayForecast(
                    methodDetail.timeOfDayAndChance,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showcaseMethodWeatherAndReqs) {
                    Column(
                        Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        methodDetail.gameExclusive?.let {
//                            Text(methodDetail.gameExclusive.toString())
//                        }

                        methodDetail.requisite?.let {
                            Text(
                                methodDetail.requisite.toString(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        methodDetail.itemNeeded?.let {
                            Text(
                                methodDetail.itemNeeded.toString(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }
        }
    }
}


//Canvas(modifier = Modifier.fillMaxSize()) {
//    val gradiant = Brush.verticalGradient(
//        colors = listOf(
//            startingColor.value,
//            Color.White
//        ),
//        startY = 0f,
//        endY = Float.POSITIVE_INFINITY // Ensures it extends to the bottom
//    )
//    drawRect(brush = gradiant, size = size)
//}
@Composable
fun CanvasByGameVersion(gameVersion: String) {
    val startingColor = when (gameVersion) {
        BDSP -> BDColor
        "BD" -> BDColor
        "SP" -> SPColor
        LGPE -> LGPColor
        "LGP" -> LGPColor
        "LGE" -> LGEColor
        SWSH -> SWColor
        "SW" -> SWColor
        "SH" -> SHColor
        else -> Color.White
    }
    val endingColor = when (gameVersion) {
        BDSP -> SPColor
        LGPE -> LGEColor
        SWSH -> SHColor
        else -> Color.White
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val gradient = Brush.horizontalGradient(
            colors = listOf(startingColor, Color.White, endingColor),
            startX = 0f,
            endX = size.width // Use actual width instead of infinity
        )
        drawRect(brush = gradient, size = size)
    }

}