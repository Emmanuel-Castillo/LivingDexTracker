import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.utils.viewmodel.LocationDetails

@Composable
fun LocationRow(
    location: Map.Entry<String, LocationDetails>,
    addToFocusedLocations: (String) -> Unit,
    removeFromFocusedLocations: (String) -> Unit
) {
//    data class MethodDetails(
//        val timeOfDay: String?,
//        val chance: String,
//        val requisite: String?,
//        val game_version: String
//    )
//
//    data class AnchorDetails(
//        val methods: MutableMap<String, MutableList<MethodDetails>>,
//    )
//
//    data class LocationDetails(
//        var anchors: MutableMap<String, AnchorDetails>// Groups by anchor
//    )

    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(4.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
    ) {

        // Display Location com.emmanuelcastillo.livingdextracker.utils.api.Name and Expanded Arrows
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(location.key, modifier = Modifier.padding(start = 8.dp))
                IconButton(onClick = {
                    expanded = !expanded
                    if (expanded) {
                        addToFocusedLocations(location.key)
                    } else {
                        removeFromFocusedLocations(location.key)
                    }
                }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )


                }
            }

            // Show Location Anchors if Expanded
            if (expanded) {
                val locationDetails = location.value
                val locationAnchors = locationDetails.anchors

                for (anchor in locationAnchors) {

                    val anchorName = anchor.key
                    val anchorDetails = anchor.value
                    val anchorMethods = anchorDetails.methods

                    // Column to contain each location anchor
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .background(
                                Color.White.copy(alpha = .4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        // Display anchor name
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            text = anchorName,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline
                        )

                        // Looping through each anchor method

                        anchorMethods.onEachIndexed { index, entry ->

                            // Container for each anchor method
                            MethodRow(entry)
                            if (index < anchorMethods.size - 1)
                                Spacer(modifier = Modifier.size(10.dp))
                        }
                    }

                }
            }
        }

    }

}