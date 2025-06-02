package com.emmanuelcastillo.livingdextracker.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import java.sql.Blob

data class Vertex(
    val name: String,
    val position: Offset
)

fun buildPathFromVertices(numbers: IntArray, vertexMap: Map<String, Vertex>): Path {
    val path = Path()
    // Move through each vertex in the name argument, and return the path
    numbers.forEachIndexed { index, number ->
        val vertex = vertexMap["P$number"] ?: error("Vertex $number is not found")
        if (index == 0) {
            path.moveTo(vertex.position.x, vertex.position.y)
        } else {
            path.lineTo(vertex.position.x, vertex.position.y)
        }
    }
    return path
}

fun flattenNumbers(vararg items: Any): IntArray {
    return items.flatMap {
        when (it) {
            is Int -> listOf(it)
            is IntRange -> it.toList()
            is IntProgression -> it.toList()
            else -> error("Unsupported type: $it")
        }
    }.toIntArray()
}
data class MapMarker(
    val name: String,
    val position: Offset, // Offset(x, y) in pixels relative to original image size
    val shape: MarkerShapeClass = MarkerShapeClass.MARKER
)

data class SingleMap(
    val mapImageSize: IntSize,
    val image: Int,
    val mapName: String,
    val locationMarkers: List<MapMarker>
)

sealed class MarkerShapeClass {
    object MARKER : MarkerShapeClass()
    class CIRCLE(val size: Dp) : MarkerShapeClass()
    class FOUR_BY_FOUR_EXCLUDE_CORNER(val corner: String) : MarkerShapeClass()
    class VLINE(val height: Float) : MarkerShapeClass()
    class HLINE(val width: Float) : MarkerShapeClass()
    class RECT(val width: Dp, val height: Dp, val rotation: Float = 0f) : MarkerShapeClass()
    class CUSTOM_SHAPE(val path: Path) : MarkerShapeClass()
}

//@Composable
//fun MapEditor(
//
//)

