package com.example.myapplication.domain

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GenerateMonthDaysUseCase @Inject constructor() {

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        localDate: LocalDate,
        pastDate: LocalDate,
        attendanceDays: List<AttendanceDays>,
    ): Month {

        val monthSelected = localDate.month
        val currentDay = LocalDate.now().dayOfMonth
        val currentMonth = LocalDate.now().month
        var today = 0
        val pastMonthDays = YearMonth.from(pastDate).lengthOfMonth()
        val daysOfMonth = YearMonth.from(localDate).lengthOfMonth()
        val dayOfWeek = localDate.withDayOfMonth(1).dayOfWeek.value

        val tempDays: ArrayList<Day> = arrayListOf()
        val sunDaysAndSaturdays= arrayOf(1,7,8,14,15,21,22,28,29,35,36,42)

        for(i in 1..42){
            val isSundayOrSaturday = sunDaysAndSaturdays.any{ it == i }
            val day = i-dayOfWeek
            if(!isSundayOrSaturday) {
                if (i <= dayOfWeek || i> daysOfMonth + dayOfWeek ){
                    if(dayOfWeek !in 6..7 ){
                        if(i <= dayOfWeek){
                            tempDays.add(Day(num = pastMonthDays - dayOfWeek + i, isCurrentMonth = false, freePlaces = false, dayOfWeek = dayOfWeek,date = getFormatDate(day,monthSelected.value)))
                        }
                    }
                }else{
                    if (day == currentDay && monthSelected == currentMonth){
                        today = day
                        tempDays.add(Day(num = day, profilePhoto = false, freePlaces = true, isToday = true, date = getFormatDate(day,monthSelected.value)))
                    }
                    else{
                        var freePlaces = 15
                            attendanceDays.forEach {
                                freePlaces = if(it.day == day )
                                    it.freePlaces
                                else
                                    15
                        }
                        tempDays.add( Day(num = day, profilePhoto = false, freePlaces = true, places = freePlaces, date = getFormatDate(day,monthSelected.value)))
                    }
                }
            }
        }
        val isPastMonth =
        if(LocalDate.now().year > localDate.year) true else currentMonth > monthSelected

        return Month(daysList = selectDays(tempDays), today = today, pastMonth = isPastMonth)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectDays(dayList: ArrayList<Day>) :ArrayList<Day>{
        if (dayList.size < 25){
            for (i in 1..(25-dayList.size)){
                dayList.add(Day(num = i, isCurrentMonth = false, freePlaces = true))
            }
        }else
            for (i in 1..(dayList.size-25)){
                dayList.removeLast()
            }
        return dayList
    }

    private fun getFormatDate(dayMonth: Int, month:Int): String = "${dayMonth}-0${month}-2023"
}