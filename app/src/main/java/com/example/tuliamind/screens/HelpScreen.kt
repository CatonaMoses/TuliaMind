package com.example.tuliamind.screens

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

private data class Contact(
    val name: String,
    val about: String,
    val number: String,
    val type: String
)

@Composable
fun HelpScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val contacts = listOf(
        Contact("Kenya Red Cross counselling", "Free counselling line",                             "1199",         "Crisis"),
        Contact("Befrienders Kenya",           "Emotional support and suicide prevention",          "+254722178177","Crisis"),
        Contact("National emergency",          "Police, ambulance, fire",                           "999",          "Crisis"),
        Contact("University counselling",      "SAMPLE: replace with your campus counsellor",       "0700000001",   "Clinical"),
        Contact("County referral hospital",    "SAMPLE: mental health clinic",                      "0700000002",   "Clinical"),
        Contact("Guidance teacher",            "SAMPLE: trained guidance and counselling teacher",  "0700000003",   "Lay"),
        Contact("Community health volunteer",  "SAMPLE: trained in psychological first aid",        "0700000004",   "Lay"),
        Contact("Chaplain or youth leader",    "SAMPLE: faith-based support",                       "0700000005",   "Lay")
    )
    val filters = listOf("All", "Crisis", "Clinical", "Lay")
    var filter by remember { mutableStateOf("All") }

    fun colorOf(type: String) = when (type) {
        "Crisis"   -> Carbon.Red60
        "Clinical" -> Carbon.Blue60
        else       -> Carbon.Teal50
    }

    ScreenScaffold("Get help", onBack) {
        Text(
            "You do not have to carry this alone. Tap a name to call.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(16.dp))

        // ── Filter tabs ──────────────────────────────────────────────────────
        Tag("RadioButton", "filter tabs = android.widget.RadioGroup", Carbon.Cyan50)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filters.forEach { f ->
                Box(
                    Modifier
                        .weight(1f)
                        .background(
                            if (filter == f) Carbon.Blue60 else Carbon.Gray80,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { filter = f }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { Text(f, color = Color.White, fontSize = 13.sp) }
            }
        }
        Spacer(Modifier.height(16.dp))

        contacts.filter { filter == "All" || it.type == filter }.forEach { c ->
            val accent = colorOf(c.type)
            CarbonCard(accent) {
                Text(
                    c.type.uppercase(),
                    fontSize = 10.sp,
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
                Text(c.name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(c.about, color = Carbon.Gray30, fontSize = 13.sp)
                Spacer(Modifier.height(10.dp))
                CarbonButton("Call ${c.number}", accent) { dial(ctx, c.number) }
            }
            Spacer(Modifier.height(12.dp))
        }
        Disclaimer()
    }
}
