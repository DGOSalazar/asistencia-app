package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.Day
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

class GenerateWeekDaysUseCase @Inject constructor(){

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(day:Day): ArrayList<Day>{
        var dayList : ArrayList<Day> = arrayListOf()
        val dayOfMonth = day.num ; val dayOfWeek = day.nameEng ; val monthSelected = localDate.month
        return when (dayOfWeek.value){
            1 -> {
                generateWeek(0,4,dayOfMonth,dayOfWeek,monthSelected)
            }
            2 -> {
                generateWeek(-1,3,day.num,day.nameEng,localDate.month)
            }
            3 -> {
                generateWeek(-2,2,day.num,day.nameEng,localDate.month)
            }
            4 -> {
                generateWeek(-3,1,day.num,day.nameEng,localDate.month)
            }
            else -> {
                generateWeek(-4,0,day.num,day.nameEng,localDate.month)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateWeek(begin: Int, to :Int, dayOfMonth: Int, dayOfWeek: DayOfWeek, monthSelected: Month) : ArrayList<Day>{
        var dayList : ArrayList<Day> = arrayListOf()
        var isCurrentWeek = false
        for (i in begin..to){
            if (dayOfMonth +i<= monthSelected.length(false) && dayOfMonth+i>0) {

                if (localDate.dayOfMonth == dayOfMonth+i) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek + i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i == 0,
                            date = getFormatDate(dayOfMonth + i, monthSelected.value),
                            isWeekDay = true
                        )
                    )
                }else{
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek + i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i == 0,
                            date = getFormatDate(dayOfMonth + i, monthSelected.value)
                        )
                    )
                }

            }else{
                if ((dayOfMonth+i) <= 0){
                    dayList.add(
                        Day(
                            num = if(dayOfMonth+i == 0){(((monthSelected-1).length(false)))}
                            else{(((monthSelected).length(false))+i)},
                            name = setSpanishDay(dayOfWeek + i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i == 0,
                            date = getFormatDate(((monthSelected-1).length(false))+i, monthSelected.value).toString(),
                        )
                    )
                }
                else{
                    if ((dayOfMonth+i>monthSelected.length(false))) {
                        dayList.add(
                            Day(
                                num = (dayOfMonth-monthSelected.length(false)) + i,
                                name = setSpanishDay(dayOfWeek + i.toLong()),
                                dayOfWeek = dayOfWeek.value + i,
                                selected = i == 0,
                                date = getFormatDate((dayOfMonth-monthSelected.length(false)) + i, monthSelected.value),
                            )
                        )
                    }
                }
            }
        }
        return dayList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSpanishDay(day: DayOfWeek): String = when(day){
        DayOfWeek.MONDAY ->{"Lunes"}
        DayOfWeek.TUESDAY ->{"Martes"}
        DayOfWeek.WEDNESDAY ->{"Miercoles"}
        DayOfWeek.THURSDAY ->{"Jueves"}
        DayOfWeek.FRIDAY ->{"Viernes"}
        else->{""}
    }.toString()

    private fun getFormatDate(dayMonth: Int, month:Int): String {
        return if(dayMonth>9) "${dayMonth}-0${month}-2023" else {
            "0${dayMonth}-0${month}-2023"
        }
    }

}