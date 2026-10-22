package com.example.tuliamind.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/**
 * COMPONENT 3: BROADCAST RECEIVER.
 * Dynamic (registered in code, lives while screen is open): battery level and airplane mode.
 * Static (declared in Manifest): ReminderReceiver, triggered by the custom broadcast button.
 */
@Composable
fun BroadcastDemo(ctx: Context) {
    var battery  by remember { mutableStateOf("waiting…") }
    var airplane by remember {
        mutableStateOf(
            if (Settings.Global.getInt(ctx.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1) "ON" else "OFF"
        )
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, i: Intent) {
                when (i.action) {
                    Intent.ACTION_BATTERY_CHANGED -> {
                        val level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                        val scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                        if (level >= 0) battery = "${level * 100 / scale}%"
                    }
                    Intent.ACTION_AIRPLANE_MODE_CHANGED ->
                        airplane = if (i.getBooleanExtra("state", false)) "ON" else "OFF"
                }
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        ContextCompat.registerReceiver(ctx, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        onDispose { ctx.unregisterReceiver(receiver) }
    }

    CarbonCard(Carbon.Magenta50) {
        Tag("BroadcastReceiver", "android.content.BroadcastReceiver", Carbon.Magenta50)
        Text("4. Broadcast Receiver", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "A receiver listens for broadcasts from the system or other apps. The live values below " +
                "come from a receiver registered in code. Toggle airplane mode or plug in a charger " +
                "and watch them update.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))
        Text("Battery level: $battery", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Carbon.Magenta50)
        Text("Airplane mode: $airplane", fontSize = 16.sp, color = Carbon.Gray10)
        Spacer(Modifier.height(12.dp))
        Text(
            "The button sends a custom broadcast. ReminderReceiver (declared in Manifest) catches it and shows a Toast.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))
        CarbonButton("Send custom broadcast", Carbon.Magenta50) {
            ctx.sendBroadcast(Intent(ACTION_REMINDER).setClass(ctx, ReminderReceiver::class.java))
        }
    }
}
