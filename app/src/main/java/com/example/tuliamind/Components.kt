package com.example.tuliamind

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.ui.theme.Carbon

// ─── Non-UI helpers ──────────────────────────────────────────────────────────

/** Starts an Intent safely; shows a Toast if no handler exists. */
fun safeStart(ctx: Context, intent: Intent) {
    try { ctx.startActivity(intent) }
    catch (_: Exception) {
        Toast.makeText(ctx, "No app found to handle this action", Toast.LENGTH_SHORT).show()
    }
}

/** Opens the phone dialler pre-filled with [number]. */
fun dial(ctx: Context, number: String) =
    safeStart(ctx, Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))

// ─── Top bar ─────────────────────────────────────────────────────────────────

@Composable
fun TopBar(title: String, onBack: () -> Unit) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF0D1F3C), Carbon.Gray90)))
                .statusBarsPadding()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(36.dp)
                        .background(
                            Brush.linearGradient(listOf(Carbon.Blue60, Carbon.Purple60)),
                            RoundedCornerShape(4.dp)
                        )
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(14.dp))
                Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(listOf(Carbon.Blue60, Carbon.Purple60, Carbon.Teal50))
                )
        )
    }
}

// ─── Screen scaffold ─────────────────────────────────────────────────────────

@Composable
fun ScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxSize().background(Carbon.Gray100)) {
        TopBar(title, onBack)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            content = content
        )
    }
}

// ─── Button ──────────────────────────────────────────────────────────────────

@Composable
fun CarbonButton(
    text: String,
    color: Color = Carbon.Blue60,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

// ─── Card ────────────────────────────────────────────────────────────────────

@Composable
fun CarbonCard(
    accent: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C2E), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .drawBehind { drawRect(accent, size = Size(size.width, 4.dp.toPx())) }
            .padding(16.dp),
        content = content
    )
}

// ─── Text field ──────────────────────────────────────────────────────────────

@Composable
fun carbonFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Carbon.Blue40,
    unfocusedBorderColor = Carbon.Gray70,
    focusedTextColor = Color.White,
    unfocusedTextColor = Carbon.Gray10,
    focusedLabelColor = Carbon.Blue40,
    unfocusedLabelColor = Carbon.Gray30,
    cursorColor = Carbon.Blue40,
    focusedContainerColor = Carbon.Gray85,
    unfocusedContainerColor = Carbon.Gray90
)

@Composable
fun CarbonField(value: String, onChange: (String) -> Unit, label: String, minLines: Int = 1) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        minLines = minLines,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = carbonFieldColors()
    )
}

// ─── Misc UI helpers ─────────────────────────────────────────────────────────

@Composable
fun Disclaimer() {
    Spacer(Modifier.height(8.dp))
    Text(
        "⚠  TuliaMind is a self-care tool. It does not diagnose or replace a doctor, " +
            "counsellor or emergency service.",
        fontSize = 11.sp,
        color = Carbon.Gray70,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Spacer(Modifier.height(8.dp))
}

/** Coloured pill label that names the UI/Android component shown on screen. */
@Composable
fun Tag(name: String, classic: String, color: Color = Carbon.Blue60) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            name.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(color, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(classic, fontSize = 11.sp, color = Carbon.Gray30, fontFamily = FontFamily.Monospace)
    }
}
