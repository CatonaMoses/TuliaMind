package com.example.tuliamind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

const val ACTION_REMINDER = "com.example.tuliamind.CHECKIN_REMINDER"

/**
 * COMPONENT 3: BROADCAST RECEIVER (declared in the manifest = "static" registration).
 * Android calls onReceive() when a matching broadcast is sent.
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        LabState.log("ReminderReceiver.onReceive(${intent.action})")
        Toast.makeText(context, "Reminder: time for your mood check-in", Toast.LENGTH_LONG).show()
    }
}
