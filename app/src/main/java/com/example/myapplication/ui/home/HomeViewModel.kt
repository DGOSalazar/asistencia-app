package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Month
import com.example.myapplication.domain.GenerateMonthDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val generateMonthDaysUseCase: GenerateMonthDaysUseCase):ViewModel() {

    private val _userData = MutableLiveData<List<Int>>()
    var userData : LiveData<List<Int>> = _userData

    private val _currentMonth = MutableLiveData<Month>()
    var currentMonth : LiveData<Month> = _currentMonth


    fun getUserDate(){
        viewModelScope.launch {
            _userData.value = listOf(1, 4, 5, 10, 11, 20, 23, 28 )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendarDays(localDate: LocalDate, pastDate:LocalDate){
        _currentMonth.value = generateMonthDaysUseCase.invoke(localDate, pastDate)
    }

}