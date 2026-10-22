package com.example.tuliamind.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/** COMPONENT 1: ACTIVITY — opens a second Activity with an EXPLICIT Intent and shows the lifecycle log. */
@Composable
fun ActivityDemo(ctx: Context) {
    CarbonCard(Carbon.Blue60) {
        Tag("Activity", "android.app.Activity", Carbon.Blue60)
        Text("1. Activity", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "An Activity is one screen with a lifecycle. This app has two: MainActivity (hosts every " +
                "Compose screen) and AboutActivity. Open the second one, press back, then read the log below " +
                "to see onPause and onStop fire when you leave, and onStart and onResume when you return.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(12.dp))
        CarbonButton("Open second Activity", Carbon.Blue60) {
            ctx.startActivity(
                Intent(ctx, AboutActivity::class.java)
                    .putExtra("from", "Android Lab")
                    .putExtra("note", "Data travels inside an Intent as extras.")
            )
        }
        Spacer(Modifier.height(12.dp))
        Text("Lifecycle log (newest first)", fontWeight = FontWeight.Medium, color = Color.White, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        LabState.lifecycleLog.take(8).forEach {
            Text(it, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = Carbon.Cyan50)
        }
    }
}
