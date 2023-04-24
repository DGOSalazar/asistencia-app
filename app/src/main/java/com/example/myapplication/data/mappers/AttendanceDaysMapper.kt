package com.example.myapplication.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.remote.response.AttendanceDaysResponse

class AttendanceDaysMapper : Mapper<AttendanceDaysResponse,AttendanceDays> {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun map(input: AttendanceDaysResponse): AttendanceDays {
       return AttendanceDays(
           emails = input.email,
           currentDay = input.currentDay
       )
    }
}