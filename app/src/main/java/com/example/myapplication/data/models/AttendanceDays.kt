package com.example.myapplication.data.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
data class AttendanceDays  constructor(
    val emails:ArrayList<String>,
    val currentDay:String,
    val currentDayEng:DayOfWeek = DayOfWeek.FRIDAY,
    var day:Int=0,
    var freePlaces:Int=0
)
