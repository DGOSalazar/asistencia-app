package com.example.myapplication.data.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.Year
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class Day constructor(var num: Int=12,
                           var name: String="01",
                           var nameEng: DayOfWeek = DayOfWeek.TUESDAY,
                           var places: Int=0,
                           var freePlaces: Boolean = false,
                           var isCurrentMonth: Boolean=true,
                           var isToday: Boolean= false,
                           var selected: Boolean= false,
                           var dayOfWeek:Int=0,
                           var userList: ArrayList<User> = arrayListOf(),
                           val emails: ArrayList<String> = arrayListOf(),
                           val date: String = "",
                           val dayOfYear: Year = Year.now(),
                           var enable:Boolean = false,
                           var isWeekDay: Boolean = false
)
