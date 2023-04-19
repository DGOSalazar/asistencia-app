package com.example.myapplication.domain

import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.AttendanceHistoryModel
import javax.inject.Inject

class AttendanceHistoryRegisterUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(request: AttendanceHistoryModel): ResponseStatus<Boolean> =
        repository.doAttendanceHistoryRegister(request)

}