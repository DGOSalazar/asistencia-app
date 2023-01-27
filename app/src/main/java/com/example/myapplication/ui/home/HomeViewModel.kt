package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Month
import com.example.myapplication.domain.GenerateMonthDaysUseCase
import com.example.myapplication.domain.GetAllAttendanceDaysByMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateMonthDaysUseCase: GenerateMonthDaysUseCase,
    private val getAllAttendanceDaysByMonthUseCase: GetAllAttendanceDaysByMonthUseCase
    ) : ViewModel()
{

    private val _userData = MutableLiveData<List<AttendanceDays>>()
    var userData : LiveData<List<AttendanceDays>> = _userData

    private val _currentMonth = MutableLiveData<Month>()
    var currentMonth : LiveData<Month> = _currentMonth


    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserDate(){
        getAllAttendanceDaysByMonthUseCase.invoke(
            errorObserver = { },
            success = { _userData.value = it }
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendarDays(
        localDate: LocalDate,
        pastDate: LocalDate,
        daysToAttend: List<AttendanceDays>,
        isNextMonth:  Int
    ){
        _currentMonth.value = generateMonthDaysUseCase.invoke( localDate, pastDate, daysToAttend, isNextMonth)
    }


}