package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GenerateMonthDaysUseCase @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(localDate: LocalDate, pastDate: LocalDate): Month {

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

            if(!isSundayOrSaturday) {
                if (i <= dayOfWeek || i> daysOfMonth + dayOfWeek ){
                    if(dayOfWeek !in 6..7 ){
                        if(i <= dayOfWeek){
                            tempDays.add(Day(num = pastMonthDays - dayOfWeek + i, isCurrentMonth = false, freePlaces = false))
                        }
                    }
                }else{
                    val day = i-dayOfWeek
                    if (day == currentDay && monthSelected == currentMonth){
                        today = day
                        tempDays.add(Day(num = day, profilePhoto = "",freePlaces = true, isToday = true))
                    }
                    else{
                        if (day % 2 == 0)
                            tempDays.add(Day(num = day, profilePhoto = "",freePlaces = true, places = 12))
                        else
                            tempDays.add(Day(num = day, profilePhoto = "",freePlaces = true))
                    }
                }
            }
        }

        return Month(daysList = selectDays(tempDays), today = today, pastMonth = currentMonth > monthSelected)
    }

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
}