@Composable
fun Map(
    locationMarkers: List<MapMarker>,
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>,
    imageOriginalSize: IntSize,
    image: Int,
    vertices: Map<String, Vertex> = emptyMap()
) {

    // Markers set from pokemon encounter location names
    var markers by remember(locationMarkers) {
        mutableStateOf(
            if (devMode) locationMarkers
            else locationMarkers.filter { marker ->
                pokemonEncounterLocations.any { locationName ->
                    marker.name.equals(locationName, ignoreCase = true)
                }
            }
        )
    }

    var verticesOnMap by remember { mutableStateOf(vertices) }

    // State for map zooming and panning
    var scale by remember { mutableStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }

    // Known size of the map asset
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .background(color = Color.Transparent.copy(alpha = .4f))
            .pointerInput(Unit) {
                if (!devMode) {
                    detectTransformGestures { _, panChange, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3f)

                        val scaledWidth = imageOriginalSize.width * scale
                        val scaledHeight = imageOriginalSize.height * scale

                        val maxPanX = ((scaledWidth - containerSize.width) / 2f).coerceAtLeast(0f)
                        val maxPanY = ((scaledHeight - containerSize.height) / 2f).coerceAtLeast(0f)


                        val proposedPan = pan + panChange
                        pan = Offset(
                            x = proposedPan.x.coerceIn(-maxPanX, maxPanX),
                            y = proposedPan.y.coerceIn(-maxPanY, maxPanY)
                        )
                    }
                }

            }
            .border(1.dp, Color.Black)
            .clipToBounds()
            .height(with(LocalDensity.current) { imageOriginalSize.height.toDp() })
            .onSizeChanged { containerSize = it } // Measure container
    ) {
        // This layer zooms and pans only the image + markers
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = pan.x,
                    translationY = pan.y
                )
                .border(1.dp, Color.Black)
                .pointerInput(scale, pan) {
                    if (devMode) {
                        detectTapGestures { tapOffset ->
                            val mapSpace = (tapOffset - pan) / scale
                            val newVertexName = "P${verticesOnMap.size + 1}"
                            markers = markers + MapMarker("New", mapSpace)
//                            verticesOnMap = verticesOnMap.plus(Pair(newVertexName, Vertex(newVertexName, mapSpace)))
                            println("New Marker: Offset(${mapSpace.x}f, ${mapSpace.y}f)")
//                            println("\"$newVertexName\" to Vertex(\"$newVertexName\", Offset(${mapSpace.x}f, ${mapSpace.y}f))")
                        }
                    }

                }
        ) {
            // Background image
            Image(
                painter = painterResource(id = image),
                contentDescription = "Region Map",
                contentScale = ContentScale.FillBounds
            )

            // Markers
            val infiniteTransition = rememberInfiniteTransition()
            markers.forEach { marker ->
                val animatedOpacity by infiniteTransition.animateFloat(
                    initialValue = 0.4f,
                    targetValue = .8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                val isFocused =
                    if (devMode) false else marker.name in focusedLocations
                val colorOpacity =
                    if (isFocused) animatedOpacity else
                        .3f

                when (marker.shape) {
                    is MarkerShapeClass.FOUR_BY_FOUR_EXCLUDE_CORNER -> Marker_Rect_ExcludeCorner(
                        marker, marker.shape.corner,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused,
                        canDrag = devMode
                    )

                    is MarkerShapeClass.CIRCLE -> Marker_Circle(
                        marker = marker,
                        size = marker.shape.size,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused,
                        canDrag = devMode
                    )

                    is MarkerShapeClass.VLINE -> Marker_VLine(
                        marker, end = marker.shape.height,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused,
                        canDrag = devMode
                    )

                    is MarkerShapeClass.HLINE -> Marker_HLine(
                        marker, end = marker.shape.width,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused,
                        canDrag = devMode
                    )

                    is MarkerShapeClass.RECT -> Marker_Rect(
                        marker,
                        width = marker.shape.width,
                        height = marker.shape.height,
                        rotation = marker.shape.rotation,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused,
                        canDrag = devMode,
                    )

                    is MarkerShapeClass.CUSTOM_SHAPE -> CustomShape(
                        marker,
                        canDrag = devMode,
                        path = marker.shape.path,
                        colorOpacity = colorOpacity,
                        isFocused = isFocused
                    )

                    else -> Marker(marker, 16.dp, isFocused, devMode)
                }
            }

            if (devMode) {
                verticesOnMap.forEach { vertex ->
                    val name = vertex.value.name
                    val position = vertex.value.position

                    var dragOffset by remember { mutableStateOf(position) }
                    val density = LocalDensity.current

                    val isFocused by remember { mutableStateOf(false) }

                    Box(Modifier
//                    .size(4.dp)
                        .offset(
                            x = with(density) { (dragOffset.x).toDp() },
                            y = with(density) { (dragOffset.y).toDp() }
                        )
//                    .border(1.dp, Color.Black)
                    ) {
                        Canvas(modifier = Modifier
                            .size(2.dp)
                            .pointerInput(Unit) {
                                if (devMode) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        dragOffset += dragAmount
                                        println("\"${name}\" to Vertex(\"${name}\", Offset(${dragOffset.x}f, ${dragOffset.y}f))")
                                    }

//                                detectTapGestures {
//                                    isFocused = !isFocused
//                                    print("tapped")
//                                }
                                }
                            }) {

                            // Glowing outer rectangle
                            drawCircle(
                                color = Color.Green,
                            )
                        }

//                        if (!isFocused) {
//                            Text(
//                                name,
//                                fontSize = 4.sp, modifier = Modifier
//                                    .align(Alignment.TopStart) // or Center, Bottom, etc. depending on your layout
//                                    .offset(
//                                        y = -12.dp,
//                                        x = -2.dp
//                                    ) // Offset so the text doesn't overlap the circle
//                            )
//                        }

                    }
                }
            }

        }
    }
}

