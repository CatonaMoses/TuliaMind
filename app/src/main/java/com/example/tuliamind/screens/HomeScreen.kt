package com.example.tuliamind.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

private data class NavItem(
    val emoji: String,
    val title: String,
    val sub: String,
    val color: Color,
    val route: String
)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val hour = remember { java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) }
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else      -> "Good evening"
    }
    val tip = remember {
        listOf(
            "Drink some water and take three slow breaths.",
            "Talking to one trusted person can lighten the load.",
            "A 10-minute walk can lift your mood.",
            "Try to switch off screens 30 minutes before bed."
        ).random()
    }
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulseScale"
    )

    val wellbeing = listOf(
        NavItem("😊", "Mood",        "Daily check-in",   Carbon.Blue60,    "mood"),
        NavItem("📓", "Journal",     "Private notes",    Carbon.Purple60,  "journal"),
        NavItem("🌬️", "Breathe",    "Calm in 1 minute", Carbon.Teal50,    "breathing"),
        NavItem("📋", "Self-check",  "How am I doing?",  Carbon.Cyan50,    "quiz"),
        NavItem("🔥", "Sober streak","Track your days",  Carbon.Magenta50, "streak"),
        NavItem("📞", "Get help",    "Counsellors, lines",Carbon.Green50,  "help")
    )
    val connect = listOf(
        NavItem("📅", "Book",        "Counsellor visit",  Carbon.Orange60, "booking"),
        NavItem("📍", "Clinic map",  "Find help near you",Carbon.Green60,  "map"),
        NavItem("🌐", "Learn",       "WHO and MoH pages", Carbon.Cyan60,   "web"),
        NavItem("🧪", "Android Lab", "See how it works",  Carbon.Purple70, "lab")
    )

    androidx.compose.foundation.lazy.LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Carbon.Gray100)
    ) {
        // ── Hero header ──────────────────────────────────────────────────────
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF0D1F3C), Carbon.Blue70, Carbon.Purple60))
                    )
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Tag("TextView", "android.widget.TextView", Carbon.Blue40)
                Spacer(Modifier.height(6.dp))
                Text("$greeting 👋", fontSize = 15.sp, color = Color.White.copy(alpha = 0.8f))
                Text(
                    "How are you really doing today?",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        item {
            Column(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding()
            ) {
                // ── Panic button ─────────────────────────────────────────────
                Row(
                    Modifier
                        .fillMaxWidth()
                        .scale(pulse)
                        .background(Carbon.Red60, RoundedCornerShape(8.dp))
                        .clickable { onNavigate("panic") }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🆘", fontSize = 30.sp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "I need help now",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Grounding exercise or call a helpline",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))

                // ── Wellbeing tiles ──────────────────────────────────────────
                SectionTitle("Wellbeing tools")
                wellbeing.chunked(2).forEachIndexed { r, pair ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEachIndexed { c, item ->
                            Reveal(r * 2 + c, Modifier.weight(1f)) {
                                HomeTile(item) { onNavigate(item.route) }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                // ── Connect tiles ────────────────────────────────────────────
                Spacer(Modifier.height(4.dp))
                SectionTitle("Connect and learn")
                connect.chunked(2).forEachIndexed { r, pair ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEachIndexed { c, item ->
                            Reveal(6 + r * 2 + c, Modifier.weight(1f)) {
                                HomeTile(item) { onNavigate(item.route) }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                // ── Daily tip ────────────────────────────────────────────────
                Spacer(Modifier.height(4.dp))
                CarbonCard(Carbon.Yellow30) {
                    Text("💡 Tip of the day", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(6.dp))
                    Text(tip, color = Carbon.Gray10, fontSize = 14.sp)
                }
                Disclaimer()
            }
        }
    }
}

@Composable
private fun HomeTile(item: NavItem, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(126.dp)
            .background(item.color, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.emoji, fontSize = 28.sp)
        Column {
            Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(item.sub, fontSize = 11.sp, color = Color.White.copy(alpha = 0.82f))
        }
    }
}
