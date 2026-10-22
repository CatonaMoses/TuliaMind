package com.example.tuliamind

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.ui.theme.Carbon
import com.example.tuliamind.ui.theme.TuliaMindTheme

/**
 * A SECOND ACTIVITY, started with an EXPLICIT Intent from the Android Lab.
 * It reads the data (extras) that the first Activity put inside the Intent.
 */
class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LabState.log("AboutActivity.onCreate()")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        val from = intent.getStringExtra("from") ?: "unknown"
        val note = intent.getStringExtra("note") ?: ""

        setContent {
            TuliaMindTheme {
                Surface(Modifier.fillMaxSize(), color = Carbon.Gray100) {
                    Column(
                        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Second Activity", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(12.dp))
                        Text("Started by: $from", fontSize = 18.sp, color = Carbon.Cyan50)
                        Spacer(Modifier.height(8.dp))
                        Text("Message in the Intent: $note", color = Carbon.Gray30)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "This screen is a separate Activity declared in AndroidManifest.xml. " +
                                "Android keeps a back stack of Activities: closing this one returns you to the Lab. " +
                                "Check the lifecycle log there to see onPause, onStop, onStart and onResume fire.",
                            color = Carbon.Gray10
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { finish() },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Carbon.Blue60, contentColor = Color.White)
                        ) { Text("Close this Activity") }
                    }
                }
            }
        }
    }

    override fun onStart() { super.onStart(); LabState.log("AboutActivity.onStart()") }
    override fun onStop() { LabState.log("AboutActivity.onStop()"); super.onStop() }
    override fun onDestroy() { LabState.log("AboutActivity.onDestroy()"); super.onDestroy() }
}
