package com.example.tuliamind.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.ui.theme.Carbon
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    var show by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (show) 1f else 0.3f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )
    val tagAlpha by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 500),
        label = "tagAlpha"
    )

    LaunchedEffect(Unit) {
        show = true
        delay(2600)
        onDone()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF0D1B2A), Carbon.Gray100, Color(0xFF0D1B2A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative glow rings
        Box(
            Modifier
                .size(280.dp)
                .background(Color(0x0A0F62FE), CircleShape)
        )
        Box(
            Modifier
                .size(190.dp)
                .background(Color(0x120F62FE), CircleShape)
        )

        // Logo + name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale).alpha(alpha)
        ) {
            Box(
                Modifier
                    .size(96.dp)
                    .background(
                        Brush.linearGradient(listOf(Carbon.Blue60, Carbon.Purple60)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("T", fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(20.dp))
            Text("TuliaMind", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Tagline below logo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(tagAlpha)
                .padding(top = 172.dp)
        ) {
            Text(
                "Calm. Private. Yours.",
                fontSize = 15.sp,
                color = Carbon.Gray30,
                fontStyle = FontStyle.Italic
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Mental wellness for Kenyan youth",
                fontSize = 12.sp,
                color = Carbon.Blue40
            )
        }
    }
}
