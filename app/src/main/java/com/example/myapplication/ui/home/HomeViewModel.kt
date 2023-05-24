package com.example.myapplication.ui.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.*
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.example.myapplication.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateMonthDaysUseCase: GenerateMonthDaysUseCase,
    private val generateWeekDaysUseCase: GenerateWeekDaysUseCase,
    private val enrollUserToDayUseCase: EnrollUserToDayUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAllAttendanceDaysByMonthUseCase: GetAllAttendanceDaysByMonthUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val sharePreferenceRepository: SharePreferenceRepository,
    private val validateGeolocationUseCase: ValidateGeolocationUseCase,
    private val attendanceHistoryRegisterUseCase: AttendanceHistoryRegisterUseCase,
    private val showOrHideAttendanceButton: ShowOrHideAttendanceButton,
    private val newGenerateMonthDayUC: NewGenerateMonthDayUC,
    private val userHomeRepository: UserHomeRepository
) : ViewModel() {

    private val _daySelected = MutableLiveData<String>()
    var daySelected: LiveData<String> = _daySelected

    private val _users = MutableLiveData<ArrayList<UserHomeResponse>>()
    var users: LiveData<ArrayList<UserHomeResponse>> = _users

    private val _userEmails = MutableLiveData<ArrayList<String>>()
    var userEmails: LiveData<ArrayList<String>> = _userEmails

    private val _assistanceDays = MutableLiveData<List<AttendanceDays>>()
    var assistanceDays: LiveData<List<AttendanceDays>> = _assistanceDays

    private val _currentMonth = MutableLiveData<Month>()
    var currentMonth: LiveData<Month> = _currentMonth

    private val _weekSelected = MutableLiveData<List<Day>>()
    var weekSelected: LiveData<List<Day>> = _weekSelected

    private val _accountData = MutableLiveData<UserHomeResponse>()
    var accountData: LiveData<UserHomeResponse> = _accountData

    private val _loader = MutableLiveData<Boolean>()
    var loader: LiveData<Boolean> = _loader

    private val _isLoading = MutableLiveData<Status>()
    var isLoading: LiveData<Status> = _isLoading

    private val _dayObject = MutableLiveData<Day>()
    var dayObject: LiveData<Day> = _dayObject

    private val _mail = MutableLiveData<String>()
    var mail: LiveData<String> = _mail

    private val _local = MutableLiveData<ArrayList<String>>()
    var local: LiveData<ArrayList<String>> = _local

    private val _confirmOk = MutableLiveData<Boolean>()
    var confirmOk: LiveData<Boolean> = _confirmOk

    private var _status = MutableLiveData<ResponseStatus<Any>?>()
    val status: LiveData<ResponseStatus<Any>?> get() = _status


    var daysToAttend:List<AttendanceDays> = emptyList()
    var remoteDays:ArrayList<DayCollectionResponse> = arrayListOf()




    fun confirmStatus(email: String, day: String) {
        viewModelScope.launch {
            getLocationUseCase.checkConfirmStatus(day, email)
            _confirmOk.value = getLocationUseCase.getConfirm()
        }
    }

    fun setObjectDay(day: Day) {
        _dayObject.value = day
    }

    fun getDayObject(): Day {
        return dayObject.value!!
    }

    fun addUserToDay() {
        enrollUserToDayUseCase(_daySelected.value!!, _userEmails.value!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setWeekList(day: Day) {
        _weekSelected.value = generateWeekDaysUseCase(day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserDate() {
        getAllAttendanceDaysByMonthUseCase.invoke(
            errorObserver = { },
            success = { _assistanceDays.value = it }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendarDays(
        localDate: LocalDate,
        pastDate: LocalDate,
        daysToAttend: List<AttendanceDays>,
        isNextMonth: Int
    ) {
        _currentMonth.value =
            generateMonthDaysUseCase(localDate, pastDate, daysToAttend, isNextMonth)
    }

    fun setDay(p: String) {
        _daySelected.value = p
    }

    fun getUserDatastore(listEmails: ArrayList<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUserInfoUseCase.userInfo(listEmails).collect {
                    _users.postValue(it.data!!)
                    _isLoading.postValue(Status.SUCCESS)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getListEmails(day: String) {
        viewModelScope.launch {
            _isLoading.value = Status.LOADING
            withContext(Dispatchers.IO) {
                getUserInfoUseCase.emailList(day).collect {
                    _userEmails.postValue(it.data!!)
                }
            }
        }
    }

    fun addUserToListUsers(email: String) {
        _userEmails.value!!.add(email)
    }

    fun getAccountData(accountEmail: String) {
        val listEmails: ArrayList<String> = arrayListOf()
        listEmails.add(accountEmail)
        viewModelScope.launch {
            getUserInfoUseCase.userInfo(listEmails).collect {
                _accountData.postValue(it.data!![0])
            }
        }
    }

    fun deleteUserOfDay(email: String) {
        _userEmails.value!!.remove(email)
    }

    fun setEmail(email: String) {
        _mail.value = email
    }

    fun getEmail() = sharePreferenceRepository.getEmail().toString()

    //fun getEmail(): String = mail.value!!

    fun clearLiveData() {
        val clean = arrayListOf<UserHomeResponse>()
        _users.value = clean
        _userEmails.value = arrayListOf()
    }

    fun getCurrentLocation(context: Context) {
        viewModelScope.launch {
            getLocationUseCase.getLocationResult(context)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun applyAttendance() {
        viewModelScope.launch {
            _status.value = validateGeolocationUseCase.invoke() as ResponseStatus<Any>
        }
    }

    fun cleanLiveData() {
        _status.value = null
    }




}
