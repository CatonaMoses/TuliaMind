package com.example.tuliamind.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.background
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
import java.util.Calendar

/**
 * BookingScreen showcases every required UI widget in one real use-case:
 * TextView · TextField · AutoComplete · Spinner · DatePicker · TimePicker
 * RadioButton · CheckBox · Button
 */
@Composable
fun BookingScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val db = remember { Db(ctx) }

    val counsellors = listOf(
        "Campus counsellor", "Clinical psychologist", "Psychiatrist",
        "School guidance teacher", "Community health volunteer", "Peer mentor"
    )
    val modes = listOf("In person", "Phone call", "Video call")
    val topicOptions = listOf("Stress", "Anxiety", "Low mood", "Substance use", "Relationships", "Academics")

    var name       by remember { mutableStateOf("") }
    var county     by remember { mutableStateOf("") }
    var counsellor by remember { mutableStateOf(counsellors[0]) }
    var date       by remember { mutableStateOf("") }
    var time       by remember { mutableStateOf("") }
    var mode       by remember { mutableStateOf(modes[0]) }
    val topics      = remember { mutableStateListOf<String>() }
    var notes      by remember { mutableStateOf("") }
    var consent    by remember { mutableStateOf(false) }
    var error      by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var appointments by remember { mutableStateOf(db.appointments()) }

    fun pickDate() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            ctx,
            android.R.style.Theme_DeviceDefault_Dialog_Alert,
            { _, y, m, d -> date = "%04d-%02d-%02d".format(y, m + 1, d) },
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = System.currentTimeMillis() }.show()
    }

    fun pickTime() {
        TimePickerDialog(
            ctx,
            android.R.style.Theme_DeviceDefault_Dialog_Alert,
            { _, h, min -> time = "%02d:%02d".format(h, min) },
            9, 0, true
        ).show()
    }

    ScreenScaffold("Book a counsellor", onBack) {

        // ── TextView ─────────────────────────────────────────────────────────
        CarbonCard(Carbon.Orange60) {
            Tag("TextView", "android.widget.TextView", Carbon.Purple60)
            Text("Talk to someone", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                "Fill in the form and your appointment is saved on this phone.",
                color = Carbon.Gray30, fontSize = 13.sp
            )
        }
        Spacer(Modifier.height(20.dp))

        // ── TextField ────────────────────────────────────────────────────────
        Tag("TextField", "android.widget.EditText", Carbon.Blue60)
        CarbonField(name, { name = it }, "Your name")
        Spacer(Modifier.height(20.dp))

        // ── AutoComplete ─────────────────────────────────────────────────────
        Tag("AutoComplete", "android.widget.AutoCompleteTextView", Carbon.Teal50)
        AutoCompleteField(county, { county = it }, "County (type a few letters, e.g. Nai)", KENYA_COUNTIES)
        Spacer(Modifier.height(20.dp))

        // ── Spinner ──────────────────────────────────────────────────────────
        Tag("Spinner", "android.widget.Spinner", Carbon.Magenta50)
        CarbonDropdown("Who would you like to see?", counsellors, counsellor) { counsellor = it }
        Spacer(Modifier.height(20.dp))

        // ── Date + Time pickers ──────────────────────────────────────────────
        Tag("Pickers", "DatePickerDialog + TimePickerDialog", Carbon.Orange60)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CarbonButton(
                if (date.isEmpty()) "📅  Pick date" else date,
                Carbon.Gray80,
                Modifier.weight(1f)
            ) { pickDate() }
            CarbonButton(
                if (time.isEmpty()) "🕐  Pick time" else time,
                Carbon.Gray80,
                Modifier.weight(1f)
            ) { pickTime() }
        }
        Spacer(Modifier.height(20.dp))

        // ── RadioButton ──────────────────────────────────────────────────────
        Tag("RadioButton", "android.widget.RadioButton  (pick one)", Carbon.Cyan50)
        modes.forEach { m -> CarbonRadio(m, mode == m) { mode = m } }
        Spacer(Modifier.height(20.dp))

        // ── CheckBox ─────────────────────────────────────────────────────────
        Tag("CheckBox", "android.widget.CheckBox  (pick many)", Carbon.Green50)
        topicOptions.forEach { t ->
            CarbonCheck(t, t in topics) { on -> if (on) topics.add(t) else topics.remove(t) }
        }
        Spacer(Modifier.height(16.dp))

        CarbonField(notes, { notes = it }, "Anything else? (optional)", minLines = 3)
        Spacer(Modifier.height(8.dp))
        CarbonCheck("I agree to be contacted about this appointment", consent) { consent = it }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Carbon.Red60, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        }
        Spacer(Modifier.height(14.dp))

        // ── Button ───────────────────────────────────────────────────────────
        Tag("Button", "android.widget.Button", Carbon.Blue70)
        CarbonButton("Book appointment", Carbon.Green50) {
            error = when {
                name.isBlank()                    -> "Please enter your name."
                county.isBlank()                  -> "Please choose your county."
                date.isEmpty() || time.isEmpty()  -> "Please pick a date and a time."
                topics.isEmpty()                  -> "Choose at least one topic."
                !consent                          -> "Please tick the consent box."
                else                              -> ""
            }
            if (error.isEmpty()) {
                db.addAppointment(
                    name.trim(), counsellor, county, date, time, mode,
                    topics.joinToString(", "), notes.trim()
                )
                confirmation = "Booked: $counsellor on $date at $time ($mode), $county."
                appointments = db.appointments()
                name = ""; county = ""; date = ""; time = ""
                notes = ""; consent = false; topics.clear()
            }
        }

        if (confirmation.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            CarbonCard(Carbon.Green50) {
                Text("✅ Appointment saved", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(confirmation, color = Carbon.Gray10, fontSize = 13.sp)
            }
        }

        // ── Saved appointments ───────────────────────────────────────────────
        Spacer(Modifier.height(28.dp))
        Text("Your appointments", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(8.dp))
        if (appointments.isEmpty()) {
            Text("None yet. Book one above.", color = Carbon.Gray30)
        }
        appointments.forEach { a ->
            CarbonCard(Carbon.Orange60) {
                Text(a.counsellor, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${a.date}  ·  ${a.time}  ·  ${a.mode}", color = Carbon.Cyan50, fontSize = 13.sp)
                Text("${a.name}, ${a.county}", color = Carbon.Gray30, fontSize = 13.sp)
                Text("Topics: ${a.topics}", color = Carbon.Gray10, fontSize = 13.sp)
                Row {
                    TextButton(onClick = { addToCalendar(ctx, a) }) {
                        Text("Add to calendar", color = Carbon.Blue40, fontSize = 13.sp)
                    }
                    TextButton(onClick = {
                        db.deleteAppointment(a.id)
                        appointments = db.appointments()
                    }) {
                        Text("Cancel", color = Carbon.Red60, fontSize = 13.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        Disclaimer()
    }
}

private fun addToCalendar(ctx: android.content.Context, a: Appointment) {
    val d = a.date.split("-")
    val t = a.time.split(":")
    val start = Calendar.getInstance().apply {
        set(d[0].toInt(), d[1].toInt() - 1, d[2].toInt(), t[0].toInt(), t[1].toInt(), 0)
    }.timeInMillis
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, "Counselling: ${a.counsellor}")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, start + 3_600_000L)
    }
    safeStart(ctx, intent)
}
