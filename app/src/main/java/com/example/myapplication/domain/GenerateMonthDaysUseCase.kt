package com.example.myapplication.domain

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import com.example.myapplication.ui.home.NEXT_MONTH
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GenerateMonthDaysUseCase @Inject constructor() {

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        currentDate: LocalDate,
        pastDate: LocalDate,
        attendanceDays: List<AttendanceDays>,
        isNextMonth:  Int
    ): Month {
        val monthSelected = currentDate.month
        val currentDay = LocalDate.now().dayOfMonth
        val currentMonth = LocalDate.now().month

        var todayValue = 0
        var todayPosition = 0
        var pastToday = false
        var countEnableDays = 0

        val pastMonthDays = YearMonth.from(pastDate).lengthOfMonth()
        val daysOfMonth = YearMonth.from(currentDate).lengthOfMonth()
        val dayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value


        val tempDays: ArrayList<Day> = arrayListOf()
        val sunDaysAndSaturdays = arrayOf(1,7,8,14,15,21,22,28,29,35,36,42)

        val pastMonthDaysList = getDaysToAttend(attendanceDays, pastDate)
        val currentMonthDaysList = getDaysToAttend(attendanceDays, currentDate)
        val nextDate = currentDate.plusMonths(1)
        val nextMonthDaysList = getDaysToAttend(attendanceDays, nextDate)

        for(i in 1..42){
            val isSundayOrSaturday = sunDaysAndSaturdays.any{ it == i }
            val day = i-dayOfWeek

            if(!isSundayOrSaturday) {   //add days if is not weekend
                if (i <= dayOfWeek || i> daysOfMonth + dayOfWeek ){
                    if(dayOfWeek !in 6..7 && i <= dayOfWeek){   //add past month days only if the week of current month is not start on monday
                        val dia =  pastMonthDays - dayOfWeek + i
                        var freePlaces = 15

                        pastMonthDaysList.forEach {  day->
                            if (day.day == pastMonthDays - dayOfWeek + i)
                                freePlaces = day.freePlaces
                        }
                        tempDays.add(
                            Day(
                            num = dia,
                            nameEng = pastDate.withDayOfMonth(dia).dayOfWeek,
                            isCurrentMonth = false,
                            freePlaces = false,
                            dayOfWeek = dayOfWeek,
                            date = getFormatDate(day,monthSelected.value),
                            places = freePlaces)
                        )
                    }
                }else{
                    val day = i-dayOfWeek

                    if (day == currentDay && monthSelected == currentMonth){        //set today
                        todayValue = day
                        todayPosition = i
                        if (!pastToday) pastToday = true
                        tempDays.add( Day(num = day, isToday = true, enable = true,
                            nameEng = currentDate.withDayOfMonth(day).dayOfWeek,
                            freePlaces = true,
                            date = getFormatDate(day,monthSelected.value)))
                    }
                    else{
                        if (pastToday) countEnableDays  += 1
                        val dayEnable = if(isNextMonth == NEXT_MONTH) false else pastToday && countEnableDays <= 15
                        var freePlaces = 15

                        currentMonthDaysList.forEach {
                             if( it.day == day ) freePlaces =it.freePlaces
                        }
                        tempDays.add( Day(num = day, places = freePlaces,
                            nameEng = currentDate.withDayOfMonth(day).dayOfWeek,
                            enable = dayEnable,freePlaces = true,
                            date = getFormatDate(day,monthSelected.value)) )
                    }
                }
            }

            if (day == currentDay && monthSelected == currentMonth){        //set today
                todayValue = day
                todayPosition = i
                if (!pastToday) pastToday = true
            }
        }
        val daysLeftToEnable = 15-countEnableDays
        val selectedDays = selectDays(tempDays,nextMonthDaysList, daysLeftToEnable)
        var daysToFormatNextMonth = 0

        if (todayPosition + 16 < selectedDays.size)
            daysToFormatNextMonth = 15 - selectedDays.size - (todayPosition + 1)

        return Month(
            daysList = selectedDays,
            today = todayValue,
            daysToFormatNextMonth = daysToFormatNextMonth
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectDays(
        dayList: ArrayList<Day>,
        nextMonthDaysList: List<AttendanceDays>,
        daysLeftToEnable: Int
    ) :ArrayList<Day>{
        var temDaysLeftToEnable = daysLeftToEnable

        if (dayList.size < 25){
            var freePlaces = 15
            var date = ""

            for (day in 1..(25-dayList.size)){
                nextMonthDaysList.forEach{
                    if(it.day == day){
                        freePlaces = it.freePlaces
                        date = it.currentDay
                    }
                }
                dayList.add(Day(
                    num = day, isCurrentMonth = false,
                    places = freePlaces, enable = temDaysLeftToEnable > 0, date = date))
                temDaysLeftToEnable-=1
            }
        }else
            for (i in 1..(dayList.size-25)){
                dayList.removeLast()
            }
        return dayList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysToAttend(attendanceDays: List<AttendanceDays>, date:LocalDate):List<AttendanceDays>{
        val filterList = mutableListOf<AttendanceDays>()

        attendanceDays.forEach { day ->
            val currentDate = date.toString().split("-")
            val dayDate = day.currentDay.split("-")
            val dayMonth = dayDate[1]
            val currentMonth = currentDate[1]

            if (currentMonth == dayMonth) {
                day.freePlaces = 15 - day.emails.size
                day.day = dayDate[0].toInt()
                filterList.add(day)
            }
        }
        return filterList
    }
    private fun getFormatDate(dayMonth: Int, month:Int): String = "${dayMonth}-0${month}-2023"
}