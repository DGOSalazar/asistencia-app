package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import com.example.myapplication.data.models.User
import com.example.myapplication.domain.EnrollUserToDayUseCase
import com.example.myapplication.domain.GenerateMonthDaysUseCase
import com.example.myapplication.domain.GenerateWeekDaysUseCase
import com.example.myapplication.domain.GetUserInfoUseCase
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateMonthDaysUseCase: GenerateMonthDaysUseCase,
    private val generateWeekDaysUseCase: GenerateWeekDaysUseCase,
    private val enrollUserToDayUseCase: EnrollUserToDayUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
    ):ViewModel() {

    private val _daySelected = MutableLiveData<Day>()
    var daySelected : LiveData<Day> = _daySelected

    private val _user = MutableLiveData<User>()
    var user : LiveData<User> = _user

    private val _userData = MutableLiveData<List<Int>>()
    var userData : LiveData<List<Int>> = _userData

    private val _currentMonth = MutableLiveData<Month>()
    var currentMonth : LiveData<Month> = _currentMonth

    private val _weekSelected = MutableLiveData<List<Day>>()
    var weekSelected : LiveData<List<Day>> = _weekSelected

    fun addUserToDay(){
        enrollUserToDayUseCase(_user.value!!.email!!,_daySelected.value!!)
    }

    fun getUserData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                 getUserInfoUseCase() {
                     _user.postValue(it)
                 }
            }
        }
    }

    fun getUserDate(){
        viewModelScope.launch {
            _userData.value = listOf(1, 4, 5, 10, 11, 20, 23, 28 )
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setWeekList(d: DayOfWeek, a: Int){
        _weekSelected.value = generateWeekDaysUseCase(d,a)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendarDays(localDate: LocalDate, pastDate:LocalDate){
        _currentMonth.value = generateMonthDaysUseCase(localDate, pastDate)
    }

    fun setDay(p: Int){
        _daySelected.value!!.dayOfWeek = p
    }
}