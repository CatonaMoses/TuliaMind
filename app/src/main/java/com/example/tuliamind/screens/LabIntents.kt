package com.example.tuliamind.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/** Intents: messages that ask Android to do something.
 *  EXPLICIT = names the exact component (the button in section 1).
 *  IMPLICIT = describes an action and Android finds an app to handle it. */
@Composable
fun IntentsDemo(ctx: Context) {
    val actions = remember {
        listOf(
            "Share" to Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "I use TuliaMind to look after my mind. You are not alone.")
                },
                "Share TuliaMind via"
            ),
            "Open website"   to Intent(Intent.ACTION_VIEW, Uri.parse("https://www.who.int/health-topics/mental-health")),
            "Dial 1199"      to Intent(Intent.ACTION_DIAL, Uri.parse("tel:1199")),
            "Send email"     to Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf("counsellor@example.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Appointment request")
            },
            "Find hospitals" to Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=hospital")),
            "Open settings"  to Intent(Settings.ACTION_SETTINGS)
        )
    }

    CarbonCard(Carbon.Orange60) {
        Tag("Intent", "android.content.Intent", Carbon.Orange60)
        Text("2. Intents", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "An Intent is a message. Explicit: names the exact component (section 1 button). " +
                "Implicit: describes an action and Android finds an app to handle it. " +
                "Each button below fires a different implicit Intent.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(12.dp))
        actions.chunked(2).forEach { pair ->
            Row(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pair.forEach { (label, intent) ->
                    CarbonButton(label, Carbon.Gray80, Modifier.weight(1f)) { safeStart(ctx, intent) }
                }
            }
        }
    }
}
