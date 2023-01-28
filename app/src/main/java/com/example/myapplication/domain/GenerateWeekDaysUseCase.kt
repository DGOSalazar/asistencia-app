package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.Day
import java.time.DayOfWeek
import java.time.LocalDate
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
                for (i in 0..4) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek+i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i==0,
                            date = getFormatDate(dayOfMonth+i, monthSelected.value)
                        )
                    )
                }
                dayList
            }
            2 -> {
                for (i in -1..3) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek+i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i==0,
                            date = getFormatDate(dayOfMonth+i, monthSelected.value)
                        )
                    )
                }
                dayList
            }
            3 -> {
                for (i in -2..2) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek+i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i==0,
                            date = getFormatDate(dayOfMonth+i, monthSelected.value)
                        )
                    )
                }
                dayList
            }
            4 -> {
                for (i in -3..1) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek+i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i==0,
                            date = getFormatDate(dayOfMonth+i, monthSelected.value)
                        )
                    )
                }
                dayList
            }
            5 -> {
                for (i in -4..0) {
                    dayList.add(
                        Day(
                            num = dayOfMonth + i,
                            name = setSpanishDay(dayOfWeek+i.toLong()),
                            dayOfWeek = dayOfWeek.value + i,
                            selected = i==0,
                            date = getFormatDate(dayOfMonth+i, monthSelected.value)
                        )
                    )
                }
                dayList
            }
            else -> {
                arrayListOf(Day())
            }
        }
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
    private fun getFormatDate(dayMonth: Int, month:Int): String = "${dayMonth}-0${month}-2023"
}