@Composable
fun MultiMap(
    pokemonEncounterLocations: List<String>,
    devMode: Boolean,
    focusedLocations: List<String>,
    maps: List<SingleMap>
) {
    var region by remember { mutableStateOf(maps[0]) }

    Column(Modifier.wrapContentWidth()) {
        Map(
            region.locationMarkers,
            pokemonEncounterLocations,
            devMode,
            focusedLocations,
            region.mapImageSize,
            region.image
        )

        Row(
            modifier = Modifier
                .background(Color.Black.copy(alpha = .8f))
                .width((with(LocalDensity.current) { region.mapImageSize.width.toDp()}))
                .horizontalScroll(
                    rememberScrollState(),
                )
                .padding(PaddingValues(8.dp))
            , horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (map in maps) {
                Box(
                    modifier = Modifier
                        .background(if (map.mapName == region.mapName) Color.Black else Color.White)
                        .padding(8.dp)
                        .clickable { region = map }) {
                    Text(map.mapName, color = if (map.mapName == region.mapName) Color.White else Color.Black)
                }
            }

        }
    }

}

@Composable
fun CustomShape(
    marker: MapMarker,
    canDrag: Boolean,
    path: Path,
    colorOpacity: Float,
    isFocused: Boolean
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    Canvas(modifier = Modifier
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .size(24.dp)
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }

        }) {
        drawPath(path = path, color =
//            Color.Black.copy(alpha = .4f)
        Color.Green.copy(alpha = colorOpacity),
        )

        if (isFocused) {
            drawPath(path = path, color =
            Color.Black, style = Stroke(2f)
            )
        }
    }
}

@Composable
fun Marker(
    marker: MapMarker,
    size: Dp,
    isFocused: Boolean,
    canDrag: Boolean
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    // Animating a vertical up/down infinite for the poster
    val hoverOffset by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Icon(
        imageVector = Icons.Default.Place,
        contentDescription = "Marker",
        tint = Color.Red,
        modifier = Modifier
            .size(size)
            .offset(
                x = with(density) { dragOffset.x.toDp() - size/2},
                y = with(density) { dragOffset.y.toDp() - size })
//            .border(1.dp, Color.Black)
            .zIndex(2f)
            .graphicsLayer {
                translationY = if (isFocused) hoverOffset else 0f
            }
            .pointerInput(Unit) {
                if (canDrag) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
//                onPositionChanged(dragOffset)
                        println("Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                    }
                }

            }
    )

    if (isFocused) {
        Text(marker.name, fontSize = 8.sp, color = Color.Black , modifier = Modifier.offset(
            x = with(density) { dragOffset.x.toDp() - size * 2},
            y = with(density) { dragOffset.y.toDp() - 35.dp}).zIndex(3f))
    }
}

@Composable
fun Marker_Rect(
    marker: MapMarker,
    width: Dp,
    height: Dp,
    rotation: Float = 0f,
    color: Color = Color.Green,
    colorOpacity: Float,
    isFocused: Boolean,
    canDrag: Boolean,
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    Canvas(modifier = Modifier
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .size(width = width, height = height)
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }

        }) {

        // Glowing outer rectangle
        rotate(rotation) {
            drawRoundRect(
                color = color.copy(alpha = colorOpacity),
                topLeft = Offset.Zero,
                size = size,
            )
        }

        if (isFocused) {
            rotate(rotation) {
                drawRoundRect(
                    color = Color.Black,
                    topLeft = Offset.Zero,
                    size = size,
                    style = Stroke(1.dp.toPx())
                )
            }

        }
    }
}

