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
    operator fun invoke(day:Day): List<Day> {
        val dayOfMonth = day.num
        val dayOfWeek = day.nameEng
        val monthSelected = localDate.month
        return when (dayOfWeek.value) {
            1 -> {
                listOf(
                    Day(
                        num = dayOfMonth,
                        name = setSpanishDay(dayOfWeek),
                        dayOfWeek = dayOfWeek.value,
                        selected = true,
                        date = getFormatDate(dayOfMonth,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 1,
                        name = setSpanishDay(dayOfWeek + 1),
                        dayOfWeek = dayOfWeek.value + 1,
                        date = getFormatDate(dayOfMonth+1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 2,
                        name = setSpanishDay(dayOfWeek + 2),
                        dayOfWeek = dayOfWeek.value + 2,
                        date = getFormatDate(dayOfMonth+2,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 3,
                        name = setSpanishDay(dayOfWeek + 3),
                        dayOfWeek = dayOfWeek.value + 3,
                        date = getFormatDate(dayOfMonth+3,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 4,
                        name = setSpanishDay(dayOfWeek + 4),
                        dayOfWeek = dayOfWeek.value + 4,
                        date = getFormatDate(dayOfMonth+4,monthSelected.value)
                    )
                )
            }
            2 -> {
                listOf(
                    Day(
                        num = dayOfMonth - 1,
                        name = setSpanishDay(dayOfWeek - 1),
                        dayOfWeek = dayOfWeek.value - 1,
                        date = getFormatDate(dayOfMonth-1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth,
                        name = setSpanishDay(dayOfWeek),
                        dayOfWeek = dayOfWeek.value,
                        selected = true,
                        date = getFormatDate(dayOfMonth,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 1,
                        name = setSpanishDay(dayOfWeek + 1),
                        dayOfWeek = dayOfWeek.value + 1,
                        date = getFormatDate(dayOfMonth+1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 2,
                        name = setSpanishDay(dayOfWeek + 2),
                        dayOfWeek = dayOfWeek.value + 2,
                        date = getFormatDate(dayOfMonth+2,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 3,
                        name = setSpanishDay(dayOfWeek + 3),
                        dayOfWeek = dayOfWeek.value + 3,
                        date = getFormatDate(dayOfMonth+3,monthSelected.value)
                    )
                )
            }
            3 -> {
                listOf(
                    Day(
                        num = dayOfMonth - 2,
                        name = setSpanishDay(dayOfWeek - 2),
                        dayOfWeek = dayOfWeek.value - 2,
                        date = getFormatDate(dayOfMonth-2,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 1,
                        name = setSpanishDay(dayOfWeek - 1),
                        dayOfWeek = dayOfWeek.value - 1,
                        date = getFormatDate(dayOfMonth-1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth,
                        name = setSpanishDay(dayOfWeek),
                        dayOfWeek = dayOfWeek.value,
                        selected = true,
                        date = getFormatDate(dayOfMonth,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 1,
                        name = setSpanishDay(dayOfWeek + 1),
                        dayOfWeek = dayOfWeek.value + 1,
                        date = getFormatDate(dayOfMonth+1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 2,
                        name = setSpanishDay(dayOfWeek + 2),
                        dayOfWeek = dayOfWeek.value + 2,
                        date = getFormatDate(dayOfMonth+2,monthSelected.value)
                    )
                )
            }
            4 -> {
                listOf(
                    Day(
                        num = dayOfMonth - 3,
                        name = setSpanishDay(dayOfWeek - 3),
                        dayOfWeek = dayOfWeek.value - 3,
                        date = getFormatDate(dayOfMonth-3,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 2,
                        name = setSpanishDay(dayOfWeek - 2),
                        dayOfWeek = dayOfWeek.value - 2,
                        date = getFormatDate(dayOfMonth-2,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 1,
                        name = setSpanishDay(dayOfWeek - 1),
                        dayOfWeek = dayOfWeek.value - 1,
                        date = getFormatDate(dayOfMonth-1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth,
                        name = setSpanishDay(dayOfWeek),
                        dayOfWeek = dayOfWeek.value,
                        selected = true,
                        date = getFormatDate(dayOfMonth,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth + 1,
                        name = setSpanishDay(dayOfWeek + 1),
                        dayOfWeek = dayOfWeek.value + 1,
                        date = getFormatDate(dayOfMonth+1,monthSelected.value)
                    )
                )
            }
            5 -> {
                listOf(
                    Day(
                        num = dayOfMonth - 4,
                        name = setSpanishDay(dayOfWeek - 4),
                        dayOfWeek = dayOfWeek.value - 4,
                        date = getFormatDate(dayOfMonth-4,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 3,
                        name = setSpanishDay(dayOfWeek - 3),
                        dayOfWeek = dayOfWeek.value - 3,
                        date = getFormatDate(dayOfMonth-3,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 2,
                        name = setSpanishDay(dayOfWeek - 2),
                        dayOfWeek = dayOfWeek.value - 2,
                        date = getFormatDate(dayOfMonth-2,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth - 1,
                        name = setSpanishDay(dayOfWeek - 1),
                        dayOfWeek = dayOfWeek.value - 1,
                        date = getFormatDate(dayOfMonth-1,monthSelected.value)
                    ),
                    Day(
                        num = dayOfMonth,
                        name = setSpanishDay(dayOfWeek),
                        dayOfWeek = dayOfWeek.value,
                        selected = true,
                        date = getFormatDate(dayOfMonth,monthSelected.value)
                    )
                )
            }
            else -> {
                listOf(Day())
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