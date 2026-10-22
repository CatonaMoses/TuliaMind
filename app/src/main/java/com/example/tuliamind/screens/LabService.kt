package com.example.tuliamind.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/** COMPONENT 2: SERVICE — a meditation timer that keeps running with no screen of its own. */
@Composable
fun ServiceDemo(ctx: Context) {
    val running by LabState.timerRunning
    val secs    by LabState.timerSeconds

    CarbonCard(Carbon.Teal50) {
        Tag("Service", "android.app.Service", Carbon.Teal50)
        Text("3. Service", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "A Service does work in the background with no screen. This timer is a started Service: " +
                "press Start, leave this screen, come back, and it has kept counting.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "%02d:%02d".format(secs / 60, secs % 60),
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold,
            color = Carbon.Teal50
        )
        Text(
            if (running) "Service is RUNNING" else "Service is stopped",
            color = if (running) Carbon.Green50 else Carbon.Gray30,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CarbonButton("Start", Carbon.Teal50, Modifier.weight(1f)) {
                ctx.startService(Intent(ctx, MeditationTimerService::class.java))
            }
            CarbonButton("Stop", Carbon.Red60, Modifier.weight(1f)) {
                ctx.stopService(Intent(ctx, MeditationTimerService::class.java))
            }
            CarbonButton("Reset", Carbon.Gray80, Modifier.weight(1f)) {
                LabState.timerSeconds.value = 0
            }
        }
    }
}
