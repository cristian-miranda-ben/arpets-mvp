package com.example.ar2.scenes.scenes_ar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sqrt


@Composable
fun Control(
    size: Int = 150,
    onMove: (angle: Float, strength: Float) -> Unit
) {
    var knobPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(0x3300FF00))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = knobPosition + dragAmount
                        val radius = size.dp.toPx() / 2f
                        val limited = if (newOffset.getDistance() < radius) newOffset
                        else newOffset / newOffset.getDistance() * radius

                        knobPosition = limited

                        // Convertir a ángulo e intensidad
                        val angle = atan2(-limited.x, -limited.y) * (180f / Math.PI).toFloat()
                        val strength = min(limited.getDistance() / radius * 100f, 100f)
                        onMove(angle, strength)
                    },
                    onDragEnd = {
                        knobPosition = Offset.Zero
                        onMove(0f, 0f)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size / 2.5).dp)
                .offset { IntOffset(knobPosition.x.toInt(), knobPosition.y.toInt()) }
                .clip(CircleShape)
                .background(Color.Green)
        )
    }
}
