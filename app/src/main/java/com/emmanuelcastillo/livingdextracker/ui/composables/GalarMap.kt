package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun GalarMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>
) {

    val CITY_SHAPE_CONFIG = Triple(10.dp, 10.dp, 45f)
    val LANDMARK_SHAPE_CONFIG = Pair(6.5.dp, 6.5.dp)
    val WILD_AREA_SHAPE_CONFIG = Pair(5.dp, 5.dp)

    val (cityWidth, cityHeight, cityRotation) = CITY_SHAPE_CONFIG
    val (landmarkWidth, landmarkHeight) = LANDMARK_SHAPE_CONFIG
    val (wildAreaWidth, wildAreaHeight) = WILD_AREA_SHAPE_CONFIG

    val imageOriginalSize = IntSize(800, 2055)
    val image = R.drawable.galar

    // Fixed map markers of routes, cities, etc.
    val locationMarkers = listOf(
        MapMarker(
            "Postwick",
            Offset(394.375f, 1975.28f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Wedgehurst",
            Offset(394.375f, 1837.28f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Motostoke",
            Offset(366.108f, 1328.1378f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Turffield",
            Offset(169.91272f, 1170.0358f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Hulbury",
            Offset(555.96094f, 1169f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Hammerlocke",
            Offset(376.95566f, 997.91785f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Spikemuth",
            Offset(686.61597f, 998.03705f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Circhester",
            Offset(628.7307f, 760.234f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Stow-on-Side",
            Offset(175.49838f, 904.8738f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Ballonlea",
            Offset(138.96184f, 695.989f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Wyndon",
            Offset(375.80548f, 443.5476f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Battle Tower",
            Offset(375.73358f, 262.85495f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),
        MapMarker(
            "Slumbering Weald",
            Offset(265.9184f, 1994.8228f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Slumbering Weald",
            Offset(227.88109f, 1949.9236f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Galar Mine",
            Offset(125.75955f, 1275.6431f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Galar Mine No. 2",
            Offset(572.113f, 1264.6967f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Route 9 Tunnel",
            Offset(576.65704f, 1001.8464f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Glimwood Tangle",
            Offset(179.63443f, 742.5739f),
            MarkerShapeClass.RECT(landmarkWidth, landmarkHeight)
        ),
        MapMarker(
            "Meetup Spot",
            Offset(355.80786f, 1689.1453f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Rolling Fields",
            Offset(314.161f, 1614.5852f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Dappled Grove",
            Offset(260.32648f, 1627.6378f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Giant's Seat",
            Offset(480.672f, 1581.6418f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "South Lake Miloch",
            Offset(436.52353f, 1565.0875f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Axew's Eye",
            Offset(337.07904f, 1540.239f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "West Lake Axewell",
            Offset(268.95746f, 1533.4353f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Watch Tower Ruins",
            Offset(285.12616f, 1465.5332f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "East Lake Axewell",
            Offset(373.39453f, 1464.8512f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "North Lake Miloch",
            Offset(463.56888f, 1453.3616f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Motostoke Riverbank",
            Offset(457.95508f, 1370.915f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Bridge Field",
            Offset(438.96634f, 1263.3306f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Stony Wilderness",
            Offset(426.90005f, 1160.8969f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Giant's Mirror",
            Offset(432.43002f, 1118.7455f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Dusty Bowl",
            Offset(393.98633f, 1118.9619f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Giant's Cap",
            Offset(341.24124f, 1119.1053f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Lake of Outrage",
            Offset(309.96875f, 1072.9658f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Hammerlocke Hills",
            Offset(418.7553f, 1060.9934f),
            MarkerShapeClass.RECT(wildAreaWidth, wildAreaHeight)
        ),
        MapMarker(
            "Route 10",
            Offset(393.14127f, 663.75543f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 9",
            Offset(709.7153f, 806.1755f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 8",
            Offset(549.7424f, 888.69556f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 7",
            Offset(524.3262f, 1005.6215f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 6",
            Offset(226.22726f, 970.19495f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 5",
            Offset(290.92844f, 1174.9591f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 4",
            Offset(135.77798f, 1214.2765f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 3",
            Offset(222.49284f, 1331.8021f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 2",
            Offset(475.71933f, 1804.478f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Route 1",
            Offset(407.56604f, 1917.8826f),
            MarkerShapeClass.MARKER
        )


    )

    Map(
        locationMarkers,
        pokemonEncounterLocations,
        devMode,
        focusedLocations,
        imageOriginalSize,
        image
    )
}


