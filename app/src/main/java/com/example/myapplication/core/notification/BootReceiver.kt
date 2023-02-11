package com.example.myapplication.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.ui.MainActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            MainActivity.RemindersManager.startReminder(context)
        }
    }
}