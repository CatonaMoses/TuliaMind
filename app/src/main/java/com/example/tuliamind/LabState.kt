package com.example.tuliamind

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Shared state so Activities, the Service and the UI can talk to each other. */
object LabState {
    val lifecycleLog = mutableStateListOf<String>()
    val timerSeconds = mutableStateOf(0)
    val timerRunning = mutableStateOf(false)

    fun log(message: String) {
        val stamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        lifecycleLog.add(0, "$stamp  $message")
        if (lifecycleLog.size > 30) lifecycleLog.removeAt(lifecycleLog.size - 1)
    }
}
