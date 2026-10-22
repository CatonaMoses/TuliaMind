package com.example.tuliamind.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

private fun milestone(d: Int) = when {
    d >= 90 -> "🏆 90 days. Incredible strength!"
    d >= 30 -> "🌟 One month strong!"
    d >= 7  -> "✅ One week down. Keep going!"
    d >= 1  -> "Every day counts."
    else    -> "Day one. You have started — that is the hardest part."
}

@Composable
fun StreakScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { Db(context) }
    val substances = listOf("Alcohol", "Miraa (khat)", "Cannabis", "Cigarettes")
    val colors = listOf(Carbon.Cyan50, Carbon.Green50, Carbon.Purple60, Carbon.Magenta50)
    val triggers = listOf(
        "Exam stress", "Peer pressure", "Family conflict",
        "Boredom", "Loneliness", "Money worries"
    )
    var tick by remember { mutableStateOf(0) }
    val days = remember(tick) { substances.map { db.streakDays(it) } }
    val top = remember(tick) { db.topTriggers() }
    var logged by remember { mutableStateOf("") }

    ScreenScaffold("Sober streak", onBack) {
        Text(
            "One day at a time. Start a streak for anything you want to cut down or quit.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(16.dp))

        // ── Streak cards ─────────────────────────────────────────────────────
        substances.forEachIndexed { i, s ->
            CarbonCard(colors[i]) {
                Text(s, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                val d = days[i]
                if (d == null) {
                    Spacer(Modifier.height(8.dp))
                    CarbonButton("Start my streak", colors[i]) { db.startStreak(s); tick++ }
                } else {
                    Text(
                        "$d",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors[i]
                    )
                    Text(if (d == 1) "day" else "days", color = Carbon.Gray30, fontSize = 13.sp)
                    Text(milestone(d), color = Carbon.Gray10, fontSize = 13.sp)
                    TextButton(onClick = { db.resetStreak(s); tick++ }) {
                        Text("Reset", color = Carbon.Red60, fontSize = 13.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── Trigger chips ────────────────────────────────────────────────────
        Spacer(Modifier.height(8.dp))
        Tag("CheckBox", "trigger chip = android.widget.CheckBox", Carbon.Orange40)
        Text(
            "Felt a craving? What triggered it?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        triggers.chunked(2).forEach { pair ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pair.forEach { t ->
                    Box(
                        Modifier
                            .weight(1f)
                            .background(Carbon.Gray80, RoundedCornerShape(8.dp))
                            .clickable { db.addTrigger(t); logged = t; tick++ }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(t, color = Color.White, fontSize = 14.sp) }
                }
            }
        }
        if (logged.isNotEmpty()) {
            Text(
                "Logged: $logged. Noticing it is the first step.",
                color = Carbon.Green50,
                fontSize = 13.sp
            )
        }

        // ── Top triggers ─────────────────────────────────────────────────────
        Spacer(Modifier.height(20.dp))
        Text("Your top triggers", fontSize = 17.sp, fontWeight = FontWeight.Medium, color = Color.White)
        Spacer(Modifier.height(8.dp))
        if (top.isEmpty()) {
            Text("Nothing logged yet.", color = Carbon.Gray30)
        }
        top.forEach { (name, count) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, color = Color.White, modifier = Modifier.weight(1f), fontSize = 14.sp)
                Box(
                    Modifier
                        .height(10.dp)
                        .width(minOf(count * 24, 160).dp)
                        .background(Carbon.Magenta50, RoundedCornerShape(4.dp))
                )
                Text("  $count", color = Carbon.Gray30, fontSize = 13.sp)
            }
        }
        Disclaimer()
    }
}
