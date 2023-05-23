package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.dialog.UserDialog
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.MonthType
import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.UserType
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.*
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.example.myapplication.databinding.FragmentAssistenceMainBinding
import com.example.myapplication.sys.utils.Tools
import com.example.myapplication.sys.utils.Tools.Companion.getTodayDate
import com.example.myapplication.ui.home.adapters.NewCalendarAdapter
import com.example.myapplication.ui.home.adapters.UserAdapter
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


const val CURRENT_MONTH = 1
const val PAST_MONTH = 2
const val NEXT_MONTH = 3


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main){

    private val viewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: AttendanceMainViewModel by viewModels()
    private lateinit var newCalendarAdapter: NewCalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding: FragmentAssistenceMainBinding


    @Inject
    lateinit var tools: Tools

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate= LocalDate.now()
    private var userData: UserHomeResponse = UserHomeResponse()
    private var statusMonthType: MonthType = MonthType.CURRENT


    override fun onPause() {
        super.onPause()
        mainViewModel.cleanLiveData()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.cleanLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAssistenceMainBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLiveData()
        newSetListeners()
        newSetCalendarAdapter()
        mainViewModel.getHomeData(day = getTodayDate(), email = mainViewModel.getEmail())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setLiveData(){
        mainViewModel.calendarDays.observe(viewLifecycleOwner){
            mainViewModel.calendarDay = it
            mainViewModel.setCountDays()
            updateCalendarDays()
        }
        mainViewModel.initialHomeDataLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as HomeActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    setView(it.data!!)
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(it.message?:"error")
                }
            }
        }
        mainViewModel.showOrHideAttendanceBtn.observe(viewLifecycleOwner){
            when (it) {
                is ResponseStatus.Loading -> {
                    if (isAdded)
                        (activity as HomeActivity).showLoader()
                }
                is ResponseStatus.Success -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    mBinding.fabConfirmAsit.visibility = if (it.data) View.VISIBLE else View.GONE
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }
        mainViewModel.statusGeolocation.observe(viewLifecycleOwner){
            if (it == null)
                return@observe
            when (it) {
                is ResponseStatus.Loading -> {
                    if (isAdded)
                        (activity as HomeActivity).showLoader()
                }
                is ResponseStatus.Success -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    if(it.data){
                        mainViewModel.registerHistoryAttendance()
                    }
                    else
                        showAlert(getString(R.string.error_local_location_not_match)){ }
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }
        mainViewModel.statusHistoryRegister.observe(viewLifecycleOwner){
            if (it == null)
                return@observe
            when (it) {
                is ResponseStatus.Loading -> {
                    if (isAdded)
                        (activity as HomeActivity).showLoader()
                }
                is ResponseStatus.Success -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    showAlert("registro exitoso"){}
                    mainViewModel.showOrHideAttendanceButton()
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCalendarDays() {
        newCalendarAdapter.apply {
            calendarDaysList = mainViewModel.calendarDay
            monthType = statusMonthType
            attendanceDaysList = mainViewModel.getAttendanceDays()
            notifyDataSetChanged()
        }
        setCalendarTitle()
    }

    private fun newSetListeners(){
        mBinding.vBack.setOnClickListener{
            changeLastMonth()
        }
        mBinding.vNext.setOnClickListener{
            changeNextMonth()
        }
        mBinding.fabConfirmAsit.setOnClickListener {
            if(tools.isEnableGeolocation())
                mainViewModel.applyAttendance()
            else
                showAlert(getString(R.string.gps_desable_message)){
                    startActivity( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
        }
    }

    private fun setView(homeData: HomeInitialData) {
        setCalendarTitle()
        mainViewModel.validateStatus(homeData.userData){
            mainViewModel.user = homeData.userData?.data
        }
        mainViewModel.validateStatus(homeData.userList){
            mainViewModel.userList = homeData.userList?.data
        }
        mainViewModel.validateStatus(homeData.remoteDays!!){
            mainViewModel.remoteDays = homeData.remoteDays.data
        }
        when(mainViewModel.user!!.userType){
            UserType.COLLABORATOR.value -> {
                setViewAsCollaborator()
            }
            UserType.SUPERUSER.value -> {
                setViewAsSuperUser()
            }
            else ->{
                setViewAsCollaborator()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewAsCollaborator(){
        mainViewModel.showOrHideAttendanceButton()
        mBinding.tvWelcome.text = getString(R.string.welcome_message_1, mainViewModel.user!!.name)
        mBinding.tvDate.text = String.format(getString(R.string.welcome_date),
            resources.getStringArray(R.array.new_days)[localDate.dayOfWeek.value-1],localDate.dayOfMonth,
            resources.getStringArray(R.array.months)[localDate.monthValue-1],localDate.year)
        setUserAdapter()
        mainViewModel.getCalendarDays(MonthType.CURRENT)
    }

    private fun setViewAsSuperUser(){
    /**agregar cambios para super usuario */
    }

    private fun setUserAdapter(){
        mBinding.progress.visibility = View.GONE
        mUserAdapter = UserAdapter(mainViewModel.userList!!){
            UserDialog(it).show(parentFragmentManager,"Yep")
        }
        mBinding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(requireContext().applicationContext)
            adapter = mUserAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun newSetCalendarAdapter(){
        newCalendarAdapter = NewCalendarAdapter().apply {
            attendanceDaysList = mainViewModel.getAttendanceDays()
            userType = userData.userType
            onClickDay = { day ->
                onClickCalendarDay(day)
            }
        }
        mBinding.recyclerCalendar.apply {
            layoutManager = GridLayoutManager(requireContext().applicationContext,5)
            adapter = newCalendarAdapter
        }
    }

    private fun changeNextMonth(){
        if (statusMonthType == MonthType.NEXT ) return
        statusMonthType = if (statusMonthType == MonthType.LAST) MonthType.CURRENT else MonthType.NEXT
        localDate = localDate.plusMonths(1)
        mainViewModel.getCalendarDays(statusMonthType)
    }

    private fun changeLastMonth(){
        if (statusMonthType == MonthType.LAST ) return
        statusMonthType = if (statusMonthType == MonthType.NEXT) MonthType.CURRENT else MonthType.LAST
        localDate = localDate.plusMonths(-1)
        mainViewModel.getCalendarDays(statusMonthType)
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendarTitle() {
        mBinding.tvMonth.text = (resources.getStringArray(R.array.months)[localDate.monthValue-1])
        (activity as? HomeActivity)?.hideBottomBar(true)
    }

    private fun onClickCalendarDay(day:CalendarDay){
        val newDay = mainViewModel.getDay(day)
        viewModel.setDay(newDay.date)
        viewModel.setObjectDay(newDay)
        findNavController().navigate(AssistenceMainFragmentDirections.actionAssistenceMainFragmentToAssistenceWeekFragment())
    }

    private fun showAlert(message:String, action:() -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Title")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { view, _ ->
            action()
            view.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}