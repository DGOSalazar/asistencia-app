package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetAllAttendanceDaysByMonthUseCase @Inject constructor(
    private val firebaseServices: FirebaseServices)
{
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(
        success:(List<AttendanceDays>) -> Unit,
        errorObserver:(String) -> Unit
    ){
        firebaseServices.getAllRegistersDays(
            errorObserver = errorObserver,
            success = success
        )
    }
}