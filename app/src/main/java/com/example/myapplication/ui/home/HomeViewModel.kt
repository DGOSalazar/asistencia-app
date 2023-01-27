package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Month
import com.example.myapplication.data.models.User
import com.example.myapplication.domain.*
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAllAttendanceDaysByMonthUseCase: GetAllAttendanceDaysByMonthUseCase,
    private val getListEmailsUseCase: GetListEmailsUseCase,
    private val getUser: GetUser
    ):ViewModel() {

    private val _daySelected = MutableLiveData<String>()
    var daySelected : LiveData<String> = _daySelected

    private val _users = MutableLiveData<ArrayList<User>>()
    var users : LiveData<ArrayList<User>> = _users

    private val _userEmails = MutableLiveData<ArrayList<String>>()
    var userEmails : LiveData<ArrayList<String>> = _userEmails

    private val _userData = MutableLiveData<List<AttendanceDays>>()
    var userData : LiveData<List<AttendanceDays>> = _userData

    private val _currentMonth = MutableLiveData<Month>()
    var currentMonth : LiveData<Month> = _currentMonth

    private val _weekSelected = MutableLiveData<List<Day>>()
    var weekSelected : LiveData<List<Day>> = _weekSelected


    fun addUserToDay(){
        enrollUserToDayUseCase(_daySelected.value!!, _userEmails.value!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setWeekList(day:Day){
        _weekSelected.value = generateWeekDaysUseCase(day)
    }
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

    fun setDay(p: String){
        _daySelected.value = p
    }

    fun getUserDatastore(listEmails:ArrayList<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getUserInfoUseCase(listEmails){
                    _users.postValue(it)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getListEmails(day: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getListEmailsUseCase(day){
                    _userEmails.postValue(it)
                }
            }
        }
    }

    fun enrollUser(date: String, emails: ArrayList<String>){
        viewModelScope.launch {
            enrollUserToDayUseCase(date,emails)
        }
    }

    fun addUserToListUsers(email: String) {
        _userEmails.value!!.add(email)
    }
    fun getEmail()  = getUser()
}