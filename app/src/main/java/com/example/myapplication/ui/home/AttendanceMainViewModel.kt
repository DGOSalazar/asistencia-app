package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.MonthType
import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.*
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.example.myapplication.domain.*
import com.example.myapplication.sys.utils.Tools
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Year
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class AttendanceMainViewModel@Inject constructor(
    private val userHomeRepository: UserHomeRepository,
    private val sharePreferenceRepository: SharePreferenceRepository,
    private val newGenerateMonthDayUC: NewGenerateMonthDayUC,
    private val showOrHideAttendanceButton: ShowOrHideAttendanceButton,
    private val validateGeolocationUseCase: ValidateGeolocationUseCase,
    private val attendanceHistoryRegisterUseCase: AttendanceHistoryRegisterUseCase
):ViewModel() {

    var user: UserHomeDomainModel? = UserHomeDomainModel()
    var userList: List<UserHomeResponse>? = arrayListOf()
    var remoteDays: List<DayCollection>? = arrayListOf()
    var calendarDay: ArrayList<CalendarDay> = arrayListOf()
    var countDays:Int = 0



    private var _initialHomeDataLiveData = MutableLiveData<Resource2<HomeInitialData>>()
    val initialHomeDataLiveData: LiveData<Resource2<HomeInitialData>> get() = _initialHomeDataLiveData

    private var _calendarDays = MutableLiveData<ArrayList<CalendarDay>>()
    val calendarDays: LiveData<ArrayList<CalendarDay>> get() = _calendarDays

    private var _showOrHideAttendanceBtn = MutableLiveData<ResponseStatus<Boolean>>()
    val showOrHideAttendanceBtn: LiveData<ResponseStatus<Boolean>> get() = _showOrHideAttendanceBtn

    private var _statusGeolocation = MutableLiveData<ResponseStatus<Boolean>?>()
    val statusGeolocation: LiveData<ResponseStatus<Boolean>?> get() = _statusGeolocation

    private var _statusHistoryRegister = MutableLiveData<ResponseStatus<Boolean>?>()
    val statusHistoryRegister: LiveData<ResponseStatus<Boolean>?> get() = _statusHistoryRegister



    @RequiresApi(Build.VERSION_CODES.O)
    fun getHomeData(day: String, email:String){
        _initialHomeDataLiveData.value = Resource2.loading()
        viewModelScope.launch(Dispatchers.Main) {
            userHomeRepository.getHomeInitialData(email = email, date = day).collect{
                _initialHomeDataLiveData.value = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showOrHideAttendanceButton() {
        viewModelScope.launch {
            _showOrHideAttendanceBtn.value =
                showOrHideAttendanceButton.invoke(email = getEmail(), currentDate = Tools.getTodayDate())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCalendarDays(monthType: MonthType){
        _calendarDays.value = newGenerateMonthDayUC.invoke(getAttendanceDays(), monthType, countDays)
    }

    @Suppress("UNCHECKED_CAST")
    fun applyAttendance() {
        viewModelScope.launch {
            _statusGeolocation.value = validateGeolocationUseCase.invoke()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    fun registerHistoryAttendance() {
        viewModelScope.launch {
            val request = AttendanceHistoryModel(getEmail(), Tools.getTodayDate() , 2)
            _statusHistoryRegister.value = attendanceHistoryRegisterUseCase.invoke(request)
        }
    }

    fun getEmail() = sharePreferenceRepository.getEmail().toString()

    fun getAttendanceDays(): List<DayCollection> {
        return  remoteDays!!.filter{ day -> day.emails.any{ email -> email == getEmail() }}
    }

    fun <T> validateStatus(data:Resource2<T>?, action: () -> Unit){
        if(data!!.status == Status.SUCCESS){
            action()
        }else{
            _initialHomeDataLiveData.value = Resource2.error(msg = data.message?:"error")
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDay(day: CalendarDay):Day {
        return Day(
            num = day.date.substring(0,2).toInt(),
            nameEng = getNameEng(day),
            places = day.freePlaces,
            dayOfWeek = getDayOfWeek(day),
            date = day.date,
            dayOfYear =  Year.now(),
        )
    }

    private fun getNameEng(day: CalendarDay): DayOfWeek {
        val calendar = Calendar.getInstance()
        val date = day.date.split("-")
        calendar.set(date[2].toInt(), date[1].toInt()-1, date[0].toInt())
        val dayOfWeekValue = calendar[Calendar.DAY_OF_WEEK]
        return DayOfWeek.values()[dayOfWeekValue -2]
    }

    private fun getDayOfWeek(day: CalendarDay):Int{
        val calendar = Calendar.getInstance()
        val date = day.date.split("-")
        calendar.set(date[2].toInt(), date[1].toInt(), date[0].toInt())
        return calendar[Calendar.DAY_OF_WEEK]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCountDays() {
        val date = Tools.getTodayDate().split("-")
        countDays = calendarDay.filter {
            val compareDate = it.date.split("-")
            it.isEnable && date[1] == compareDate[1] //compare by month
        }.size
        countDays
    }

    fun cleanLiveData() {
        _statusGeolocation.value = null
        _statusHistoryRegister.value = null
    }

}
