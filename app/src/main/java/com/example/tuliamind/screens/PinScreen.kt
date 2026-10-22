package com.example.tuliamind.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.sha256
import com.example.tuliamind.ui.theme.Carbon

@Composable
fun PinScreen(onUnlocked: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { ctx.getSharedPreferences("tulia", Context.MODE_PRIVATE) }
    val hasPin = remember { prefs.contains("pin_hash") }
    var pin   by remember { mutableStateOf("") }
    var first by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val title = when {
        hasPin        -> "Welcome back"
        first.isEmpty() -> "Create a 4-digit PIN"
        else          -> "Confirm your PIN"
    }

    fun submit() {
        when {
            hasPin -> {
                if (sha256(pin) == prefs.getString("pin_hash", "")) {
                    onUnlocked()
                } else {
                    error = "Wrong PIN, try again"; pin = ""
                }
            }
            first.isEmpty() -> { first = pin; pin = ""; error = "" }
            pin == first -> {
                prefs.edit().putString("pin_hash", sha256(pin)).apply()
                onUnlocked()
            }
            else -> { error = "PINs did not match. Start again."; first = ""; pin = "" }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Carbon.Gray100, Color(0xFF0D1B2A))))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text("🔒", fontSize = 44.sp)
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Your PIN keeps your journal private.", color = Carbon.Gray30, fontSize = 13.sp)
        Spacer(Modifier.height(24.dp))

        // PIN dot indicator
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { i ->
                Box(
                    Modifier
                        .size(16.dp)
                        .background(
                            if (i < pin.length) Carbon.Blue40 else Carbon.Gray70,
                            RoundedCornerShape(8.dp)
                        )
                )
            }
        }
        Text(
            error,
            color = Carbon.Red60,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 10.dp).height(22.dp)
        )
        Spacer(Modifier.weight(1f))

        // Number pad
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("",  "0", "⌫")
        )
        keys.forEach { row ->
            Row(Modifier.fillMaxWidth()) {
                row.forEach { k ->
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(60.dp)
                            .background(
                                if (k.isEmpty()) Color.Transparent else Carbon.Gray80,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = k.isNotEmpty()) {
                                error = ""
                                when {
                                    k == "⌫"       -> pin = pin.dropLast(1)
                                    pin.length < 4 -> {
                                        pin += k
                                        if (pin.length == 4) submit()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(k, fontSize = 22.sp, color = Color.White)
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
