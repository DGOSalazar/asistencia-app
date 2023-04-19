package com.example.myapplication.domain

import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.AttendanceHistoryModel
import com.example.myapplication.data.remote.response.DayCollectionResponse
import javax.inject.Inject

class ShowOrHideAttendanceButton @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(currentDate:String, email:String) = run {
        val datesResponse:ResponseStatus<ArrayList<DayCollectionResponse>> = repository.doGetAttendanceDates()
        if (datesResponse is ResponseStatus.Success){
            val historyResponse:ResponseStatus<ArrayList<AttendanceHistoryModel>> = repository.doGetAttendanceHistory()

            if (historyResponse is ResponseStatus.Success){
                var isRegisterAttendance = false
                if (historyResponse.data.isNotEmpty()){
                    isRegisterAttendance = historyResponse.data.any{ attendanceHistory ->
                        attendanceHistory.date == currentDate && attendanceHistory.email == email
                    }
                }
                var isAttendanceDay = false
                if (datesResponse.data.isNotEmpty()){
                    isAttendanceDay = datesResponse.data.any { date ->
                        val isEmail = if (date.email != null ) date.email.any { it == email } else false
                        date.currentDay == currentDate && isEmail
                    }
                }
                ResponseStatus.Success(isAttendanceDay && !isRegisterAttendance )
            }else
                ResponseStatus.Error((historyResponse as ResponseStatus.Error).messageId)
        }else
            ResponseStatus.Error((datesResponse as ResponseStatus.Error).messageId)

    }


}