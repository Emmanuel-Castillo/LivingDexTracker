package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun KantoMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>
) {

    val CITY_SHAPE_CONFIG = Triple(10.dp, 10.dp, 0f)
    val ROW_WIDTH = 4.5.dp

    val (cityWidth, cityHeight, cityRotation) = CITY_SHAPE_CONFIG

    // Fixed map markers of routes, cities, etc.
    val locationMarkers = listOf(
        MapMarker(
            "Indigo Plateau",
            Offset(51.430176f, 65.29196f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Viridian City",
            Offset(98.11841f, 205.88918f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Pallet Town",
            Offset(98.060036f, 277.0229f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Cinnabar Island",
            Offset(97.94922f, 371.0127f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Fuchsia City",
            Offset(239.75827f, 323.74487f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Vermillion City",
            Offset(286.96387f, 228.94336f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Pewter City",
            Offset(97.59439f, 87.74738f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Cerulean City",
            Offset(287.45215f, 65.08522f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Saffron City",
            Offset(287.1662f, 135.79767f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Lavender Town",
            Offset(380.94147f, 136.42416f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Celadon Town",
            Offset(215.54785f, 135.97852f),
            MarkerShapeClass.RECT(cityWidth, cityHeight, cityRotation)
        ),MapMarker(
            "Seafoam Islands",
            Offset(168.52077f, 371.778f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Victory Road",
            Offset(51.36905f, 112.4897f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Viridian Forest",
            Offset(97.984825f, 136.10788f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Diglett's Cave",
            Offset(122.01755f, 111.823975f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Mt. Moon",
            Offset(192.94656f, 64.90483f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Rock Tunnel",
            Offset(382.41992f, 89.29378f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Power Plant",
            Offset(404.63788f, 112.31987f),
            MarkerShapeClass.CIRCLE(9.5.dp)
        ),MapMarker(
            "Route 1",
            Offset(104.792206f, 233.44803f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 16.5.dp)
        ),MapMarker(
            "Route 2",
            Offset(105.16766f, 161.92967f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 16.5.dp)
        ),MapMarker(
            "Route 2",
            Offset(104.96387f, 114.95801f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 7.5.dp)
        ),MapMarker(
            "Route 3",
            Offset(124.98035f, 94.79148f),
            MarkerShapeClass.RECT(width = 24.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 3",
            Offset(175.98062f, 71.62876f),
            MarkerShapeClass.RECT(width = 6.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 3",
            Offset(175.99121f, 83.97656f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 4.dp)
        ),MapMarker(
            "Route 4",
            Offset(218.77861f, 71.82434f),
            MarkerShapeClass.RECT(width = 25.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 24",
            Offset(294.0716f, 24.209389f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 15.2.dp)
        ),MapMarker(
            "Route 25",
            Offset(306.0838f, 24.19591f),
            MarkerShapeClass.RECT(width = 18.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 9",
            Offset(313.57806f, 72.18729f),
            MarkerShapeClass.RECT(width = 28.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 10",
            Offset(387.9834f, 71.96484f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 6.dp)
        ),MapMarker(
            "Route 10",
            Offset(388.1654f, 114.5352f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 7.5.dp)
        ),MapMarker(
            "Route 12",
            Offset(387.9834f, 162.93164f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 50.5.dp)
        ),MapMarker(
            "Route 5",
            Offset(293.90482f, 91.8638f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 16.5.dp)
        ),MapMarker(
            "Route 6",
            Offset(293.90482f, 162.64221f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 24.7.dp)
        ),MapMarker(
            "Route 8",
            Offset(313.95703f, 141.98438f),
            MarkerShapeClass.RECT(width = 25.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 7",
            Offset(242.96387f, 141.98438f),
            MarkerShapeClass.RECT(width = 16.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 16",
            Offset(152.0098f, 141.98438f),
            MarkerShapeClass.RECT(width = 23.5.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 17",
            Offset(151.96387f, 153.94336f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 72.dp)
        ),MapMarker(
            "Route 17",
            Offset(163.7591f, 330.88562f),
            MarkerShapeClass.RECT(width = 13.5.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 18",
            Offset(198.96289f, 330.94922f),
            MarkerShapeClass.RECT(width = 15.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 15",
            Offset(266.99023f, 330.94922f),
            MarkerShapeClass.RECT(width = 19.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 13",
            Offset(328.98633f, 283.92773f),
            MarkerShapeClass.RECT(width = 22.5.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 13",
            Offset(317.30722f, 283.97754f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 22.5.dp)
        ),MapMarker(
            "Route 11",
            Offset(314.13864f, 236.69571f),
            MarkerShapeClass.RECT(width = 28.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 19",
            Offset(246.96631f, 351.1364f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 15.dp)
        ),MapMarker(
            "Route 20",
            Offset(194.94727f, 377.77972f),
            MarkerShapeClass.RECT(width = 19.7.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 20",
            Offset(124.9707f, 377.9707f),
            MarkerShapeClass.RECT(width = 16.5.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Route 21",
            Offset(104.96387f, 303.92285f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 25.dp)
        ),MapMarker(
            "Route 23",
            Offset(57.624077f, 91.92259f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 7.4.dp)
        ),MapMarker(
            "Route 23",
            Offset(57.964844f, 137.95605f),
            MarkerShapeClass.RECT(width = ROW_WIDTH, height = 33.dp)
        ),MapMarker(
            "Route 22",
            Offset(69.91205f, 213.14185f),
            MarkerShapeClass.RECT(width = 10.dp, height = ROW_WIDTH)
        ),MapMarker(
            "Cerulean Cave",
            Offset(259.78152f, 22.73861f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Pokémon Mansion",
            Offset(81.24262f, 346.4355f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Pokémon Tower",
            Offset(381.3317f, 107.31127f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Silph Co.",
            Offset(279.45065f, 108.17154f),
            MarkerShapeClass.MARKER
        )
    )

    Map(
        locationMarkers = locationMarkers,
        pokemonEncounterLocations = pokemonEncounterLocations,
        devMode = devMode,
        focusedLocations = focusedLocations,
        imageOriginalSize = IntSize(509, 440),
        image = R.drawable.kanto_lgpe
    )
}



