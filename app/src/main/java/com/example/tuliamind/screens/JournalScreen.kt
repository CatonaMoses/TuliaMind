package com.example.tuliamind.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

@Composable
fun JournalScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { Db(context) }
    var entries by remember { mutableStateOf(db.journals()) }
    var adding by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    ScreenScaffold("Private journal", onBack) {
        Text(
            "🔒 Only you can see this. Entries never leave this phone.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(16.dp))

        if (adding) {
            Tag("TextField", "android.widget.EditText  (title)", Carbon.Blue60)
            CarbonField(title, { title = it }, "Title")
            Spacer(Modifier.height(12.dp))
            Tag("TextField", "android.widget.EditText  (body)", Carbon.Purple60)
            CarbonField(body, { body = it }, "What is on your mind?", minLines = 5)
            Spacer(Modifier.height(12.dp))
            Tag("Button", "android.widget.Button", Carbon.Green50)
            CarbonButton("Save entry", Carbon.Green50) {
                if (body.isNotBlank()) {
                    db.addJournal(title.ifBlank { "Untitled" }, body)
                    entries = db.journals()
                    title = ""; body = ""; adding = false
                }
            }
            Spacer(Modifier.height(8.dp))
            CarbonButton("Cancel", Carbon.Gray80) { adding = false }
        } else {
            CarbonButton("+ New entry", Carbon.Purple60) { adding = true }
        }

        Spacer(Modifier.height(20.dp))
        if (entries.isEmpty() && !adding) {
            Text("No entries yet. Writing even two lines can help.", color = Carbon.Gray30)
        }
        entries.forEach { e ->
            CarbonCard(Carbon.Purple60) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(e.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White,
                        modifier = Modifier.weight(1f))
                    Text(
                        e.created,
                        fontSize = 11.sp,
                        color = Carbon.Gray30,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(e.body, color = Carbon.Gray10, fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(Modifier.height(4.dp))
                TextButton(onClick = { db.deleteJournal(e.id); entries = db.journals() }) {
                    Text("Delete", color = Carbon.Red60, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
