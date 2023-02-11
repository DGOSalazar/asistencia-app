package com.example.myapplication.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.di.ReminderManagerModule
import com.example.myapplication.ui.MainActivity
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            applicationContext = context,
            channelId = context.getString(R.string.reminders_notification_channel_id)
        )
        MainActivity.RemindersManager.startReminder(context.applicationContext)
    }

    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        channelId: String,
    ) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(applicationContext.getString(R.string.title_notification_reminder))
            .setContentText(applicationContext.getString(R.string.description_notification_reminder))
            .setSmallIcon(R.drawable.icon_app)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(applicationContext.getString(R.string.description_notification_reminder))
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notify(NOTIFICATION_ID, builder.build())
    }
    companion object{
        const val NOTIFICATION_ID = 1
    }
}