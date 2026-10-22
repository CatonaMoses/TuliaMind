package com.example.tuliamind.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

@Composable
fun QuizScreen(onBack: () -> Unit, onHelp: () -> Unit) {
    val questions = listOf(
        "Little interest or pleasure in doing things",
        "Feeling down, sad or hopeless",
        "Trouble falling or staying asleep, or sleeping too much",
        "Feeling tired or having little energy",
        "Feeling nervous, anxious or on edge",
        "Trouble concentrating on studies or work",
        "Feeling bad about yourself or that you are letting people down"
    )
    val options = listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    val done = index >= questions.size

    val progress by animateFloatAsState(
        targetValue = (if (done) questions.size else index + 1).toFloat() / questions.size,
        animationSpec = tween(400),
        label = "progress"
    )

    ScreenScaffold("Self-check", onBack) {
        if (!done) {
            // ── Progress bar ─────────────────────────────────────────────────
            Tag("TextView", "question counter = android.widget.TextView", Carbon.Cyan50)
            Text("Question ${index + 1} of ${questions.size}", color = Carbon.Gray30, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Carbon.Gray80, RoundedCornerShape(3.dp))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Carbon.Cyan50, RoundedCornerShape(3.dp))
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("Over the last 2 weeks, how often have you had:", color = Carbon.Gray30, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Text(questions[index], fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(24.dp))

            // ── Answer buttons (RadioButton-equivalent) ──────────────────────
            Tag("RadioButton", "android.widget.RadioButton  (single choice)", Carbon.Blue60)
            options.forEachIndexed { i, text ->
                CarbonButton(text, Carbon.Gray80) { score += i; index++ }
                Spacer(Modifier.height(8.dp))
            }
        } else {
            // ── Result ───────────────────────────────────────────────────────
            val (title, msg, accent) = when {
                score <= 6  -> Triple(
                    "Low level of distress",
                    "Your answers suggest you are coping fairly well. Keep checking in on your mood, sleep, food and friendships.",
                    Carbon.Green50
                )
                score <= 13 -> Triple(
                    "Moderate level of distress",
                    "You may be carrying a lot. Try the breathing exercise and talk to someone you trust. Consider speaking to a counsellor.",
                    Carbon.Yellow30
                )
                else -> Triple(
                    "High level of distress",
                    "Please reach out for support soon. Talking to a counsellor, doctor or trusted person can really help.",
                    Carbon.Red60
                )
            }
            CarbonCard(accent) {
                Text("Your score", color = Carbon.Gray30, fontSize = 13.sp)
                Text("$score / 21", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = accent)
                Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Text(msg, color = Carbon.Gray10, fontSize = 14.sp, lineHeight = 20.sp)
            }
            Spacer(Modifier.height(16.dp))
            CarbonButton("Find support", Carbon.Green50) { onHelp() }
            Spacer(Modifier.height(8.dp))
            CarbonButton("Retake", Carbon.Gray80) { index = 0; score = 0 }
            Disclaimer()
        }
    }
}
