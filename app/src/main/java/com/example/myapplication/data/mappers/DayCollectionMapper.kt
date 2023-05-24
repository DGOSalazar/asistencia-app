package com.example.myapplication.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.remote.response.DayCollectionResponse

class DayCollectionMapper : Mapper<DayCollectionResponse, DayCollection> {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun map(input: DayCollectionResponse): DayCollection {
        return DayCollection(
            emails = input.email?: arrayListOf(),
            currentDay = input.currentDay?:""
        )
    }
}