package com.example.myapplication.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.remote.response.AttendanceDaysResponse

class CollectionDayMapper : Mapper<AttendanceDaysResponse, DayCollection> {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun map(input: AttendanceDaysResponse): DayCollection {
        return DayCollection(
            emails = input.email,
            currentDay = input.currentDay
        )
    }
}