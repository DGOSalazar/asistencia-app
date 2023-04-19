package com.example.myapplication.data.mappers

import com.example.myapplication.data.models.AttendanceHistoryModel
import com.example.myapplication.data.remote.request.AttendanceHistoryRegisterRequest

class AttendanceHistoryRegisterMapper : Mapper<AttendanceHistoryModel, AttendanceHistoryRegisterRequest>{
    override suspend fun map(input: AttendanceHistoryModel): AttendanceHistoryRegisterRequest {
        return AttendanceHistoryRegisterRequest(
            email = input.email!!,
            date =  input.date,
            status = input.status
        )
    }
}