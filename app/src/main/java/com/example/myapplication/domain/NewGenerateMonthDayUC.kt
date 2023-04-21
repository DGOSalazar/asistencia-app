package com.example.myapplication.domain

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.Constants.DAY_PER_MONTH
import com.example.myapplication.core.utils.Constants.FREE_PLACES_VALUE
import com.example.myapplication.data.models.NewDayModel
import com.example.myapplication.data.remote.response.AttendanceDaysResponse
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NewGenerateMonthDayUC @Inject constructor() {

    private val calendar: Calendar = Calendar.getInstance()
    private var currentMonth = calendar[Calendar.MONTH]
    private var currentYear = calendar[Calendar.YEAR]

    @SuppressLint("SimpleDateFormat")
    private val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var attendanceDays: ArrayList<AttendanceDaysResponse>
    private var userEmail: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        days: ArrayList<AttendanceDaysResponse>,
        email: String
    ): ArrayList<NewDayModel> = run {
        attendanceDays = days
        userEmail = email

        val allDays = arrayListOf<NewDayModel>()
        val daysOfCurrentMonth: ArrayList<NewDayModel> = getCurrentMonthDays()
        val daysOfLastMonth: ArrayList<NewDayModel>? = getLastMonthDays()

        if (!daysOfLastMonth.isNullOrEmpty()) allDays.addAll(daysOfLastMonth)

        allDays.addAll(daysOfCurrentMonth)

        if (allDays.size < DAY_PER_MONTH) allDays.addAll(getNextMonthDays(DAY_PER_MONTH - allDays.size))

        allDays
    }

    private fun getCurrentMonthDays(): ArrayList<NewDayModel> {
        calendar.set(currentYear, currentMonth, 1)
        val daysList = arrayListOf<NewDayModel>()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..daysInMonth) {
            calendar.set(currentYear, currentMonth, i)
            val dayOfWeek: Int = calendar[Calendar.DAY_OF_WEEK]
            if (dayOfWeek !in arrayOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                daysList.add(NewDayModel(
                        value = sdf.format(calendar.time),
                        freePlaces = getFreePlaces())
                )
            }
        }
        return daysList
    }

    private fun getLastMonthDays(): ArrayList<NewDayModel>? {
        val daysList = arrayListOf<NewDayModel>()
        calendar.set(currentYear, currentMonth, 1)
        val firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK)

        if (firstDayOfCurrentMonth in arrayOf(Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY))
            return null
        else {
            calendar.add(Calendar.MONTH, -1)
            val daysInPastMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val lastDaysOfTheWeekOfTheLastMonth = daysInPastMonth - firstDayOfCurrentMonth + 3

            for (i in lastDaysOfTheWeekOfTheLastMonth..daysInPastMonth) {
                calendar.set(currentYear, currentMonth, i)
                daysList.add(NewDayModel(
                    value = sdf.format(calendar.time),
                    freePlaces = getFreePlaces())
                )
            }
            calendar.add(Calendar.MONTH, 1)
        }
        return daysList
    }

    private fun getNextMonthDays(daysNextMonth: Int): ArrayList<NewDayModel> {
        val daysList = arrayListOf<NewDayModel>()
        calendar.add(Calendar.MONTH, 1)

        for (i in 1..daysNextMonth) {
            calendar.set(currentYear, currentMonth, i)
            daysList.add(NewDayModel(
                value = sdf.format(calendar.time),
                freePlaces = getFreePlaces())
            )
        }
        calendar.add(Calendar.MONTH, -1)
        return daysList
    }

    private fun getFreePlaces():Int{
        val currentDate : String = sdf.format(calendar.time)
        var freePlaces = FREE_PLACES_VALUE
        attendanceDays.forEach { remoteDate ->
            if (remoteDate.currentDay == currentDate)
                freePlaces -= remoteDate.email.size
        }
        return freePlaces
    }

}