package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.emmanuelcastillo.livingdextracker.R

@Composable
fun PaldeaMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>
) {

    // height set to 1100
    val imageOriginalSize = IntSize(4506, 1100)
    val image = R.drawable.paldea

    val vertices = mapOf(
        "P1" to Vertex("P1", Offset(101.975586f, 458.93555f)),
        "P2" to Vertex("P2", Offset(111.99512f, 402.97852f)),
        "P3" to Vertex("P3", Offset(93.96582f, 360.9375f)),
        "P4" to Vertex("P4", Offset(110.97266f, 327.97852f)),
        "P5" to Vertex("P5", Offset(94.98828f, 249.97559f)),
        "P6" to Vertex("P6", Offset(109.984375f, 221.92383f)),
        "P7" to Vertex("P7", Offset(168.98047f, 199.95117f)),
        "P8" to Vertex("P8", Offset(184.96582f, 173.9502f)),
        "P9" to Vertex("P9", Offset(245.97266f, 143.99414f)),
        "P10" to Vertex("P10", Offset(288.98438f, 159.96094f)),
        "P11" to Vertex("P11", Offset(316.9668f, 164.9414f)),
        "P12" to Vertex("P12", Offset(336.97266f, 179.95605f)),
        "P13" to Vertex("P13", Offset(341.98242f, 204.93164f)),
        "P14" to Vertex("P14", Offset(338.9834f, 225.95215f)),
        "P15" to Vertex("P15", Offset(374.9746f, 263.96484f)),
        "P16" to Vertex("P16", Offset(417.98633f, 295.97168f)),
        "P17" to Vertex("P17", Offset(422.9629f, 293.9209f)),
        "P18" to Vertex("P18", Offset(449.95605f, 301.97754f)),
        "P19" to Vertex("P19", Offset(454.96582f, 319.92188f)),
        "P20" to Vertex("P20", Offset(447.97852f, 324.9756f)),
        "P21" to Vertex("P21", Offset(446.95703f, 329.95605f)),
        "P22" to Vertex("P22", Offset(452.95508f, 332.95898f)),
        "P23" to Vertex("P23", Offset(456.97656f, 342.91992f)),
        "P24" to Vertex("P24", Offset(454.96582f, 346.94824f)),
        "P25" to Vertex("P25", Offset(462.9746f, 357.93457f)),
        "P26" to Vertex("P26", Offset(455.95508f, 374.92676f)),
        "P27" to Vertex("P27", Offset(439.96973f, 375.95215f)),
        "P28" to Vertex("P28", Offset(448.96777f, 412.93945f)),
        "P29" to Vertex("P29", Offset(419.96387f, 420.92285f)),
        "P30" to Vertex("P30", Offset(377.97363f, 423.92578f)),
        "P31" to Vertex("P31", Offset(364.98828f, 426.9287f)),
        "P32" to Vertex("P32", Offset(345.9707f, 435.9375f)),
        "P33" to Vertex("P33", Offset(327.9746f, 451.97754f)),
        "P34" to Vertex("P34", Offset(309.9795f, 459.96094f)),
        "P35" to Vertex("P35", Offset(286.97363f, 463.91602f)),
        "P36" to Vertex("P36", Offset(252.95996f, 457.9834f)),
        "P37" to Vertex("P37", Offset(234.96484f, 455.93262f)),
        "P38" to Vertex("P38", Offset(187.96484f, 456.958f)),
        "P39" to Vertex("P39", Offset(151.97363f, 451.97754f)),
        "P40" to Vertex("P40", Offset(127.97949f, 453.95508f)),
        "P41" to Vertex("P41", Offset(384.96094f, 154.98047f)),
        "P42" to Vertex("P42", Offset(391.98145f, 192.99316f)),
        "P43" to Vertex("P43", Offset(403.97852f, 178.93066f)),
        "P44" to Vertex("P44", Offset(415.9756f, 175.92773f)),
        "P45" to Vertex("P45", Offset(431.96094f, 175.92773f)),
        "P46" to Vertex("P46", Offset(441.98047f, 178.93066f)),
        "P47" to Vertex("P47", Offset(450.97754f, 188.96484f)),
        "P48" to Vertex("P48", Offset(453.97754f, 198.92578f)),
        "P49" to Vertex("P49", Offset(464.98535f, 208.95996f)),
        "P50" to Vertex("P50", Offset(463.96387f, 219.94629f)),
        "P51" to Vertex("P51", Offset(456.97656f, 222.94922f)),
        "P52" to Vertex("P52", Offset(466.9629f, 230.93262f)),
        "P53" to Vertex("P53", Offset(470.98438f, 245.94727f)),
        "P54" to Vertex("P54", Offset(484.95898f, 243.96973f)),
        "P55" to Vertex("P55", Offset(498.96582f, 231.95801f)),
        "P56" to Vertex("P56", Offset(509.9746f, 212.98828f)),
        "P57" to Vertex("P57", Offset(511.95215f, 190.94238f)),
        "P58" to Vertex("P58", Offset(519.96094f, 183.98438f)),
        "P59" to Vertex("P59", Offset(540.95605f, 178.93066f)),
        "P60" to Vertex("P60", Offset(559.97363f, 175.92773f)),
        "P61" to Vertex("P61", Offset(548.96484f, 143.99414f)),
        "P62" to Vertex("P62", Offset(568.9707f, 141.94336f)),
        "P63" to Vertex("P63", Offset(581.95703f, 127.9541f)),
        "P64" to Vertex("P64", Offset(602.95215f, 125.97656f)),
        "P65" to Vertex("P65", Offset(614.9492f, 140.99121f)),
        "P66" to Vertex("P66", Offset(638.9756f, 132.93457f)),
        "P67" to Vertex("P67", Offset(645.9629f, 151.97754f)),
        "P68" to Vertex("P68", Offset(664.94727f, 156.95801f)),
        "P69" to Vertex("P69", Offset(676.97754f, 151.97754f)),
        "P70" to Vertex("P70", Offset(677.9668f, 169.92188f)),
        "P71" to Vertex("P71", Offset(671.9678f, 190.94238f)),
        "P72" to Vertex("P72", Offset(660.95996f, 204.93164f)),
        "P73" to Vertex("P73", Offset(654.96094f, 226.97754f)),
        "P74" to Vertex("P74", Offset(683.96484f, 243.96973f)),
        "P75" to Vertex("P75", Offset(716.95703f, 271.94824f)),
        "P76" to Vertex("P76", Offset(745.96094f, 302.9297f)),
        "P77" to Vertex("P77", Offset(723.97754f, 315.9668f)),
        "P78" to Vertex("P78", Offset(726.97656f, 331.9336f)),
        "P79" to Vertex("P79", Offset(681.9541f, 336.9873f)),
        "P80" to Vertex("P80", Offset(660.95996f, 348.92578f)),
        "P81" to Vertex("P81", Offset(660.95996f, 364.96582f)),
        "P82" to Vertex("P82", Offset(617.98145f, 368.9209f)),
        "P83" to Vertex("P83", Offset(610.96094f, 385.98633f)),
        "P84" to Vertex("P84", Offset(580.9678f, 386.93848f)),
        "P85" to Vertex("P85", Offset(576.98047f, 401.95312f)),
        "P86" to Vertex("P86", Offset(552.9531f, 406.9336f)),
        "P87" to Vertex("P87", Offset(507.96387f, 408.98438f)),
        "P88" to Vertex("P88", Offset(511.95215f, 391.91895f)),
        "P89" to Vertex("P89", Offset(522.95996f, 376.97754f)),
        "P90" to Vertex("P90", Offset(526.98145f, 352.9541f)),
        "P91" to Vertex("P91", Offset(507.96387f, 328.93066f)),
        "P92" to Vertex("P92", Offset(486.96875f, 330.98145f)),
        "P93" to Vertex("P93", Offset(475.96094f, 332.95898f)),
        "P94" to Vertex("P94", Offset(469.9619f, 344.9707f)),
        "P95" to Vertex("P95", Offset(461.95312f, 338.96484f)),
        "P96" to Vertex("P96", Offset(411.9873f, 133.95996f)),
        "P97" to Vertex("P97", Offset(452.95508f, 116.96777f)),
        "P98" to Vertex("P98", Offset(492.96777f, 116.96777f)),
        "P99" to Vertex("P99", Offset(526.98145f, 128.97949f)),
        "P100" to Vertex("P100", Offset(101.975586f, 481.9336f)),
        "P101" to Vertex("P101", Offset(71.98242f, 504.93164f)),
        "P102" to Vertex("P102", Offset(64.99512f, 523.9746f)),
        "P103" to Vertex("P103", Offset(96.96582f, 571.94824f)),
        "P104" to Vertex("P104", Offset(159.9834f, 547.9248f)),
        "P105" to Vertex("P105", Offset(197.98438f, 532.9834f)),
        "P106" to Vertex("P106", Offset(216.96875f, 513.9404f)),
        "P107" to Vertex("P107", Offset(228.96582f, 514.9658f)),
        "P108" to Vertex("P108", Offset(244.98438f, 508.95996f)),
        "P109" to Vertex("P109", Offset(256.98145f, 513.9404f)),
        "P110" to Vertex("P110", Offset(268.97852f, 510.9375f)),
        "P111" to Vertex("P111", Offset(301.9707f, 521.9238f)),
        "P112" to Vertex("P112", Offset(316.9668f, 540.9668f)),
        "P113" to Vertex("P113", Offset(321.97656f, 546.97266f)),
        "P114" to Vertex("P114", Offset(330.9746f, 528.9551f)),
        "P115" to Vertex("P115", Offset(329.98535f, 512.91504f)),
        "P116" to Vertex("P116", Offset(316.9668f, 484.93652f)),
        "P117" to Vertex("P117", Offset(315.97754f, 471.97266f)),
        "P118" to Vertex("P118", Offset(331.9629f, 550.92773f)),
        "P119" to Vertex("P119", Offset(335.98438f, 559.9365f)),
        "P120" to Vertex("P120", Offset(340.96094f, 554.95605f)),
        "P121" to Vertex("P121", Offset(339.97168f, 548.9502f)),
        "P122" to Vertex("P122", Offset(341.98242f, 541.91895f)),
        "P123" to Vertex("P123", Offset(341.98242f, 526.97754f)),
        "P124" to Vertex("P124", Offset(346.95898f, 519.9463f)),
        "P125" to Vertex("P125", Offset(355.95703f, 527.9297f)),
        "P126" to Vertex("P126", Offset(358.95605f, 539.9414f)),
        "P127" to Vertex("P127", Offset(357.96777f, 566.9678f)),
        "P128" to Vertex("P128", Offset(361.98828f, 575.97656f)),
        "P129" to Vertex("P129", Offset(367.9873f, 582.9346f)),
        "P130" to Vertex("P130", Offset(386.97168f, 593.9209f)),
        "P131" to Vertex("P131", Offset(392.96973f, 601.97754f)),
        "P132" to Vertex("P132", Offset(390.95996f, 607.91016f)),
        "P133" to Vertex("P133", Offset(401.96777f, 617.94434f)),
        "P134" to Vertex("P134", Offset(398.96875f, 626.9531f)),
        "P135" to Vertex("P135", Offset(420.98535f, 621.97266f)),
        "P136" to Vertex("P136", Offset(431.96094f, 614.9414f)),
        "P137" to Vertex("P137", Offset(441.98047f, 589.9658f)),
        "P138" to Vertex("P138", Offset(447.97852f, 562.93945f)),
        "P139" to Vertex("P139", Offset(455.95508f, 543.9697f)),
        "P140" to Vertex("P140", Offset(460.96484f, 539.9414f)),
        "P141" to Vertex("P141", Offset(470.98438f, 539.9414f)),
        "P142" to Vertex("P142", Offset(467.98438f, 523.9746f)),
        "P143" to Vertex("P143", Offset(470.98438f, 511.9629f)),
        "P144" to Vertex("P144", Offset(487.958f, 502.9541f)),
        "P145" to Vertex("P145", Offset(499.95508f, 499.95117f)),
        "P146" to Vertex("P146", Offset(514.9844f, 485.9619f)),
        "P147" to Vertex("P147", Offset(514.9844f, 479.95605f)),
        "P148" to Vertex("P148", Offset(502.9541f, 468.96973f)),
        "P149" to Vertex("P149", Offset(499.95508f, 444.9463f)),
        "P150" to Vertex("P150", Offset(501.96582f, 411.9873f)),
        "P151" to Vertex("P151", Offset(86.978516f, 588.9404f)),
        "P152" to Vertex("P152", Offset(75.9707f, 612.96387f)),
        "P153" to Vertex("P153", Offset(68.9834f, 628.93066f)),
        "P154" to Vertex("P154", Offset(94.98828f, 624.9756f)),
        "P155" to Vertex("P155", Offset(119.9707f, 632.959f)),
        "P156" to Vertex("P156", Offset(136.97754f, 628.93066f)),
        "P157" to Vertex("P157", Offset(135.98926f, 641.9678f)),
        "P158" to Vertex("P158", Offset(149.96387f, 641.9678f)),
        "P159" to Vertex("P159", Offset(163.9707f, 652.9541f)),
        "P160" to Vertex("P160", Offset(182.98828f, 647.97363f)),
        "P161" to Vertex("P161", Offset(206.98242f, 648.9258f)),
        "P162" to Vertex("P162", Offset(234.96484f, 634.9365f)),
        "P163" to Vertex("P163", Offset(245.97266f, 619.9219f)),
        "P164" to Vertex("P164", Offset(270.98926f, 615.9668f)),
        "P165" to Vertex("P165", Offset(283.9746f, 594.9463f)),
        "P166" to Vertex("P166", Offset(294.9834f, 585.9375f)),
        "P167" to Vertex("P167", Offset(302.95898f, 589.9658f)),
        "P168" to Vertex("P168", Offset(306.98047f, 597.9492f)),
        "P169" to Vertex("P169", Offset(315.97754f, 604.98047f)),
        "P170" to Vertex("P170", Offset(328.96387f, 608.93555f)),
        "P171" to Vertex("P171", Offset(341.98242f, 604.98047f)),
        "P172" to Vertex("P172", Offset(348.96973f, 594.9463f)),
        "P173" to Vertex("P173", Offset(345.9707f, 577.9541f)),
        "P174" to Vertex("P174", Offset(341.98242f, 568.9453f)),
        "P175" to Vertex("P175", Offset(387.95996f, 641.9678f)),
        "P176" to Vertex("P176", Offset(370.98633f, 667.96875f)),
        "P177" to Vertex("P177", Offset(373.98535f, 683.93555f)),
        "P178" to Vertex("P178", Offset(361.98828f, 695.94727f)),
        "P179" to Vertex("P179", Offset(327.9746f, 705.9082f)),
        "P180" to Vertex("P180", Offset(319.96582f, 716.9678f)),
        "P181" to Vertex("P181", Offset(315.97754f, 731.9092f)),
        "P182" to Vertex("P182", Offset(316.9668f, 742.96875f)),
        "P183" to Vertex("P183", Offset(325.96484f, 740.91797f)),
        "P184" to Vertex("P184", Offset(350.98047f, 748.9746f)),
        "P185" to Vertex("P185", Offset(344.98145f, 760.9131f)),
        "P186" to Vertex("P186", Offset(333.97363f, 767.94434f)),
        "P187" to Vertex("P187", Offset(312.97852f, 789.917f)),
        "P188" to Vertex("P188", Offset(300.98145f, 785.9619f)),
        "P189" to Vertex("P189", Offset(287.9629f, 789.917f)),
        "P190" to Vertex("P190", Offset(280.9756f, 806.9092f)),
        "P191" to Vertex("P191", Offset(269.9668f, 819.9463f)),
        "P192" to Vertex("P192", Offset(258.9922f, 831.958f)),
        "P193" to Vertex("P193", Offset(245.97266f, 844.9219f)),
        "P194" to Vertex("P194", Offset(239.97461f, 861.91406f)),
        "P195" to Vertex("P195", Offset(235.98633f, 879.93164f)),
        "P196" to Vertex("P196", Offset(230.97656f, 891.94336f)),
        "P197" to Vertex("P197", Offset(217.99121f, 898.90137f)),
        "P198" to Vertex("P198", Offset(216.96875f, 893.9209f)),
        "P199" to Vertex("P199", Offset(200.98438f, 893.9209f)),
        "P200" to Vertex("P200", Offset(171.98047f, 881.9092f)),
        "P201" to Vertex("P201", Offset(156.9834f, 887.91504f)),
        "P202" to Vertex("P202", Offset(133.97852f, 889.9658f)),
        "P203" to Vertex("P203", Offset(138.98828f, 869.9707f)),
        "P204" to Vertex("P204", Offset(149.96387f, 858.91113f)),
        "P205" to Vertex("P205", Offset(126.99121f, 839.9414f)),
        "P206" to Vertex("P206", Offset(86.978516f, 804.93164f)),
        "P207" to Vertex("P207", Offset(49.96582f, 763.916f)),
        "P208" to Vertex("P208", Offset(38.990234f, 714.917f)),
        "P209" to Vertex("P209", Offset(44.989258f, 682.91016f)),
        "P210" to Vertex("P210", Offset(110.97266f, 901.9043f)),
        "P211" to Vertex("P211", Offset(113.97266f, 955.95703f)),
        "P212" to Vertex("P212", Offset(111.99512f, 989.9414f)),
        "P213" to Vertex("P213", Offset(132.98926f, 1023.9258f)),
        "P214" to Vertex("P214", Offset(176.99023f, 1036.9629f)),
        "P215" to Vertex("P215", Offset(220.99023f, 1038.9404f)),
        "P216" to Vertex("P216", Offset(257.96973f, 1031.9092f)),
        "P217" to Vertex("P217", Offset(274.97656f, 1019.89746f)),
        "P218" to Vertex("P218", Offset(275.96582f, 997.9248f)),
        "P219" to Vertex("P219", Offset(275.96582f, 994.9219f)),
        "P220" to Vertex("P220", Offset(279.98633f, 989.9414f)),
        "P221" to Vertex("P221", Offset(288.98438f, 971.9238f)),
        "P222" to Vertex("P222", Offset(277.97656f, 964.9658f)),
        "P223" to Vertex("P223", Offset(276.9873f, 957.9346f)),
        "P224" to Vertex("P224", Offset(275.96582f, 951.9287f)),
        "P225" to Vertex("P225", Offset(283.9746f, 944.9707f)),
        "P226" to Vertex("P226", Offset(281.96387f, 936.91406f)),
        "P227" to Vertex("P227", Offset(284.96387f, 930.9082f)),
        "P228" to Vertex("P228", Offset(282.98633f, 925.92773f)),
        "P229" to Vertex("P229", Offset(276.9873f, 922.9248f)),
        "P230" to Vertex("P230", Offset(272.9668f, 922.9248f)),
        "P231" to Vertex("P231", Offset(265.9795f, 918.9697f)),
        "P232" to Vertex("P232", Offset(256.98145f, 916.91895f)),
        "P233" to Vertex("P233", Offset(245.97266f, 918.9697f)),
        "P234" to Vertex("P234", Offset(231.96582f, 917.94434f)),
        "P235" to Vertex("P235", Offset(261.9912f, 860.9619f)),
        "P236" to Vertex("P236", Offset(281.96387f, 870.92285f)),
        "P237" to Vertex("P237", Offset(296.96094f, 883.95996f)),
        "P238" to Vertex("P238", Offset(306.98047f, 901.9043f)),
        "P239" to Vertex("P239", Offset(333.97363f, 912.96387f)),
        "P240" to Vertex("P240", Offset(364.98828f, 918.9697f)),
        "P241" to Vertex("P241", Offset(389.9707f, 916.91895f)),
        "P242" to Vertex("P242", Offset(435.98145f, 905.9326f)),
        "P243" to Vertex("P243", Offset(442.96875f, 881.9092f)),
        "P244" to Vertex("P244", Offset(447.97852f, 863.96484f)),
        "P245" to Vertex("P245", Offset(466.9629f, 849.90234f)),
        "P246" to Vertex("P246", Offset(466.9629f, 839.9414f)),
        "P247" to Vertex("P247", Offset(473.9834f, 828.9551f)),
        "P248" to Vertex("P248", Offset(481.95898f, 828.9551f)),
        "P249" to Vertex("P249", Offset(480.9707f, 813.9404f)),
        "P250" to Vertex("P250", Offset(478.95996f, 799.9512f)),
        "P251" to Vertex("P251", Offset(470.98438f, 788.96484f)),
        "P252" to Vertex("P252", Offset(445.96777f, 780.9082f)),
        "P253" to Vertex("P253", Offset(423.98438f, 758.93555f)),
        "P254" to Vertex("P254", Offset(406.97754f, 749.92676f)),
        "P255" to Vertex("P255", Offset(399.95703f, 731.9092f)),
        "P256" to Vertex("P256", Offset(400.9795f, 726.9287f)),
        "P257" to Vertex("P257", Offset(378.9629f, 716.9678f)),
        "P258" to Vertex("P258", Offset(367.9873f, 714.917f)),
        "P259" to Vertex("P259", Offset(366.96582f, 708.91113f)),
        "P260" to Vertex("P260", Offset(361.98828f, 706.9336f)),
        "P261" to Vertex("P261", Offset(301.9707f, 1023.9258f)),
        "P262" to Vertex("P262", Offset(332.98438f, 1024.9512f)),
        "P263" to Vertex("P263", Offset(361.98828f, 1023.9258f)),
        "P264" to Vertex("P264", Offset(385.98242f, 1023.9258f)),
        "P265" to Vertex("P265", Offset(405.95605f, 1036.9629f)),
        "P266" to Vertex("P266", Offset(430.97168f, 1041.9434f)),
        "P267" to Vertex("P267", Offset(456.97656f, 1040.918f)),
        "P268" to Vertex("P268", Offset(476.98242f, 1031.9092f)),
        "P269" to Vertex("P269", Offset(481.95898f, 1015.9424f)),
        "P270" to Vertex("P270", Offset(492.96777f, 1023.9258f)),
        "P271" to Vertex("P271", Offset(507.96387f, 1019.89746f)),
        "P272" to Vertex("P272", Offset(517.9834f, 1007.959f)),
        "P273" to Vertex("P273", Offset(516.9619f, 1001.9531f)),
        "P274" to Vertex("P274", Offset(524.9707f, 994.9219f)),
        "P275" to Vertex("P275", Offset(524.9707f, 989.9414f)),
        "P276" to Vertex("P276", Offset(511.95215f, 976.9043f)),
        "P277" to Vertex("P277", Offset(494.97852f, 972.9492f)),
        "P278" to Vertex("P278", Offset(475.96094f, 967.96875f)),
        "P279" to Vertex("P279", Offset(465.9746f, 952.9541f)),
        "P280" to Vertex("P280", Offset(466.9629f, 936.91406f)),
        "P281" to Vertex("P281", Offset(473.9834f, 924.90234f)),
        "P282" to Vertex("P282", Offset(478.95996f, 902.9297f)),
        "P283" to Vertex("P283", Offset(478.95996f, 888.9404f)),
        "P284" to Vertex("P284", Offset(472.9619f, 877.9541f)),
        "P285" to Vertex("P285", Offset(460.96484f, 853.93066f)),
        "P286" to Vertex("P286", Offset(404.9668f, 721.94824f)),
        "P287" to Vertex("P287", Offset(420.98535f, 723.9258f)),
        "P288" to Vertex("P288", Offset(430.97168f, 736.9629f)),
        "P289" to Vertex("P289", Offset(432.98242f, 750.95215f)),
        "P290" to Vertex("P290", Offset(453.97754f, 771.97266f)),
        "P291" to Vertex("P291", Offset(471.97266f, 780.9082f)),
        "P292" to Vertex("P292", Offset(515.97266f, 764.9414f)),
        "P293" to Vertex("P293", Offset(529.98047f, 768.9697f)),
        "P294" to Vertex("P294", Offset(555.95215f, 785.9619f)),
        "P295" to Vertex("P295", Offset(552.9531f, 799.9512f)),
        "P296" to Vertex("P296", Offset(574.9697f, 818.9209f)),
        "P297" to Vertex("P297", Offset(591.97656f, 853.93066f)),
        "P298" to Vertex("P298", Offset(598.96387f, 856.9336f)),
        "P299" to Vertex("P299", Offset(584.95605f, 886.9629f)),
        "P300" to Vertex("P300", Offset(559.97363f, 904.9072f)),
        "P301" to Vertex("P301", Offset(556.97363f, 912.96387f)),
        "P302" to Vertex("P302", Offset(561.9512f, 928.93066f)),
        "P303" to Vertex("P303", Offset(559.97363f, 938.96484f)),
        "P304" to Vertex("P304", Offset(548.96484f, 942.9199f)),
        "P305" to Vertex("P305", Offset(540.95605f, 954.93164f)),
        "P306" to Vertex("P306", Offset(546.9541f, 962.91504f)),
        "P307" to Vertex("P307", Offset(551.96387f, 955.95703f)),
        "P308" to Vertex("P308", Offset(561.9512f, 954.93164f)),
        "P309" to Vertex("P309", Offset(584.95605f, 975.95215f)),
        "P310" to Vertex("P310", Offset(597.9746f, 976.9043f)),
        "P311" to Vertex("P311", Offset(606.97266f, 966.94336f)),
        "P312" to Vertex("P312", Offset(611.9492f, 951.9287f)),
        "P313" to Vertex("P313", Offset(628.95605f, 961.9629f)),
        "P314" to Vertex("P314", Offset(639.96484f, 965.91797f)),
        "P315" to Vertex("P315", Offset(656.9717f, 982.91016f)),
        "P316" to Vertex("P316", Offset(658.9492f, 992.94434f)),
        "P317" to Vertex("P317", Offset(672.95703f, 1006.9336f)),
        "P318" to Vertex("P318", Offset(676.97754f, 1021.94824f)),
        "P319" to Vertex("P319", Offset(681.9541f, 1038.9404f)),
        "P320" to Vertex("P320", Offset(645.9629f, 1039.9658f)),
        "P321" to Vertex("P321", Offset(599.95215f, 1031.9092f)),
        "P322" to Vertex("P322", Offset(567.9824f, 1021.94824f)),
        "P323" to Vertex("P323", Offset(565.9717f, 1039.9658f)),
        "P324" to Vertex("P324", Offset(553.9746f, 1069.9219f)),
        "P325" to Vertex("P325", Offset(518.97266f, 1073.9502f)),
        "P326" to Vertex("P326", Offset(485.98047f, 1066.919f)),
        "P327" to Vertex("P327", Offset(472.9619f, 1049.9268f)),
        "P328" to Vertex("P328", Offset(621.96875f, 866.9678f)),
        "P329" to Vertex("P329", Offset(626.9785f, 858.91113f)),
        "P330" to Vertex("P330", Offset(639.96484f, 855.9082f)),
        "P331" to Vertex("P331", Offset(647.97363f, 863.96484f)),
        "P332" to Vertex("P332", Offset(663.959f, 836.9385f)),
        "P333" to Vertex("P333", Offset(662.9697f, 816.94336f)),
        "P334" to Vertex("P334", Offset(672.95703f, 822.9492f)),
        "P335" to Vertex("P335", Offset(684.9541f, 818.9209f)),
        "P336" to Vertex("P336", Offset(687.9531f, 814.9658f)),
        "P337" to Vertex("P337", Offset(716.95703f, 815.91797f)),
        "P338" to Vertex("P338", Offset(725.9551f, 828.9551f)),
        "P339" to Vertex("P339", Offset(727.96484f, 836.9385f)),
        "P340" to Vertex("P340", Offset(737.95215f, 836.9385f)),
        "P341" to Vertex("P341", Offset(742.9619f, 843.9697f)),
        "P342" to Vertex("P342", Offset(753.9697f, 844.9219f)),
        "P343" to Vertex("P343", Offset(761.9463f, 838.916f)),
        "P344" to Vertex("P344", Offset(767.94434f, 832.91016f)),
        "P345" to Vertex("P345", Offset(777.96387f, 833.93555f)),
        "P346" to Vertex("P346", Offset(775.9531f, 848.9502f)),
        "P347" to Vertex("P347", Offset(779.9746f, 866.9678f)),
        "P348" to Vertex("P348", Offset(788.97266f, 867.9199f)),
        "P349" to Vertex("P349", Offset(839.95996f, 896.9238f)),
        "P350" to Vertex("P350", Offset(832.97266f, 934.9365f)),
        "P351" to Vertex("P351", Offset(782.97363f, 980.9326f)),
        "P352" to Vertex("P352", Offset(742.9619f, 1005.9082f)),
        "P353" to Vertex("P353", Offset(728.9541f, 1007.959f)),
        "P354" to Vertex("P354", Offset(708.94824f, 1021.94824f)),
        "P355" to Vertex("P355", Offset(703.9707f, 1031.9092f)),
        "P356" to Vertex("P356", Offset(573.98047f, 782.959f)),
        "P357" to Vertex("P357", Offset(603.97363f, 796.94824f)),
        "P358" to Vertex("P358", Offset(619.959f, 805.95703f)),
        "P359" to Vertex("P359", Offset(629.9785f, 806.9092f)),
        "P360" to Vertex("P360", Offset(637.9541f, 792.9199f)),
        "P361" to Vertex("P361", Offset(641.9756f, 766.91895f)),
        "P362" to Vertex("P362", Offset(649.9512f, 766.91895f)),
        "P363" to Vertex("P363", Offset(652.9502f, 760.9131f)),
        "P364" to Vertex("P364", Offset(659.9707f, 760.9131f)),
        "P365" to Vertex("P365", Offset(665.9697f, 766.91895f)),
        "P366" to Vertex("P366", Offset(676.97754f, 763.916f)),
        "P367" to Vertex("P367", Offset(694.97363f, 761.9385f)),
        "P368" to Vertex("P368", Offset(690.95215f, 755.9326f)),
        "P369" to Vertex("P369", Offset(698.96094f, 756.958f)),
        "P370" to Vertex("P370", Offset(702.9492f, 769.9219f)),
        "P371" to Vertex("P371", Offset(709.9697f, 767.94434f)),
        "P372" to Vertex("P372", Offset(708.94824f, 755.9326f)),
        "P373" to Vertex("P373", Offset(718.9678f, 754.9072f)),
        "P374" to Vertex("P374", Offset(720.97754f, 766.91895f)),
        "P375" to Vertex("P375", Offset(727.96484f, 762.96387f)),
        "P376" to Vertex("P376", Offset(739.9619f, 762.96387f)),
        "P377" to Vertex("P377", Offset(742.9619f, 771.97266f)),
        "P378" to Vertex("P378", Offset(751.959f, 776.9531f)),
        "P379" to Vertex("P379", Offset(752.94824f, 784.9365f)),
        "P380" to Vertex("P380", Offset(766.95605f, 783.91113f)),
        "P381" to Vertex("P381", Offset(775.9531f, 788.96484f)),
        "P382" to Vertex("P382", Offset(781.95215f, 797.97363f)),
        "P383" to Vertex("P383", Offset(791.9717f, 797.97363f)),
        "P384" to Vertex("P384", Offset(781.95215f, 805.95703f)),
        "P385" to Vertex("P385", Offset(789.96094f, 826.9043f)),
        "P386" to Vertex("P386", Offset(645.9629f, 730.95703f)),
        "P387" to Vertex("P387", Offset(641.9756f, 681.958f)),
        "P388" to Vertex("P388", Offset(645.9629f, 648.9258f)),
        "P389" to Vertex("P389", Offset(622.958f, 594.9463f)),
        "P390" to Vertex("P390", Offset(641.9756f, 594.9463f)),
        "P391" to Vertex("P391", Offset(649.9512f, 604.98047f)),
        "P392" to Vertex("P392", Offset(645.9629f, 623.9502f)),
        "P393" to Vertex("P393", Offset(651.9619f, 630.9082f)),
        "P394" to Vertex("P394", Offset(664.94727f, 623.9502f)),
        "P395" to Vertex("P395", Offset(678.9551f, 620.94727f)),
        "P396" to Vertex("P396", Offset(686.96387f, 624.9756f)),
        "P397" to Vertex("P397", Offset(725.9551f, 619.9219f)),
        "P398" to Vertex("P398", Offset(748.95996f, 674.92676f)),
        "P399" to Vertex("P399", Offset(753.9697f, 681.958f)),
        "P400" to Vertex("P400", Offset(769.9551f, 718.9453f)),
        "P401" to Vertex("P401", Offset(794.9707f, 739.9658f)),
        "P402" to Vertex("P402", Offset(821.96387f, 751.9043f)),
        "P403" to Vertex("P403", Offset(830.9619f, 748.9746f)),
        "P404" to Vertex("P404", Offset(847.96875f, 746.9238f)),
        "P405" to Vertex("P405", Offset(866.9531f, 724.9512f)),
        "P406" to Vertex("P406", Offset(886.959f, 709.9365f)),
        "P407" to Vertex("P407", Offset(912.96387f, 723.9258f)),
        "P408" to Vertex("P408", Offset(924.96094f, 710.9619f)),
        "P409" to Vertex("P409", Offset(958.9414f, 699.9756f)),
        "P410" to Vertex("P410", Offset(985.9678f, 714.917f)),
        "P411" to Vertex("P411", Offset(997.96484f, 738.9404f)),
        "P412" to Vertex("P412", Offset(991.9668f, 770.94727f)),
        "P413" to Vertex("P413", Offset(945.95605f, 809.9121f)),
        "P414" to Vertex("P414", Offset(938.96875f, 834.96094f)),
        "P415" to Vertex("P415", Offset(914.9414f, 854.95605f)),
        "P416" to Vertex("P416", Offset(879.9717f, 863.96484f)),
        "P417" to Vertex("P417", Offset(856.9668f, 866.9678f)),
        "P418" to Vertex("P418", Offset(844.9697f, 879.93164f)),
        "P419" to Vertex("P419", Offset(721.9668f, 613.916f)),
        "P420" to Vertex("P420", Offset(773.9756f, 607.91016f)),
        "P421" to Vertex("P421", Offset(789.96094f, 618.9697f)),
        "P422" to Vertex("P422", Offset(803.96875f, 627.9785f)),
        "P423" to Vertex("P423", Offset(832.97266f, 627.9785f)),
        "P424" to Vertex("P424", Offset(867.9424f, 612.96387f)),
        "P425" to Vertex("P425", Offset(869.95215f, 601.97754f)),
        "P426" to Vertex("P426", Offset(865.96484f, 592.96875f)),
        "P427" to Vertex("P427", Offset(883.95996f, 566.9678f)),
        "P428" to Vertex("P428", Offset(890.94727f, 561.91406f)),
        "P429" to Vertex("P429", Offset(894.96875f, 566.9678f)),
        "P430" to Vertex("P430", Offset(902.94434f, 564.917f)),
        "P431" to Vertex("P431", Offset(920.9404f, 570.92285f)),
        "P432" to Vertex("P432", Offset(938.96875f, 586.9629f)),
        "P433" to Vertex("P433", Offset(957.9531f, 588.9404f)),
        "P434" to Vertex("P434", Offset(961.9414f, 599.92676f)),
        "P435" to Vertex("P435", Offset(945.95605f, 612.96387f)),
        "P436" to Vertex("P436", Offset(937.94727f, 629.95605f)),
        "P437" to Vertex("P437", Offset(931.94824f, 666.94336f)),
        "P438" to Vertex("P438", Offset(925.9502f, 679.9072f)),
        "P439" to Vertex("P439", Offset(934.94727f, 691.91895f)),
        "P440" to Vertex("P440", Offset(950.9658f, 691.91895f)),
        "P441" to Vertex("P441", Offset(692.9629f, 612.96387f)),
        "P442" to Vertex("P442", Offset(673.9785f, 610.9131f)),
        "P443" to Vertex("P443", Offset(659.9707f, 597.9492f)),
        "P444" to Vertex("P444", Offset(651.9619f, 580.95703f)),
        "P445" to Vertex("P445", Offset(663.959f, 574.9512f)),
        "P446" to Vertex("P446", Offset(678.9551f, 566.9678f)),
        "P447" to Vertex("P447", Offset(676.97754f, 562.93945f)),
        "P448" to Vertex("P448", Offset(680.9658f, 542.94434f)),
        "P449" to Vertex("P449", Offset(720.97754f, 520.9717f)),
        "P450" to Vertex("P450", Offset(758.9463f, 465.9668f)),
        "P451" to Vertex("P451", Offset(778.9531f, 440.91797f)),
        "P452" to Vertex("P452", Offset(796.94824f, 445.97168f)),
        "P453" to Vertex("P453", Offset(808.9453f, 456.958f)),
        "P454" to Vertex("P454", Offset(824.96387f, 470.94727f)),
        "P455" to Vertex("P455", Offset(857.9551f, 478.93066f)),
        "P456" to Vertex("P456", Offset(880.96094f, 488.96484f)),
        "P457" to Vertex("P457", Offset(904.9551f, 504.93164f)),
        "P458" to Vertex("P458", Offset(946.94434f, 505.95703f)),
        "P459" to Vertex("P459", Offset(966.9512f, 510.9375f)),
        "P460" to Vertex("P460", Offset(983.958f, 520.9717f)),
        "P461" to Vertex("P461", Offset(1000.96484f, 532.9834f)),
        "P462" to Vertex("P462", Offset(993.94434f, 553.93066f)),
        "P463" to Vertex("P463", Offset(971.96094f, 564.917f)),
        "P464" to Vertex("P464", Offset(956.96387f, 570.92285f)),
        "P465" to Vertex("P465", Offset(954.9541f, 578.9795f)),
        "P466" to Vertex("P466", Offset(811.94434f, 438.94043f)),
        "P467" to Vertex("P467", Offset(811.94434f, 428.9795f)),
        "P468" to Vertex("P468", Offset(814.94434f, 412.93945f)),
        "P469" to Vertex("P469", Offset(823.9746f, 393.96973f)),
        "P470" to Vertex("P470", Offset(823.9746f, 379.98047f)),
        "P471" to Vertex("P471", Offset(831.9512f, 372.94922f)),
        "P472" to Vertex("P472", Offset(845.958f, 377.9297f)),
        "P473" to Vertex("P473", Offset(856.9668f, 375.95215f)),
        "P474" to Vertex("P474", Offset(865.96484f, 368.9209f)),
        "P475" to Vertex("P475", Offset(878.9502f, 362.98828f)),
        "P476" to Vertex("P476", Offset(886.959f, 365.91797f)),
        "P477" to Vertex("P477", Offset(893.9463f, 358.95996f)),
        "P478" to Vertex("P478", Offset(893.9463f, 351.9287f)),
        "P479" to Vertex("P479", Offset(897.9678f, 345.92285f)),
        "P480" to Vertex("P480", Offset(900.9668f, 330.98145f)),
        "P481" to Vertex("P481", Offset(911.9424f, 325.92773f)),
        "P482" to Vertex("P482", Offset(920.9404f, 324.9756f)),
        "P483" to Vertex("P483", Offset(906.9658f, 340.94238f)),
        "P484" to Vertex("P484", Offset(903.9658f, 360.9375f)),
        "P485" to Vertex("P485", Offset(911.9424f, 380.93262f)),
        "P486" to Vertex("P486", Offset(921.9619f, 391.91895f)),
        "P487" to Vertex("P487", Offset(944.9668f, 399.9756f)),
        "P488" to Vertex("P488", Offset(961.9414f, 398.9502f)),
        "P489" to Vertex("P489", Offset(970.9385f, 403.93066f)),
        "P490" to Vertex("P490", Offset(975.94824f, 416.96777f)),
        "P491" to Vertex("P491", Offset(961.9414f, 425.97656f)),
        "P492" to Vertex("P492", Offset(952.94336f, 446.92383f)),
        "P493" to Vertex("P493", Offset(961.9414f, 468.96973f)),
        "P494" to Vertex("P494", Offset(983.958f, 486.91406f)),
        "P495" to Vertex("P495", Offset(991.9668f, 485.9619f)),
        "P496" to Vertex("P496", Offset(998.9541f, 493.9453f)),
        "P497" to Vertex("P497", Offset(1010.9512f, 496.94824f)),
        "P498" to Vertex("P498", Offset(1016.9492f, 505.95703f)),
        "P499" to Vertex("P499", Offset(1009.9619f, 515.91797f)),
        "P500" to Vertex("P500", Offset(706.9707f, 150.95215f)),
        "P501" to Vertex("P501", Offset(730.96484f, 161.93848f)),
        "P502" to Vertex("P502", Offset(741.97266f, 175.92773f)),
        "P503" to Vertex("P503", Offset(750.9707f, 187.93945f)),
        "P504" to Vertex("P504", Offset(766.95605f, 195.92285f)),
        "P505" to Vertex("P505", Offset(774.96484f, 208.95996f)),
        "P506" to Vertex("P506", Offset(770.94336f, 226.97754f)),
        "P507" to Vertex("P507", Offset(782.97363f, 244.92188f)),
        "P508" to Vertex("P508", Offset(782.97363f, 256.9336f)),
        "P509" to Vertex("P509", Offset(789.96094f, 264.99023f)),
        "P510" to Vertex("P510", Offset(786.9619f, 268.9453f)),
        "P511" to Vertex("P511", Offset(782.97363f, 271.94824f)),
        "P512" to Vertex("P512", Offset(791.9717f, 277.9541f)),
        "P513" to Vertex("P513", Offset(803.96875f, 283.95996f)),
        "P514" to Vertex("P514", Offset(821.96387f, 277.9541f)),
        "P515" to Vertex("P515", Offset(839.95996f, 284.98535f)),
        "P516" to Vertex("P516", Offset(842.959f, 282.93457f)),
        "P517" to Vertex("P517", Offset(849.9463f, 282.93457f)),
        "P518" to Vertex("P518", Offset(863.9541f, 286.9629f)),
        "P519" to Vertex("P519", Offset(866.9531f, 284.98535f)),
        "P520" to Vertex("P520", Offset(882.9717f, 287.98828f)),
        "P521" to Vertex("P521", Offset(893.9463f, 297.94922f)),
        "P522" to Vertex("P522", Offset(907.9541f, 310.98633f)),
        "P523" to Vertex("P523", Offset(911.9424f, 312.96387f)),
        "P524" to Vertex("P524", Offset(919.9512f, 316.91895f)),
        "P525" to Vertex("P525", Offset(741.97266f, 312.96387f)),
        "P526" to Vertex("P526", Offset(757.958f, 330.98145f)),
        "P527" to Vertex("P527", Offset(757.958f, 338.96484f)),
        "P528" to Vertex("P528", Offset(744.9717f, 347.97363f)),
        "P529" to Vertex("P529", Offset(742.9619f, 363.94043f)),
        "P530" to Vertex("P530", Offset(737.95215f, 379.98047f)),
        "P531" to Vertex("P531", Offset(749.9492f, 385.98633f)),
        "P532" to Vertex("P532", Offset(749.9492f, 391.91895f)),
        "P533" to Vertex("P533", Offset(727.96484f, 411.9873f)),
        "P534" to Vertex("P534", Offset(739.9619f, 415.94238f)),
        "P535" to Vertex("P535", Offset(737.95215f, 426.9287f)),
        "P536" to Vertex("P536", Offset(745.96094f, 434.98535f)),
        "P537" to Vertex("P537", Offset(753.9697f, 433.95996f)),
        "P538" to Vertex("P538", Offset(756.96875f, 428.9795f)),
        "P539" to Vertex("P539", Offset(767.94434f, 426.9287f)),
        "P540" to Vertex("P540", Offset(773.9756f, 431.98242f)),
        "P541" to Vertex("P541", Offset(718.9678f, 413.96484f)),
        "P542" to Vertex("P542", Offset(714.9463f, 430.95703f)),
        "P543" to Vertex("P543", Offset(695.9619f, 437.91504f)),
        "P544" to Vertex("P544", Offset(674.9668f, 463.91602f)),
        "P545" to Vertex("P545", Offset(674.9668f, 470.94727f)),
        "P546" to Vertex("P546", Offset(658.9492f, 476.95312f)),
        "P547" to Vertex("P547", Offset(656.9717f, 480.98145f)),
        "P548" to Vertex("P548", Offset(641.9756f, 490.94238f)),
        "P549" to Vertex("P549", Offset(638.9756f, 497.97363f)),
        "P550" to Vertex("P550", Offset(639.96484f, 507.93457f)),
        "P551" to Vertex("P551", Offset(645.9629f, 523.9746f)),
        "P552" to Vertex("P552", Offset(659.9707f, 535.9131f)),
        "P553" to Vertex("P553", Offset(660.95996f, 540.9668f)),
        "P554" to Vertex("P554", Offset(667.94727f, 544.9219f)),
        "P555" to Vertex("P555", Offset(499.95508f, 537.96387f)),
        "P556" to Vertex("P556", Offset(513.9629f, 524.92676f)),
        "P557" to Vertex("P557", Offset(527.9697f, 527.9297f)),
        "P558" to Vertex("P558", Offset(589.9658f, 556.9336f)),
        "P559" to Vertex("P559", Offset(614.9492f, 573.9258f)),
        "P560" to Vertex("P560", Offset(1076.9346f, 896.9238f)),
        "P561" to Vertex("P561", Offset(1076.9346f, 457.9834f)),
        "P562" to Vertex("P562", Offset(0f, 889.9658f)),
        "P563" to Vertex("P563", Offset(0f, 1096.9482f)),
        "P564" to Vertex("P564", Offset(1076.9346f, 1096.9482f)),
        "P565" to Vertex("P565", Offset(0f, 458.93555f)),
        "P566" to Vertex("P566", Offset(0f, 0f)),
        "P567" to Vertex("P567", Offset(999.95605f, 0f)),
        "P568" to Vertex("P588", Offset(913.9531f, 94.99512f)),
        "P569" to Vertex("P569", Offset(905.94336f, 136.96289f)),
        "P570" to Vertex("P570", Offset(881.9492f, 171.97266f)),
        "P571" to Vertex("P571", Offset(870.9414f, 221.92383f)),
        "P572" to Vertex("P572", Offset(905.94336f, 263.96484f)),
        )
    // Fixed map markers of routes, cities, etc.

    val locationMarkers = listOf<MapMarker>(
        MapMarker(
            "Casseroya Lake",
            position = Offset(0f, 0f),
            MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(1..40),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "Poco Path",
            position = Offset(516.9619f, 1016.9678f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Inlet Grotto",
            position = Offset(556.97363f, 1009.9365f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Glaseado Mountain",
            position = Offset(612.08923f, 339.79718f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Socarrat Trail",
            position = Offset(245.97266f, 216.94336f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Zero Lab",
            position = Offset(513.9629f, 669.89355f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Dalizapa Passage",
            position = Offset(573.9755f, 521.96826f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Colonnade Hollow",
            position = Offset(261.0009f, 506.00076f),
            MarkerShapeClass.MARKER
        ),MapMarker(
            "Alfornada Cavern",
            position = Offset(137.92786f, 970.22656f),
            MarkerShapeClass.MARKER
        ),
        MapMarker(
            "Glaseado Mountain (North)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path =
                buildPathFromVertices(
                    flattenNumbers(12, 41..95, 23 downTo 12),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "North Province (Area Three)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(41, 96..99, 61 downTo 41),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "West Province (Area Two)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(1, 100..117, 34..40, 1),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "West Province (Area Three)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(112, 118..150, 87..94, 23..34, 117 downTo 112),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "Asado Desert",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(103, 151..174, 119 downTo 103),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "West Province (Area One)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(134, 175..209, 153..174, 119..134),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area Six)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(202, 210..234, 197..202),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area Two)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(193, 235..260, 178..193),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area Four)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(217, 261..285, 244 downTo 235, 193..197, 234 downTo 217),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area One)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(255, 286..327, 268..285, 245..255),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area Five)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(298, 328..355, 319 downTo 298),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Province (Area Three)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(294, 356..385, 345 downTo 328, 298 downTo 294),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "East Province (Area One)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(361, 386..418, 349 downTo 345, 385 downTo 361),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "East Province (Area Two)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        396, 419..440, 409 downTo 396
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "East Province (Area Three)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        396, 441..465, 433 downTo 419, 396
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "North Province (Area Two)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        452, 466..499, 461 downTo 452
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "North Province (Area One)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        69, 500..524, 482 downTo 466, 452 downTo 451, 540 downTo 525, 76 downTo 69
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "Tagtree Thicket",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        533, 541..554, 448..451, 540 downTo 533
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "Glaseado Mountain (South)",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        141,
                        555..559,
                        389..396,
                        441..448,
                        554 downTo 541,
                        533 downTo 525,
                        76..87,
                        150 downTo 141
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "Area Zero",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        141 downTo 134,
                        175..178,
                        260 downTo 256,
                        286..294,
                        356..361,
                        386..389,
                        559 downTo 555,
                        141
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "East Paldean Sea",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        561, 497..499, 461..465, 433..440, 409..418, 349, 560..561
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "South Paldean Sea",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        202, 562..564, 560, 349..355, 319..327, 268 downTo 261, 217 downTo 210, 202
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "West Paldean Sea",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        1, 565, 562, 202..209, 153 downTo 151, 103 downTo 100, 1
                    ),
                    vertexMap = vertices
                )
            )
        ),
        MapMarker(
            "North Paldean Sea",
            position = Offset.Zero,
            shape = MarkerShapeClass.CUSTOM_SHAPE(
                path = buildPathFromVertices(
                    flattenNumbers(
                        566..572,
                        524 downTo 500,
                        69 downTo 61,
                        99 downTo 96,
                        41,
                        12 downTo 1,
                        565,
                        566
                    ),
                    vertexMap = vertices
                )
            )
        )
    )

    Map(
        locationMarkers,
        pokemonEncounterLocations,
        devMode,
        focusedLocations,
        imageOriginalSize,
        image,
        vertices
    )
}


