package com.example.tuliamind.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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

@Composable
fun MoodScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { Db(context) }

    val moods = listOf("😞" to "Low", "😟" to "Down", "😐" to "Okay", "🙂" to "Good", "😄" to "Great")
    val colors = listOf(Carbon.Red60, Carbon.Orange40, Carbon.Yellow30, Carbon.Teal50, Carbon.Green50)

    var selected by remember { mutableStateOf(-1) }
    var note by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(db.lastMoods(7)) }
    var saved by remember { mutableStateOf(false) }

    ScreenScaffold("Mood check-in", onBack) {
        Text(
            "How are you feeling right now?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Spacer(Modifier.height(16.dp))

        // ── Emoji selector (Spinner-equivalent row) ──────────────────────────
        Tag("Spinner", "android.widget.Spinner  /  row picker", Carbon.Magenta50)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            moods.forEachIndexed { i, (emoji, label) ->
                val bg by animateColorAsState(
                    targetValue = if (selected == i) colors[i] else Carbon.Gray80,
                    label = "moodBg"
                )
                Column(
                    Modifier
                        .weight(1f)
                        .background(bg, RoundedCornerShape(8.dp))
                        .clickable { selected = i; saved = false }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(emoji, fontSize = 28.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        label,
                        fontSize = 11.sp,
                        color = if (selected == i) Carbon.Gray100 else Carbon.Gray30
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Tag("TextField", "android.widget.EditText", Carbon.Blue60)
        CarbonField(note, { note = it }, "Add a note (optional)", minLines = 2)
        Spacer(Modifier.height(16.dp))

        Tag("Button", "android.widget.Button", Carbon.Green50)
        CarbonButton("Save check-in", Carbon.Blue60) {
            if (selected >= 0) {
                db.addMood(selected + 1, note)
                history = db.lastMoods(7)
                note = ""
                saved = true
            }
        }

        if (saved) {
            Spacer(Modifier.height(12.dp))
            val (msg, accent) = if (selected <= 1)
                "It is okay to not be okay. Try the breathing exercise, or talk to someone in the Help section." to Carbon.Orange40
            else
                "Saved. Keep checking in — small habits make a big difference." to Carbon.Green50
            CarbonCard(accent) {
                Text(msg, color = Carbon.Gray10, fontSize = 14.sp)
            }
        }

        // ── History bar chart ────────────────────────────────────────────────
        Spacer(Modifier.height(24.dp))
        Tag("TextView", "android.widget.TextView  (chart labels)", Carbon.Cyan50)
        Text(
            "Your last 7 check-ins",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        if (history.isEmpty()) {
            Text("No check-ins yet. Your chart will appear here.", color = Carbon.Gray30)
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Carbon.Gray90, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                history.forEach { (day, score) ->
                    MoodBar(day.takeLast(5), score, colors[score - 1])
                }
            }
        }
        Disclaimer()
    }
}

@Composable
private fun MoodBar(label: String, score: Int, color: Color) {
    var go by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { go = true }
    val h by animateDpAsState(
        targetValue = if (go) (score * 22).dp else 0.dp,
        animationSpec = tween(700),
        label = "bar"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            Modifier
                .width(26.dp)
                .height(h)
                .background(color, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = Carbon.Gray30)
    }
}
