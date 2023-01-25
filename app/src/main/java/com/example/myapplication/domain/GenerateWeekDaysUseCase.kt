package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.Day
import java.time.DayOfWeek
import javax.inject.Inject

class GenerateWeekDaysUseCase @Inject constructor(){

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(dayOfWeek: DayOfWeek, dayOfMonth: Int) = when(dayOfWeek.value){
        1->{
            listOf(
                Day(num = dayOfMonth, name = setSpanishDay(dayOfWeek), dayOfWeek = dayOfWeek.value, selected = true),
                Day(num = dayOfMonth+1, name = setSpanishDay(dayOfWeek+1), dayOfWeek = dayOfWeek.value+1),
                Day(num = dayOfMonth+2, name = setSpanishDay(dayOfWeek+2), dayOfWeek = dayOfWeek.value+2),
                Day(num = dayOfMonth+3, name = setSpanishDay(dayOfWeek+3), dayOfWeek = dayOfWeek.value+3),
                Day(num = dayOfMonth+4, name = setSpanishDay(dayOfWeek+4), dayOfWeek = dayOfWeek.value+4)
            )
        }
        2->{
            listOf(
                Day(num = dayOfMonth-1, name = setSpanishDay(dayOfWeek-1), dayOfWeek = dayOfWeek.value-1),
                Day(num = dayOfMonth, name = setSpanishDay(dayOfWeek), dayOfWeek = dayOfWeek.value,selected = true),
                Day(num = dayOfMonth+1, name = setSpanishDay(dayOfWeek+1), dayOfWeek = dayOfWeek.value+1),
                Day(num = dayOfMonth+2, name = setSpanishDay(dayOfWeek+2), dayOfWeek = dayOfWeek.value+2),
                Day(num = dayOfMonth+3, name = setSpanishDay(dayOfWeek+3), dayOfWeek = dayOfWeek.value+3)
            )
        }
        3->{
            listOf(
                Day(num = dayOfMonth-2, name = setSpanishDay(dayOfWeek-2), dayOfWeek = dayOfWeek.value-2),
                Day(num = dayOfMonth-1, name = setSpanishDay(dayOfWeek-1), dayOfWeek = dayOfWeek.value-1),
                Day(num = dayOfMonth, name = setSpanishDay(dayOfWeek), dayOfWeek = dayOfWeek.value,selected = true),
                Day(num = dayOfMonth+1, name = setSpanishDay(dayOfWeek+1), dayOfWeek = dayOfWeek.value+1),
                Day(num = dayOfMonth+2, name = setSpanishDay(dayOfWeek+2), dayOfWeek = dayOfWeek.value+2)
            )
        }
        4->{
            listOf(
                Day(num = dayOfMonth-3, name = setSpanishDay(dayOfWeek-3), dayOfWeek = dayOfWeek.value-3),
                Day(num = dayOfMonth-2, name = setSpanishDay(dayOfWeek-2), dayOfWeek = dayOfWeek.value-2),
                Day(num = dayOfMonth-1, name = setSpanishDay(dayOfWeek-1), dayOfWeek = dayOfWeek.value-1),
                Day(num = dayOfMonth, name = setSpanishDay(dayOfWeek), dayOfWeek = dayOfWeek.value,selected = true),
                Day(num = dayOfMonth+1, name = setSpanishDay(dayOfWeek+1), dayOfWeek = dayOfWeek.value+1)
            )
        }
        5->{
            listOf(
                Day(num = dayOfMonth-4, name = setSpanishDay(dayOfWeek-4), dayOfWeek = dayOfWeek.value-4),
                Day(num = dayOfMonth-3, name = setSpanishDay(dayOfWeek-3), dayOfWeek = dayOfWeek.value-3),
                Day(num = dayOfMonth-2, name = setSpanishDay(dayOfWeek-2), dayOfWeek = dayOfWeek.value-2),
                Day(num = dayOfMonth-1, name = setSpanishDay(dayOfWeek-1), dayOfWeek = dayOfWeek.value-1),
                Day(num = dayOfMonth, name = setSpanishDay(dayOfWeek), dayOfWeek = dayOfWeek.value,selected = true)
            )
        }
        else -> { listOf(Day()) }
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
}