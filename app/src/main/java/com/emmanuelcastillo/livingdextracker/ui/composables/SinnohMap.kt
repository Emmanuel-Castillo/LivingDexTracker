package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun SinnohMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean = false,
    focusedLocations: List<String>
) {

    val CITY_SHAPE_CONFIG = Triple(10.dp, 10.dp, 45f)
    val (cityWidth, cityHeight, cityRotation) = CITY_SHAPE_CONFIG

    // Fixed map markers of routes, cities, etc.
    val locationMarkers = listOf(
        MapMarker(
            "Jubilife City",
            Offset(271.01926f, 402.15036f),
            MarkerShapeClass.RECT(cityWidth, cityHeight)
        ),
        MapMarker(
            "Hearthome City",
            Offset(479.37598f, 364.48352f),
            MarkerShapeClass.RECT(cityWidth, cityHeight)
        ),
        MapMarker(
            "Pastoria City",
            Offset(592.031f, 459.17444f),
            MarkerShapeClass.RECT(cityWidth, cityHeight)
        ),
        MapMarker(
            "Sunnyshore City",
            Offset(743.00946f, 401.56134f),
            MarkerShapeClass.RECT(cityWidth, cityHeight)
        ),
        MapMarker(
            "Veilstone City",
            Offset(648.0192f, 308.10208f),
            MarkerShapeClass.RECT(cityWidth, cityHeight)
        ),
        MapMarker(
            "Canalave City",
            Offset(196.55794f, 383.15396f),
            MarkerShapeClass.RECT(width = 6.dp, height = 14.dp)
        ),
        MapMarker(
            "Floaroma Town",
            Offset(290.53528f, 325.6412f),
            MarkerShapeClass.RECT(width = 6.dp, height = 14.dp)
        ),
        MapMarker(
            "Snowpoint City",
            Offset(423.3486f, 43.270573f),
            MarkerShapeClass.RECT(width = 6.dp, height = 14.dp)
        ),
        MapMarker(
            "Fight Area",
            Offset(592.20215f, 215.35446f),
            MarkerShapeClass.RECT(width = 14.dp, height = 6.dp)
        ),
        MapMarker(
            "Resort Area",
            Offset(704.6197f, 233.0256f),
            MarkerShapeClass.RECT(width = 14.dp, height = 6.dp)
        ),
        MapMarker(
            "Solaceon Town",
            Offset(554.2172f, 345.7912f),
            MarkerShapeClass.RECT(width = 14.dp, height = 6.dp)
        ),
        MapMarker(
            "Eterna City",
            Offset(384.32565f, 269.5569f),
            MarkerShapeClass.FOUR_BY_FOUR_EXCLUDE_CORNER("BottomRight")
        ),
        MapMarker(
            "Oreburgh City",
            Offset(366.15488f, 401.9878f),
            MarkerShapeClass.FOUR_BY_FOUR_EXCLUDE_CORNER("BottomLeft")
        ),
        MapMarker(
            "Celestic Town",
            Offset(480.34085f, 271.05145f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Pal Park",
            Offset(367.03552f, 516.4937f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Sandgem Town",
            Offset(290.96274f, 479.065f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Twinleaf Town",
            Offset(234.65688f, 497.32953f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Survival Area",
            Offset(611.8335f, 157.72209f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Survival Area",
            Offset(611.8335f, 157.72209f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Victory Road",
            Offset(743.5628f, 288.73358f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Pokémon League",
            Offset(743.5628f, 271.41428f),
            MarkerShapeClass.RECT(width = 6.dp, height = 6.dp)
        ),
        MapMarker(
            "Full Moon Island",
            Offset(234.37132f, 121.14821f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker(
            "New Moon Island",
            Offset(290.64325f, 121.41319f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker("Iron Island", Offset(253.87057f, 252.68164f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker(
            "Flower Paradise",
            Offset(781.1086f, 101.61465f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),   // GOOD
        MapMarker("Stark Mountain", Offset(667.9424f, 101.98242f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker("Battle Tower", Offset(612.141f, 195.61754f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker(
            "Valley Windworks",
            Offset(348.47195f, 347.37802f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker(
            "Fuego Ironworks",
            Offset(290.89426f, 308.7659f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker("Old Chateau", Offset(348.3305f, 272.16742f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker("Spear Pillar", Offset(422.55927f, 309.0094f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker("Trophy Garden", Offset(479.88657f, 422.89413f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker(
            "Trophy Garden???",
            Offset(536.2595f, 479.23807f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker("Lost Tower", Offset(554.9335f, 365.93732f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker("Cafe Cabin", Offset(554.84454f, 308.81714f), MarkerShapeClass.CIRCLE(6.dp)),
        MapMarker(
            "Old lay house??",
            Offset(555.25183f, 272.40753f),
            MarkerShapeClass.CIRCLE(6.dp)
        ),
        MapMarker(
            "Route 202",
            Offset(299.3f, 440.91656f),
            MarkerShapeClass.VLINE(height = 36f)
        ),   // GOOD
        MapMarker(
            "Route 219",
            Offset(299.3f, 497.92188f),
            MarkerShapeClass.VLINE(height = 20f)
        ),
        MapMarker(
            "Route 204",
            Offset(299.3f, 365.94727f),
            MarkerShapeClass.VLINE(height = 34.5f)
        ), // GOOD
        MapMarker("Route 217", Offset(393.5f, 75f), MarkerShapeClass.VLINE(height = 127f)),
        MapMarker(
            "Route 206",
            Offset(393.5f, 309.5f),
            MarkerShapeClass.VLINE(height = 81f)
        ),   // GOOD


        MapMarker(
            "Route 227",
            Offset(676f, 119f),
            MarkerShapeClass.VLINE(height = 46f)
        ),   // GOOD
        MapMarker(
            "Route 228",
            Offset(695f, 164.5f),
            MarkerShapeClass.VLINE(height = 56.4f)
        ),
        MapMarker(
            "Acuity Lakefront",
            Offset(390f, 73f),
            MarkerShapeClass.HLINE(width = 32f)
        ),
        MapMarker("Route 216", Offset(390f, 204f), MarkerShapeClass.HLINE(width = 53f)),
        MapMarker("Route 226", Offset(630f, 167f), MarkerShapeClass.HLINE(width = 63f)),
        MapMarker("Route 230", Offset(630f, 223.5f), MarkerShapeClass.HLINE(width = 62f)),
        MapMarker("Route 222", Offset(678.5f, 431f), MarkerShapeClass.HLINE(width = 62.4f)),
        MapMarker(
            "Route 208",
            Offset(438f, 393f),
            MarkerShapeClass.HLINE(width = 40f)
        ),    // GOOD
        MapMarker(
            "Route 203",
            Offset(308.98047f, 412f),
            MarkerShapeClass.HLINE(width = 56f)
        ),  // GOOD
        MapMarker(
            "Route 218",
            Offset(213.95996f, 412f),
            MarkerShapeClass.HLINE(width = 55.5f)
        ),
        MapMarker(
            "Route 220",
            Offset(295.9619f, 525f),
            MarkerShapeClass.HLINE(width = 25f)
        ),
        MapMarker(
            "Route 221",
            Offset(324.96582f, 525f),
            MarkerShapeClass.HLINE(width = 40f)
        ),
        MapMarker(
            "Verity Lakefront",
            Offset(220.97949f, 487.6f),
            MarkerShapeClass.HLINE(width = 19f)
        ),
        MapMarker(
            "Route 211",
            Offset(422f, 280f),
            MarkerShapeClass.HLINE(width = 21f)
        ),    // GOOD
        MapMarker(
            "Route 211",
            Offset(457f, 280f),
            MarkerShapeClass.HLINE(width = 21.5f)
        ),  // GOOD
        MapMarker(
            "Route 201",
            Offset(242.4f, 485.4f),
            MarkerShapeClass.VLINE(height = 10.5f)
        ),     // GOOD
        MapMarker(
            "Route 201",
            Offset(244f, 488f),
            MarkerShapeClass.HLINE(width = 45.5f)
        ),      // GOOD
        MapMarker(
            "Route 207",
            Offset(393f, 390.5f),
            MarkerShapeClass.VLINE(height = 9.5f)
        ),    // GOOD
        MapMarker(
            "Route 207",
            Offset(395f, 393f),
            MarkerShapeClass.HLINE(width = 29f)
        ),        // GOOD
        MapMarker(
            "Route 210",
            Offset(496.97852f, 279.5f),
            MarkerShapeClass.HLINE(width = 57f)
        ),    // GOOD
        MapMarker(
            "Route 210",
            Offset(563f, 289f),
            MarkerShapeClass.VLINE(height = 19f)
        ),   // GOOD
        MapMarker(
            "Route 210",
            Offset(563f, 326f),
            MarkerShapeClass.VLINE(height = 19f)
        ),   // GOOD
        MapMarker(
            "Route 209",
            Offset(563f, 383f),
            MarkerShapeClass.VLINE(height = 13f)
        ),   // GOOD
        MapMarker(
            "Route 209",
            Offset(516.9512f, 393f),
            MarkerShapeClass.HLINE(width = 44f)
        ),      // GOOD
        MapMarker("Route 215", Offset(572f, 318f), MarkerShapeClass.HLINE(width = 74.5f)),
        MapMarker(
            "Route 214",
            Offset(676f, 347f),
            MarkerShapeClass.VLINE(height = 56f)
        ),   // GOOD
        MapMarker(
            "Valor Lakefront",
            Offset(676f, 403f),
            MarkerShapeClass.VLINE(height = 26f)
        ), // GOOD
        MapMarker(
            "Valor Lakefront",
            Offset(653f, 431f),
            MarkerShapeClass.HLINE(width = 26f)
        ),  // GOOD
        MapMarker(
            "Route 212",
            Offset(487.98047f, 403.95996f),
            MarkerShapeClass.VLINE(height = 18f)
        ),   // GOOD
        MapMarker(
            "Route 212",
            Offset(487.98047f, 440f),
            MarkerShapeClass.VLINE(height = 50f)
        ),     // GOOD
        MapMarker(
            "Route 212",
            Offset(490f, 487f),
            MarkerShapeClass.HLINE(width = 45f)
        ),        // GOOD
        MapMarker(
            "Route 212",
            Offset(553f, 487f),
            MarkerShapeClass.HLINE(width = 37.5f)
        ),      // GOOD
        MapMarker(
            "Route 213",
            Offset(652.8261f, 433.88748f),
            MarkerShapeClass.RECT(width = 10.2.dp, height = 11.5.dp)
        ),  // GOOD
        MapMarker(
            "Route 213",
            Offset(629.9678f, 463.9453f),
            MarkerShapeClass.RECT(width = 18.9.dp, height = 11.5.dp)
        ), // GOOD
        MapMarker(
            "Spring Path",
            Offset(678.49866f, 369.63406f),
            MarkerShapeClass.RECT(width = 15.35.dp, height = 11.dp)
        ), // GOOD
        MapMarker(
            "Eterna Forest",
            Offset(313.95703f, 275.5f),
            MarkerShapeClass.RECT(width = 17.5.dp, height = 11.dp)
        ), // GOOD

        MapMarker(
            "Route 205",
            Offset(364.97754f, 280f),
            MarkerShapeClass.HLINE(width = 17.5f)
        ),    // GOOD
        MapMarker(
            "Route 205",
            Offset(307.96387f, 314.01855f),
            MarkerShapeClass.RECT(width = 5.5.dp, height = 17.dp)
        ), // GOOD
        MapMarker(
            "Route 205",
            Offset(322f, 355.93262f),
            MarkerShapeClass.HLINE(width = 25f)
        ),      // GOOD
        MapMarker(
            "Route 205",
            Offset(317.5f, 304.5f),
            MarkerShapeClass.VLINE(height = 8.5f)
        ),   // GOOD

        MapMarker(
            "Mt.Coronet",
            Offset(430.9619f, 326f),
            MarkerShapeClass.VLINE(height = 72f)
        ),   // GOOD
        MapMarker(
            "Mt.Coronet",
            Offset(439.5f, 318f),
            MarkerShapeClass.HLINE(width = 13f)
        ),      // GOOD
        MapMarker(
            "Mt.Coronet",
            Offset(449.5f, 201f),
            MarkerShapeClass.VLINE(height = 110f)
        ),   // GOOD

        MapMarker(
            "Route 223",
            Offset(751.9492f, 314f),
            MarkerShapeClass.VLINE(height = 85f)
        ),   // GOOD

        MapMarker(
            "Route 224",
            Offset(789.4f, 249f),
            MarkerShapeClass.VLINE(height = 28f)
        ),   // GOOD
        MapMarker(
            "Route 224",
            Offset(761f, 279.96094f),
            MarkerShapeClass.HLINE(width = 31f)
        ),      // GOOD

        MapMarker(
            "Route 225",
            Offset(599.9756f, 169.95117f),
            MarkerShapeClass.VLINE(height = 43f)
        ),   // GOOD
        MapMarker(
            "Route 225",
            Offset(597f, 167f),
            MarkerShapeClass.HLINE(width = 13.5f)
        ),      // GOOD

        MapMarker(
            "Route 229",
            Offset(689f, 223.5f),
            MarkerShapeClass.HLINE(width = 48f)
        ),      // GOOD
        MapMarker(
            "Route 229",
            Offset(714f, 226f),
            MarkerShapeClass.VLINE(height = 5.5f)
        ),   // GOOD
        MapMarker(
            "Route 229",
            Offset(733f, 226f),
            MarkerShapeClass.VLINE(height = 5.5f)
        ),   // GOOD


    )

    Map(
        locationMarkers = locationMarkers,
        pokemonEncounterLocations = pokemonEncounterLocations,
        devMode = devMode,
        focusedLocations = focusedLocations,
        imageOriginalSize = IntSize(1268, 734),
        image = R.drawable.sinnoh_bdsp
    )
}





