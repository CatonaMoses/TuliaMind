package com.example.tuliamind

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.ui.theme.Carbon
import kotlinx.coroutines.delay

// ─── Kenya counties for autocomplete ─────────────────────────────────────────

val KENYA_COUNTIES = listOf(
    "Baringo", "Bomet", "Bungoma", "Busia", "Elgeyo-Marakwet", "Embu", "Garissa", "Homa Bay",
    "Isiolo", "Kajiado", "Kakamega", "Kericho", "Kiambu", "Kilifi", "Kirinyaga", "Kisii",
    "Kisumu", "Kitui", "Kwale", "Laikipia", "Lamu", "Machakos", "Makueni", "Mandera",
    "Marsabit", "Meru", "Migori", "Mombasa", "Murang'a", "Nairobi", "Nakuru", "Nandi",
    "Narok", "Nyamira", "Nyandarua", "Nyeri", "Samburu", "Siaya", "Taita-Taveta", "Tana River",
    "Tharaka-Nithi", "Trans-Nzoia", "Turkana", "Uasin Gishu", "Vihiga", "Wajir", "West Pokot"
)

// ─── Staggered reveal animation ──────────────────────────────────────────────

/** Fades and slides content in with a staggered delay based on [index]. */
@Composable
fun Reveal(index: Int, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 70L)
        shown = true
    }
    val alpha by animateFloatAsState(if (shown) 1f else 0f, tween(450), label = "revealAlpha")
    val offsetY by animateFloatAsState(if (shown) 0f else 40f, tween(450), label = "revealY")
    Box(modifier.graphicsLayer { this.alpha = alpha; translationY = offsetY }) { content() }
}

// ─── Section title ───────────────────────────────────────────────────────────

@Composable
fun SectionTitle(text: String) {
    Text(
        text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Carbon.Blue40,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
    )
}

// ─── Spinner (Dropdown) ──────────────────────────────────────────────────────

/** Compose equivalent of android.widget.Spinner */
@Composable
fun CarbonDropdown(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Column {
        Text(label, fontSize = 12.sp, color = Carbon.Gray30, modifier = Modifier.padding(bottom = 4.dp))
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Carbon.Gray80, RoundedCornerShape(8.dp))
                    .clickable { open = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selected.ifEmpty { "Select…" }, color = Color.White, modifier = Modifier.weight(1f))
                Text("▾", color = Carbon.Blue40, fontSize = 16.sp)
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onSelect(option); open = false }
                    )
                }
            }
        }
    }
}

// ─── AutoComplete field ──────────────────────────────────────────────────────

/** Compose equivalent of android.widget.AutoCompleteTextView */
@Composable
fun AutoCompleteField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    suggestions: List<String>
) {
    var focused by remember { mutableStateOf(false) }
    val matches = remember(value) {
        if (value.isBlank()) emptyList()
        else suggestions.filter {
            it.contains(value, ignoreCase = true) && !it.equals(value, ignoreCase = true)
        }.take(5)
    }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focused = it.isFocused },
            colors = carbonFieldColors()
        )
        if (focused && matches.isNotEmpty()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Carbon.Gray80, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            ) {
                matches.forEach { match ->
                    Text(
                        match,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onChange(match) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    HorizontalDivider(color = Carbon.Gray70, thickness = 0.5.dp)
                }
            }
        }
    }
}

// ─── CheckBox ────────────────────────────────────────────────────────────────

/** Compose equivalent of android.widget.CheckBox */
@Composable
fun CarbonCheck(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onChange(!checked) }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Carbon.Blue60,
                uncheckedColor = Carbon.Gray30,
                checkmarkColor = Color.White
            )
        )
        Text(label, color = Color.White, fontSize = 14.sp)
    }
}

// ─── RadioButton ─────────────────────────────────────────────────────────────

/** Compose equivalent of android.widget.RadioButton inside a RadioGroup */
@Composable
fun CarbonRadio(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Carbon.Blue40,
                unselectedColor = Carbon.Gray30
            )
        )
        Text(label, color = Color.White, fontSize = 14.sp)
    }
}
