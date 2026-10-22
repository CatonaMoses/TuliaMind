package com.example.tuliamind.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

private data class GroundStep(val count: Int, val sense: String, val hint: String, val color: Color)

@Composable
fun PanicScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val steps = listOf(
        GroundStep(5, "SEE",   "Look around. Name 5 things you can see.",    Carbon.Blue60),
        GroundStep(4, "TOUCH", "Notice 4 things you can feel or touch.",     Carbon.Purple60),
        GroundStep(3, "HEAR",  "Listen. Name 3 sounds you can hear.",        Carbon.Teal50),
        GroundStep(2, "SMELL", "Find 2 things you can smell.",               Carbon.Magenta50),
        GroundStep(1, "TASTE", "Notice 1 thing you can taste.",              Carbon.Orange60)
    )
    var index by remember { mutableStateOf(0) }
    val done = index >= steps.size

    ScreenScaffold("You are safe", onBack) {
        Text(
            "This feeling will pass. Let us slow down together.",
            fontSize = 17.sp,
            color = Carbon.Gray10
        )
        Spacer(Modifier.height(20.dp))

        if (!done) {
            val s = steps[index]
            // ── Full-width step card ──────────────────────────────────────────
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(s.color, RoundedCornerShape(8.dp))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${s.count}",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "things you can ${s.sense}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(s.hint, color = Carbon.Gray30, fontSize = 14.sp)
            Spacer(Modifier.height(14.dp))
            CarbonButton("Next →", s.color) { index++ }
        } else {
            CarbonCard(Carbon.Green50) {
                Text("Well done.", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Take one more slow breath. If you still feel unsafe, call someone below.",
                    color = Carbon.Gray10,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            CarbonButton("Do it again", Carbon.Gray80) { index = 0 }
        }

        Spacer(Modifier.height(28.dp))
        Tag("Button", "android.widget.Button  (tap-to-call)", Carbon.Red60)
        Text("Talk to someone now", fontSize = 17.sp, fontWeight = FontWeight.Medium, color = Color.White)
        Spacer(Modifier.height(8.dp))
        CarbonButton("Call Kenya Red Cross  1199", Carbon.Red60) { dial(ctx, "1199") }
        Spacer(Modifier.height(8.dp))
        CarbonButton("Emergency  999", Carbon.Gray80) { dial(ctx, "999") }
        Disclaimer()
    }
}
