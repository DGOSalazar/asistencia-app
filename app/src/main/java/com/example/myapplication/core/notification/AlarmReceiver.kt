package com.example.myapplication.core.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myapplication.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 1
    }

    @SuppressLint("ResourceType")
    override fun onReceive(context: Context, intent: Intent) {
        simpleNotification(context)
    }

    private fun simpleNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_add)
            .setContentTitle("Aviso")
            .setContentText("No olvides registrar tu asistencia para la siguiente semana")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Recuerda que ahora deberas anotarte por lo menos 2 dias para asistir en oficina, no dejes que te ganen tus favoritos!")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}