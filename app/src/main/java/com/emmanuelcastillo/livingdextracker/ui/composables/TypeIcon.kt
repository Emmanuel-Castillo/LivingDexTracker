import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun TypeIcon(type: String) {
    val typeImage: Int = getTypeImage(type)
    Image(
        painter = painterResource(typeImage),
        contentDescription = "Type Image",
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

fun getTypeImage(type: String) : Int {
    return when (type.lowercase()) {
        "normal" -> R.drawable.normal_type
        "fire" -> R.drawable.fire_type
        "water" -> R.drawable.water_type
        "electric" -> R.drawable.electric_type
        "grass" -> R.drawable.grass_type
        "ice" -> R.drawable.ice_type
        "fighting" -> R.drawable.fighting_type
        "poison" -> R.drawable.poison_type
        "ground" -> R.drawable.ground_type
        "flying" -> R.drawable.flying_type
        "psychic" -> R.drawable.psychic_type
        "bug" -> R.drawable.bug_type
        "rock" -> R.drawable.rock_type
        "ghost" -> R.drawable.ghost_type
        "dragon" -> R.drawable.dragon_type
        "dark" -> R.drawable.dark_type
        "steel" -> R.drawable.steel_type
        "fairy" -> R.drawable.fairy_type
        else -> R.drawable.normal_type // Default if type is unknown
    }
}