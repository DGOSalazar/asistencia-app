package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.network.FirebaseServices
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetAllAttendanceDaysByMonthUseCase @Inject constructor(
    private val firebaseServices: FirebaseServices)
{

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        date: LocalDate,
        success:(List<AttendanceDays>) -> Unit,
        errorObserver:(String) -> Unit
    ): MutableList<AttendanceDays> {

        val month = date.month
        val allDays:List<AttendanceDays> = firebaseServices.getAllRegistersDays(
            errorObserver = errorObserver,
            success = success
        )

        val filterDays = mutableListOf<AttendanceDays>()

        allDays.forEach{ day ->

            val dayDate = LocalDate.parse(day.currentDay)
            val dayMonth = dayDate.month

            if (month == dayMonth){
                day.freePlaces = 15 - day.emails.size
                day.day = dayDate.dayOfMonth
                filterDays.add(day)
            }
        }
        return filterDays
    }

}