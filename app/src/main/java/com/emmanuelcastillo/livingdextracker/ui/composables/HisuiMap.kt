package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun HisuiMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>
) {

    val MARKER_SIZE = 16.dp
    // Fixed map markers of routes, cities, etc.
    val hisuiLocationMarkers = listOf(
        MapMarker(
            "Jubilife Village",
            Offset(316.75943f, 342.6739f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Obsidian Fieldlands",
            Offset(423.77463f, 448.73718f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Coronet Highlands",
            Offset(519.96094f, 284.98535f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Crimson Mirelands",
            Offset(734.7662f, 392.77347f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Cobalt Coastlands",
            Offset(885.4229f, 261.37247f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Alabaster Icelands",
            Offset(414.4746f, 89.37649f),
            MarkerShapeClass.MARKER
        ),
    )

    val alabasterLocationMarkers = listOf(
        MapMarker(
            "Lake Acuity",
            Offset(486.5131f, 142.32825f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "346.15112f, 268.56357f",
            Offset(287.54865f, 237.00201f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Snowfall Hot Spring",
            Offset(265.78766f, 411.21298f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Icepeak Arena",
            Offset(223.10834f, 493.15836f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Arena's Approach",
            Offset(299.22522f, 628.26624f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Avalanche Slopes",
            Offset(142.5031f, 790.8304f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Icebound Falls",
            Offset(262.90924f, 912.7888f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Whiteout Valley",
            Offset(460.18176f, 842.70166f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Bonechill Wastes",
            Offset(455.24588f, 689.3354f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Avalugg's Legacy",
            Offset(506.35718f, 504.96283f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Heart's Crag",
            Offset(823.3185f, 406.74384f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Pearl Settlement",
            Offset(674.58856f, 283.89584f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Snowpoint Temple",
            Offset(693.7792f, 74.81484f),
            MarkerShapeClass.MARKER
        ),
    )
    val cobaltLocationMarkers = listOf<MapMarker>(
        MapMarker("Islepy Shore", Offset(318.19342f, 194.16829f), MarkerShapeClass.MARKER),
        MapMarker("Spring Path", Offset(186.47058f, 300.46213f), MarkerShapeClass.MARKER),
        MapMarker("Windbreak Stand", Offset(239.2553f, 404.26147f), MarkerShapeClass.MARKER),
        MapMarker("Seagrass Haven", Offset(722.98254f, 321.2184f), MarkerShapeClass.MARKER),
        MapMarker("Firespit Island", Offset(874.92267f, 239.01193f), MarkerShapeClass.MARKER),
        MapMarker("Molten Arena", Offset(836.5566f, 134.09161f), MarkerShapeClass.MARKER),
        MapMarker("Veilstone Cape", Offset(612.82007f, 398.33853f), MarkerShapeClass.MARKER),
        MapMarker("Lunker's Lair", Offset(817.9778f, 424.9185f), MarkerShapeClass.MARKER),
        MapMarker("Castaway Shore", Offset(480.48618f, 450.8298f), MarkerShapeClass.MARKER),
        MapMarker("Tranquility Cove", Offset(503.4947f, 633.11584f), MarkerShapeClass.MARKER),
        MapMarker("Sand's Reach", Offset(824.97f, 753.3847f), MarkerShapeClass.MARKER),
        MapMarker("Tombolo Walk", Offset(917.47174f, 895.5275f), MarkerShapeClass.MARKER),
        MapMarker("Deadwood Haunt", Offset(668.0837f, 856.59143f), MarkerShapeClass.MARKER),
        MapMarker("Bather's Lagoon", Offset(532.586f, 853.0704f), MarkerShapeClass.MARKER),
        MapMarker("Hideaway Bay", Offset(484.99548f, 939.6826f), MarkerShapeClass.MARKER),
        MapMarker("Aipom Hill", Offset(275.82208f, 831.8777f), MarkerShapeClass.MARKER),
        MapMarker("Crossing Slope", Offset(159.10779f, 776.8442f), MarkerShapeClass.MARKER),
        MapMarker("Ginko Landing", Offset(249.73744f, 730.2206f), MarkerShapeClass.MARKER),
        MapMarker("Turnback Cave", Offset(182.49088f, 254.28665f), MarkerShapeClass.MARKER),
        MapMarker("Lava Dome Sanctum", Offset(861.12146f, 171.0555f), MarkerShapeClass.MARKER),
        MapMarker("Seaside Hollow", Offset(530.9697f, 336.44727f), MarkerShapeClass.MARKER),
    )
    val coronetLocationMarkers = listOf<MapMarker>(
        MapMarker("Temple of Sinnoh", Offset(215.15031f, 94.84209f), MarkerShapeClass.MARKER),
        MapMarker("Cloudcap Pass", Offset(267.9696f, 233.30804f), MarkerShapeClass.MARKER),
        MapMarker("Moonview Arena", Offset(112.70873f, 439.42062f), MarkerShapeClass.MARKER),
        MapMarker("Sacred Plaza", Offset(235.76172f, 504.27628f), MarkerShapeClass.MARKER),
        MapMarker("Stonetooth Rows", Offset(106.92888f, 609.8291f), MarkerShapeClass.MARKER),
        MapMarker("Bolderoll Ravine", Offset(100.57927f, 719.04065f), MarkerShapeClass.MARKER),
        MapMarker("Fabled Spring", Offset(157.79861f, 890.69666f), MarkerShapeClass.MARKER),
        MapMarker("Celestica Ruins", Offset(580.8302f, 470.82028f), MarkerShapeClass.MARKER),
        MapMarker("Clamberclaw Cliffs", Offset(740.6295f, 546.8132f), MarkerShapeClass.MARKER),
        MapMarker("Primeval Grotto", Offset(403.5416f, 641.47955f), MarkerShapeClass.MARKER),
        MapMarker("Celestica Trail", Offset(404.38538f, 701.39655f), MarkerShapeClass.MARKER),
        MapMarker("Lonely Spring", Offset(871.13007f, 646.48987f), MarkerShapeClass.MARKER),
        MapMarker("Sonorous Path", Offset(623.62354f, 778.3983f), MarkerShapeClass.MARKER),
        MapMarker("Ancient Quarry", Offset(520.6682f, 849.69745f), MarkerShapeClass.MARKER),
        MapMarker("Wayward Wood", Offset(522.33813f, 949.5495f), MarkerShapeClass.MARKER),
        MapMarker("Heavenward Lookout", Offset(879.04443f, 907.52057f), MarkerShapeClass.MARKER),
    )
    val crimsonLocationMarkers = listOf<MapMarker>(
        MapMarker("Brava Arena", Offset(377.53732f, 122.507034f), MarkerShapeClass.MARKER),
        MapMarker("Shrouded Ruins", Offset(508.43774f, 155.14207f), MarkerShapeClass.MARKER),
        MapMarker("Cloudpool Ridge", Offset(349.74097f, 210.31825f  ), MarkerShapeClass.MARKER),
        MapMarker("Diamond Heath", Offset(443.15067f, 270.83728f), MarkerShapeClass.MARKER),
        MapMarker("Diamond Settlement", Offset(466.71765f, 314.9055f), MarkerShapeClass.MARKER),
        MapMarker("Solaceon Ruins", Offset(385.62653f, 416.98312f), MarkerShapeClass.MARKER),
        MapMarker("Lake Valor", Offset(747.60364f, 308.57947f), MarkerShapeClass.MARKER),
        MapMarker("Golden Lowlands", Offset(262.02136f, 499.5373f), MarkerShapeClass.MARKER),
        MapMarker("Gapejaw Bog", Offset(307.96875f, 654.93164f), MarkerShapeClass.MARKER),
        MapMarker("Scarlet Bog", Offset(502.81647f, 572.68164f), MarkerShapeClass.MARKER),
        MapMarker("Bolderoll Slope", Offset(608.9502f, 500.97656f), MarkerShapeClass.MARKER),
        MapMarker("Cottonsedge Prairie", Offset(809.7781f, 619.24316f), MarkerShapeClass.MARKER),
        MapMarker("Droning Meadow", Offset(812.2459f, 694.6926f), MarkerShapeClass.MARKER),
        MapMarker("Sludge Mound", Offset(487.23035f, 754.6001f), MarkerShapeClass.MARKER),
        MapMarker("Ursa's Ring", Offset(582.97577f, 828.58905f), MarkerShapeClass.MARKER),
        MapMarker("Holm Trials", Offset(413.96484f, 899.92676f), MarkerShapeClass.MARKER),
    )
    val obsidianLocationMarkers = listOf<MapMarker>(
        MapMarker("Lake Verity", Offset(145.30676f, 470.53558f), MarkerShapeClass.MARKER),
        MapMarker("Floaro Gardens", Offset(155.73346f, 168.57945f), MarkerShapeClass.MARKER),
        MapMarker("Sandgem Flats", Offset(229.79254f, 737.5892f), MarkerShapeClass.MARKER),
        MapMarker("Ramanas Island", Offset(336.1318f, 859.0144f), MarkerShapeClass.MARKER),
        MapMarker("Aspiration Hill", Offset(349.1985f, 330.0523f), MarkerShapeClass.MARKER),
        MapMarker("Horseshoe Plains", Offset(622.2894f, 194.01595f), MarkerShapeClass.MARKER),
        MapMarker("Grueling Grove", Offset(848.5795f, 158.9617f), MarkerShapeClass.MARKER),
        MapMarker("Worn Bridge", Offset(794.57623f, 319.03745f), MarkerShapeClass.MARKER),
        MapMarker("Deertrack Path", Offset(491.2887f, 363.53204f), MarkerShapeClass.MARKER),
        MapMarker("Deertrack Heights", Offset(584.79346f, 451.45938f), MarkerShapeClass.MARKER),
        MapMarker("Obsidian Falls", Offset(850.2393f, 476.09375f), MarkerShapeClass.MARKER),
        MapMarker("Oreburrow Tunnel", Offset(947.3846f, 552.28467f), MarkerShapeClass.MARKER),
        MapMarker("Windswept Run", Offset(462.36514f, 584.93915f), MarkerShapeClass.MARKER),
        MapMarker("Nature's Pantry", Offset(638.9183f, 634.3315f), MarkerShapeClass.MARKER),
        MapMarker("Tidewater Dam", Offset(609.9432f, 779.5169f), MarkerShapeClass.MARKER),
        MapMarker("The Heartwood", Offset(738.69714f, 839.26215f), MarkerShapeClass.MARKER),
        MapMarker("Grandtree Arena", Offset(929.7048f, 864.7549f), MarkerShapeClass.MARKER),
    )

    val maps = listOf(
        SingleMap(
            IntSize(1024, 1024),
            R.drawable.obsidian_fieldlands_map,
            "Obsidian Fieldlands",
            obsidianLocationMarkers
        ),SingleMap(
            IntSize(1024, 1024),
            R.drawable.crimson_mirelands_map,
            "Crimson Mirelands",
            crimsonLocationMarkers
        ),SingleMap(
            IntSize(1024, 1024),
            R.drawable.cobalt_coastlands_map,
            "Cobalt Coastlands",
            cobaltLocationMarkers
        ),SingleMap(
            IntSize(1024, 1024),
            R.drawable.coronet_highlands_map,
            "Coronet Highlands",
            coronetLocationMarkers
        ),SingleMap(
            IntSize(1024, 1024),
            R.drawable.alabaster_icelands_map,
            "Alabaster Icelands",
            alabasterLocationMarkers
        ),
//        SingleMap(IntSize(1280, 720), R.drawable.hisui, "Hisui", hisuiLocationMarkers),
    )

    MultiMap(
        pokemonEncounterLocations = pokemonEncounterLocations,
        devMode = devMode,
        focusedLocations = focusedLocations,
        maps = maps,
    )
}



