package com.example.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.notification.AlarmReceiver
import com.example.myapplication.di.ReminderManagerModule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

const val PERMISSION_REQUEST_CODE = 1
const val REMINDER_NOTIFICATION_REQUEST_CODE = 123456

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationsChannels()
        RemindersManager.startReminder(this)
        checkPermission()
    }

    object RemindersManager {
        @SuppressLint("ServiceCast", "UnspecifiedImmutableFlag")
        fun startReminder(
            context: Context,
            reminderTime: String = "06:02",
            reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
        ){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val (hours, min) = reminderTime.split(":").map { it.toInt() }
            val intent =
                Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(
                        context.applicationContext,
                        reminderId,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

            val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
                set(Calendar.HOUR_OF_DAY, hours)
                set(Calendar.MINUTE, min)
            }
            if (Calendar.getInstance(Locale.ENGLISH)
                    .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
            ) {
                calendar.add(Calendar.DATE, 1)
            }
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
                intent
            )
        }

        fun stopReminder(
            context: Context,
            reminderId: Int = ReminderManagerModule.REMINDER_NOTIFICATION_REQUEST_CODE
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    context,
                    reminderId,
                    intent,
                    0
                )
            }
            alarmManager.cancel(intent)
        }
    }
    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.reminders_notification_channel_id),
                getString(R.string.reminders_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
    private fun checkPermission() {
        if (isPermissionGranted()) {
            requestInternetPermission()
        }
    }
    private fun requestInternetPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            toast(getString(R.string.recordatory))
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    toast(getString(R.string.accept_permission))
                }else{
                    toast(getString(R.string.dennied_permission))
                }
            }
            else->{
                toast(getString(R.string.dont_accept_message))
            }
        }
    }
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        ) != PackageManager.PERMISSION_GRANTED
    }
}