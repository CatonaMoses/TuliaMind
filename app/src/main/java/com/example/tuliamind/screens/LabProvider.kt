package com.example.tuliamind.screens

import android.content.ContentValues
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/** COMPONENT 4: CONTENT PROVIDER — reads and writes the mood table through a content:// address. */
@Composable
fun ProviderDemo(ctx: Context) {
    var output by remember { mutableStateOf("Nothing queried yet.") }

    CarbonCard(Carbon.Green50) {
        Tag("ContentProvider", "android.content.ContentProvider", Carbon.Green50)
        Text("5. Content Provider", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "A Content Provider shares data through a content:// address via a ContentResolver. " +
                "Phone contacts and the calendar work the same way. " +
                "Here MoodProvider shares this app's mood table.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            MoodProvider.CONTENT_URI.toString(),
            color = Carbon.Green50,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CarbonButton("Insert a mood", Carbon.Green60, Modifier.weight(1f)) {
                val values = ContentValues().apply {
                    put("score", 4)
                    put("note", "Inserted through the ContentProvider")
                    put("day", today())
                }
                ctx.contentResolver.insert(MoodProvider.CONTENT_URI, values)
                output = "Inserted one row. Now press Query."
            }
            CarbonButton("Query", Carbon.Gray80, Modifier.weight(1f)) {
                val sb = StringBuilder()
                ctx.contentResolver.query(MoodProvider.CONTENT_URI, null, null, null, null)?.use { c ->
                    sb.append("Rows: ${c.count}\n")
                    var shown = 0
                    while (c.moveToNext() && shown < 3) {
                        sb.append(
                            "#${c.getInt(c.getColumnIndexOrThrow("_id"))}  " +
                                "mood ${c.getInt(c.getColumnIndexOrThrow("score"))}  " +
                                "${c.getString(c.getColumnIndexOrThrow("day"))}\n"
                        )
                        shown++
                    }
                }
                output = sb.toString().trim().ifEmpty { "No result" }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(output, color = Carbon.Gray10, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
    }
}
