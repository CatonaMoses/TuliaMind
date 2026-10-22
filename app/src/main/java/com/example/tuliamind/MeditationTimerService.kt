package com.example.tuliamind

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

/**
 * COMPONENT 2: SERVICE.
 * A STARTED service: it keeps counting seconds even when the user leaves the screen.
 * Started with startService(), stopped with stopService().
 */
class MeditationTimerService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    private val tick = object : Runnable {
        override fun run() {
            LabState.timerSeconds.value = LabState.timerSeconds.value + 1
            handler.postDelayed(this, 1000)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!LabState.timerRunning.value) {
            LabState.timerRunning.value = true
            LabState.log("MeditationTimerService.onStartCommand()")
            handler.postDelayed(tick, 1000)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(tick)
        LabState.timerRunning.value = false
        LabState.log("MeditationTimerService.onDestroy()")
        super.onDestroy()
    }
}
