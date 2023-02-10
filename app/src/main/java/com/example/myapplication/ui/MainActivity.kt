package com.example.myapplication.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }
    object RemindersManager {
        const val REMINDER_NOTIFICATION_REQUEST_CODE = 123
        fun startReminder(
            context: Context,
            reminderTime: String = "08:00",
            reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
        ) {

        }

        fun stopReminder(
            context: Context,
            reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
        ) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val localDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createChannel()
        if (localDate.dayOfWeek == DayOfWeek.THURSDAY){
            createSimpleNotification()
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "pushNotify"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSimpleNotification() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this, 0, intent, flag)

        var builder = NotificationCompat.Builder(this, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_app)
            .setContentTitle(getString(R.string.title_notification_reminder))
            .setContentText(getString(R.string.description_notification_reminder))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}