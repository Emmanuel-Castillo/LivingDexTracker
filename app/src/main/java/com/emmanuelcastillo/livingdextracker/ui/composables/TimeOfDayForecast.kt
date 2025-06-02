import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun TimeOfDayForecast(timeOfDayAndChanceMap: MutableMap<String, String>, modifier: Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            for (entry in timeOfDayAndChanceMap) {
                var displayChance = entry.value
                if ((displayChance == "%") or (displayChance == "-%")) {
                    displayChance = "--%"
                }
//                Box(Modifier.padding(4.dp)) {
                    when (entry.key) {
                        "Morning" -> ComposeMorning(displayChance)
                        "Day" -> ComposeDay(displayChance)
                        "Night" -> ComposeNight(displayChance)
                        "Evening" -> ComposeNight(displayChance)
                        "Anytime" -> ComposeAll(displayChance)
//                    }

                }
            }
        }
    }
}

@Composable
fun ComposeAll(chance: String) {
    ComposeMorning(chance)
    ComposeDay(chance)
    ComposeNight(chance)
}

@Composable
fun ComposeNight(chance: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.night),
            contentDescription = "Night Icon",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            chance,
            Modifier
                .background(Color.White.copy(alpha = .4f), RoundedCornerShape(8.dp))
                .padding(vertical = 2.dp, horizontal = 4.dp),
            fontSize = 14.sp
        )
    }

}

@Composable
fun ComposeDay(chance: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.day),
            contentDescription = "Day Icon",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            chance,
            Modifier
                .background(Color.White.copy(alpha = .4f), RoundedCornerShape(8.dp))
                .padding(vertical = 2.dp, horizontal = 4.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ComposeMorning(chance: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.morning),
            contentDescription = "Morning Icon",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            chance,
            Modifier
                .background(Color.White.copy(alpha = .4f), RoundedCornerShape(8.dp))
                .padding(vertical = 2.dp, horizontal = 4.dp),
            fontSize = 14.sp
        )
    }
}
