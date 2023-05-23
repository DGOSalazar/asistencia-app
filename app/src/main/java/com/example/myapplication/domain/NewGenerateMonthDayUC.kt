package com.example.myapplication.domain

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.Constants.DAY_PER_MONTH
import com.example.myapplication.core.utils.Constants.FREE_PLACES_VALUE
import com.example.myapplication.core.utils.Constants.RANGE_DAYS_TO_REGISTER
import com.example.myapplication.core.utils.MonthType
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.models.CalendarDay
import com.example.myapplication.sys.utils.Tools
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NewGenerateMonthDayUC @Inject constructor() {

    private lateinit var calendar: Calendar
    private var currentMonth:Int = 0
    private var currentYear:Int = 0
    private var placesAvailable: Int = RANGE_DAYS_TO_REGISTER
    private var nMonthType: MonthType = MonthType.CURRENT
    private var nCountDays:Int=0
    @SuppressLint("SimpleDateFormat")
    private val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var attendanceDays: List<DayCollection>


    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        days: List<DayCollection>,
        monthType: MonthType,
        countDays:Int
    ): ArrayList<CalendarDay> = run {
        nCountDays = countDays
        placesAvailable = RANGE_DAYS_TO_REGISTER
        nMonthType = monthType
        setCalendarDate()
        attendanceDays = days

        val allDays = arrayListOf<CalendarDay>()
        val daysOfCurrentMonth: ArrayList<CalendarDay> = getCurrentMonthDays()
        val daysOfLastMonth: ArrayList<CalendarDay>? = getLastMonthDays()

        if (!daysOfLastMonth.isNullOrEmpty()) allDays.addAll(daysOfLastMonth)

        allDays.addAll(daysOfCurrentMonth)

        val nextMonthDaysNumber = DAY_PER_MONTH - allDays.size

        if (allDays.size < DAY_PER_MONTH) allDays.addAll(getNextMonthDays(nextMonthDaysNumber))

        allDays
    }

    private fun setCalendarDate(){
        calendar = Calendar.getInstance()
        when(nMonthType){
            MonthType.LAST->{
                calendar.add(Calendar.MONTH, -1)
            }
            MonthType.NEXT->{
                calendar.add(Calendar.MONTH, 1)
            }
            else->{  calendar = Calendar.getInstance()}
        }
        currentMonth = calendar[Calendar.MONTH]
        currentYear = calendar[Calendar.YEAR]
        if (nMonthType == MonthType.NEXT )
            placesAvailable -= nCountDays
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentMonthDays(): ArrayList<CalendarDay> {
        calendar.set(currentYear, currentMonth, 1)
        val daysList = arrayListOf<CalendarDay>()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..daysInMonth) {
            calendar.set(currentYear, currentMonth, i)
            val dayOfWeek: Int = calendar[Calendar.DAY_OF_WEEK]
            if (dayOfWeek !in arrayOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                val date = sdf.format(calendar.time)
                daysList.add(CalendarDay(
                        date = date,
                        freePlaces = getFreePlaces(),
                        isEnable = getIsEnable(date)))
            }
        }
        return daysList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLastMonthDays(): ArrayList<CalendarDay>? {
        val daysList = arrayListOf<CalendarDay>()
        calendar.set(currentYear, currentMonth, 1)
        val firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK)

        if (firstDayOfCurrentMonth in arrayOf(Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY))
            return null
        else {
            calendar.add(Calendar.MONTH, -1)
            val daysInPastMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val lastDaysOfTheWeekOfTheLastMonth = daysInPastMonth - firstDayOfCurrentMonth + 3

            for (i in lastDaysOfTheWeekOfTheLastMonth..daysInPastMonth) {
                calendar.set(currentYear, calendar[Calendar.MONTH], i)
                val date = sdf.format(calendar.time)
                daysList.add(CalendarDay(
                    date = date,
                    freePlaces = getFreePlaces())
                )
            }
            calendar.add(Calendar.MONTH, 1)
        }
        return daysList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNextMonthDays(daysNextMonth: Int): ArrayList<CalendarDay> {
        val daysList = arrayListOf<CalendarDay>()
        calendar.add(Calendar.MONTH, 1)

        for (i in 1..daysNextMonth) {
            calendar.set(currentYear, calendar[Calendar.MONTH], i)
            val date = sdf.format(calendar.time)
            daysList.add(CalendarDay(
                date = date,
                freePlaces = getFreePlaces(), isEnable = getIsEnable(date)
            ))
        }
        calendar.add(Calendar.MONTH, -1)
        return daysList
    }

    private fun getFreePlaces():Int{
        val currentDate : String = sdf.format(calendar.time)
        var freePlaces = FREE_PLACES_VALUE
        attendanceDays.forEach { remoteDate ->
            if (remoteDate.currentDay == currentDate)
                freePlaces -= remoteDate.emails.size
        }
        return freePlaces
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIsEnable(date:String):Boolean{
        if (nMonthType == MonthType.CURRENT ){
            val today =  Tools.getTodayDate(format = true)
            val compareFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val inputDate = inputDateFormat.parse(date)
            val compareDate = compareFormat.format(inputDate!!)
            val dayType = today.compareTo(compareDate)
            val haveDaysToRegister = placesAvailable > 0
            if (placesAvailable > 0 && dayType < 0)  placesAvailable -= 1
            return dayType < 0 && haveDaysToRegister
        }else {
            return if (nMonthType == MonthType.NEXT ){
                if (placesAvailable > 0)  placesAvailable -= 1
                placesAvailable > 0
            }else
                false
        }
    }

}