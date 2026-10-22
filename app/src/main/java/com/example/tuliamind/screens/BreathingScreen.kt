package com.example.tuliamind.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon
import kotlinx.coroutines.delay

@Composable
fun BreathingScreen(onBack: () -> Unit) {
    var running by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("Tap Start when you are ready") }
    var cycles by remember { mutableStateOf(0) }
    val size = remember { Animatable(120f) }

    LaunchedEffect(running) {
        if (running) {
            while (true) {
                label = "Breathe in…"
                size.animateTo(240f, tween(4000, easing = LinearEasing))
                label = "Hold"
                delay(4000)
                label = "Breathe out…"
                size.animateTo(120f, tween(4000, easing = LinearEasing))
                cycles++
            }
        } else {
            label = "Tap Start when you are ready"
            size.snapTo(120f)
        }
    }

    ScreenScaffold("Breathe", onBack) {
        Tag("TextView", "animated label = android.widget.TextView", Carbon.Teal50)
        Text(
            "Box breathing: in for 4 · hold for 4 · out for 4",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(24.dp))

        // ── Animated circle ──────────────────────────────────────────────────
        Box(
            Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer glow ring
            Box(
                Modifier
                    .size((size.value + 32).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Carbon.Teal50.copy(alpha = 0.12f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )
            // Main circle
            Box(
                Modifier
                    .size(size.value.dp)
                    .background(
                        Brush.radialGradient(listOf(Carbon.Cyan50, Carbon.Teal50)),
                        CircleShape
                    )
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            label,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Cycles completed: $cycles",
            color = Carbon.Gray30,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        Tag("Button", "android.widget.Button  (start/stop)", Carbon.Blue60)
        CarbonButton(
            if (running) "Stop" else "Start",
            if (running) Carbon.Red60 else Carbon.Teal50
        ) { running = !running }

        Disclaimer()
    }
}
