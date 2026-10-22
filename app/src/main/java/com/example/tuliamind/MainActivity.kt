package com.example.tuliamind

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tuliamind.ui.theme.Carbon
import com.example.tuliamind.ui.theme.TuliaMindTheme

/**
 * COMPONENT 1: ACTIVITY.
 * The single entry screen of the app. Every Compose screen is shown inside it.
 * The lifecycle callbacks below write to LabState so the Android Lab screen can display them.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LabState.log("MainActivity.onCreate()")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent {
            TuliaMindTheme {
                Surface(Modifier.fillMaxSize(), color = Carbon.Gray100) {
                    AppNav()
                }
            }
        }
    }

    override fun onStart() { super.onStart(); LabState.log("MainActivity.onStart()") }
    override fun onResume() { super.onResume(); LabState.log("MainActivity.onResume()") }
    override fun onPause() { LabState.log("MainActivity.onPause()"); super.onPause() }
    override fun onStop() { LabState.log("MainActivity.onStop()"); super.onStop() }
    override fun onDestroy() { LabState.log("MainActivity.onDestroy()"); super.onDestroy() }
}