@Composable
fun Marker_Rect_ExcludeCorner(
    marker: MapMarker,
    excludeCorner: String,
    color: Color = Color.Green,
    colorOpacity: Float,
    isFocused: Boolean,
    canDrag: Boolean,
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current
    val offset = marker.position

    Canvas(modifier = Modifier
        .size(14.dp)
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }
        }) {
        val cornerRadius = CornerRadius(2f, 2f)

        if (excludeCorner == "BottomRight") {
            // Glowing 2x2
            drawRoundRect(
                color = color.copy(alpha = colorOpacity),
                topLeft = Offset(x = size.width / 2, y = 0f),
                size = Size(width = size.width / 2, height = size.height / 2),
                cornerRadius = cornerRadius,
            )

            // Glowing 2x4
            drawRoundRect(
                color = color.copy(alpha = colorOpacity),
                topLeft = Offset.Zero,
                size = Size(width = size.width / 2, height = size.height),
                cornerRadius = cornerRadius,
            )
            if (isFocused) {
                drawLine(
                    color = Color.Black,
                    start = Offset.Zero,
                    end = Offset(0f, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width, size.height / 2),
                    end = Offset(size.width / 2, size.height / 2),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width / 2, size.height / 2),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width / 2, size.height),
                    end = Offset(0f, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        } else if (excludeCorner == "BottomLeft") {
            // Glowing 2x2
            drawRoundRect(
                color = color.copy(alpha = colorOpacity),
                topLeft = Offset(x = size.width / 2, y = size.height / 2),
                size = Size(width = size.width / 2, height = size.height / 2),
                cornerRadius = cornerRadius,
            )

            // Glowing 4x2
            drawRoundRect(
                color = color.copy(alpha = colorOpacity),
                topLeft = Offset.Zero,
                size = Size(width = size.width, height = size.height / 2),
                cornerRadius = cornerRadius,
            )
            if (isFocused) {
                drawLine(
                    color = Color.Black,
                    start = Offset.Zero,
                    end = Offset(0f, size.height / 2),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width / 2, size.height / 2),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width / 2, size.height / 2),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width / 2, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(size.width, 0f),
                    end = Offset.Zero,
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        // Border around the outer rectangle
//        drawRoundRect(
//            color = Color.Black,
//            topLeft = Offset.Zero,
//            size = outerSize,
//            cornerRadius = cornerRadius,
//            style = Stroke(width = 1.dp.toPx())
//        )

        // Opaque inner rectangle
//        drawRoundRect(
//            color = color.copy(alpha = .6f),
//            topLeft = Offset(
//                (size.width - innerSize.width) / 2,
//                (size.height - innerSize.height) / 2
//            ),
//            size = innerSize,
//            cornerRadius = cornerRadius
//        )
    }
}

@Composable
fun Marker_Circle(
    marker: MapMarker,
    size: Dp,
    color: Color = Color.Green,
    colorOpacity: Float,
    isFocused: Boolean,
    canDrag: Boolean
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    Canvas(modifier = Modifier
        .size(size)
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }
        }) {

        // Glowing outer rectangle
        drawCircle(
            color = color.copy(alpha = colorOpacity),
        )

        if (isFocused) {
            // Border around the outer rectangle
            drawCircle(
                color = Color.Black,
                style = Stroke(width = 1.dp.toPx()),
            )
        }
    }
}

@Composable
fun Marker_VLine(
    marker: MapMarker,
    color: Color = Color.Green,
    end: Float = 50f,
    colorOpacity: Float,
    isFocused: Boolean,
    canDrag: Boolean
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    Canvas(modifier = Modifier
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }
        }) {

        // Glowing outer rectangle
        drawLine(
            color = color.copy(alpha = colorOpacity),
            start = Offset.Zero,
            end = Offset(0f, end),
            strokeWidth = 2.dp.toPx()
        )
    }
//    if (isFocused) {
//        val x = marker.position.x - 10f
//        val y = marker.position.y - 10f
//
//        DraggableMarker(offset = Offset(x, y))
//    }
}

@Composable
fun Marker_HLine(
    marker: MapMarker,
    color: Color = Color.Green,
    end: Float = 50f,
    colorOpacity: Float,
    isFocused: Boolean,
    canDrag: Boolean
) {
    var dragOffset by remember { mutableStateOf(marker.position) }
    val density = LocalDensity.current

    Canvas(modifier = Modifier
        .offset(
            x = with(density) { (dragOffset.x).toDp() },
            y = with(density) { (dragOffset.y).toDp() }
        )
        .pointerInput(Unit) {
            if (canDrag) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount
                    println("${marker.name}: Offset(${dragOffset.x}f, ${dragOffset.y}f)")
                }
            }
        }) {

        // Glowing outer rectangle
        drawLine(
            color = color.copy(alpha = colorOpacity),
            start = Offset.Zero,
            end = Offset(end, 0f),
            strokeWidth = 2.dp.toPx()
        )

        // Opaque inner rectangle
//        drawRoundRect(
//            color = color.copy(alpha = .6f),
//            topLeft = Offset(
//                (size.width - innerSize.width) / 2,
//                (size.height - innerSize.height) / 2
//            ),
//            size = innerSize,
//            cornerRadius = cornerRadius
//        )
    }
}