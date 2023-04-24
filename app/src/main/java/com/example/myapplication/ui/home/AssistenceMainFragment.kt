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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.dialog.UserDialog
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.example.myapplication.databinding.FragmentAssistenceMainBinding
import com.example.myapplication.sys.utils.Tools
import com.example.myapplication.ui.home.adapters.CalendarAdapter
import com.example.myapplication.ui.home.adapters.UserAdapter
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


const val CURRENT_MONTH = 1
const val PAST_MONTH = 2
const val NEXT_MONTH = 3


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main){

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding:FragmentAssistenceMainBinding
    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var tools: Tools

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate= LocalDate.now()
    private var actualMonth = CURRENT_MONTH
    private var accountEmail = ""
    private var userData: UserHomeResponse = UserHomeResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountEmail = viewModel.getEmail()
        viewModel.cleanLiveData()
        // TODO: recuperar desde el viewModel
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
        setObservers()
        setLaunch()
        setCalendarAdapter()
        setCalendarTitle()
        setListeners()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun setObservers() {
        viewModel.assistanceDays.observe(viewLifecycleOwner, this::setCalendarDays)
        viewModel.currentMonth.observe(viewLifecycleOwner, this::updateCurrentDateInCalendar)
        viewModel.accountData.observe(viewLifecycleOwner, this::setHeader)
        viewModel.userEmails.observe(viewLifecycleOwner){
            viewModel.getUserDatastore(it)
        }
        viewModel.users.observe(viewLifecycleOwner){
            updateUsersList(it)
        }
        viewModel.local.observe(viewLifecycleOwner){
            context?.toast(it.toString())
        }

        viewModel.status.observe(viewLifecycleOwner) {
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
                    handleStatusSuccess(it)
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }

        viewModel.statusHistoryRegister.observe(viewLifecycleOwner){
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
                    viewModel.showOrHideAttendanceButton( accountEmail, getCurrentDate())
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }

        viewModel.showOrHideAttendanceBtn.observe(viewLifecycleOwner){
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
                    mBinding.fabConfirmAsit.visibility = if (it.data) View.VISIBLE else View.GONE
                }
                is ResponseStatus.Error -> {
                    if (isAdded)
                        (activity as HomeActivity).dismissLoader()
                    context?.toast(getString(it.messageId))
                }
            }
        }
    }

    private fun handleStatusSuccess(responseStatus: ResponseStatus.Success<Any>) {
        when(responseStatus.data){
            is Boolean -> {
                if(responseStatus.data){
                    viewModel.registerHistoryAttendance(accountEmail, getCurrentDate())
                }
                else
                    showAlert(getString(R.string.error_local_location_not_match)){ }
            }
            else -> return
        }
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

    @SuppressLint("SetTextI18n")
    private fun updateUsersList(emailList:ArrayList<UserHomeResponse>) {
        mBinding.tvAssist.text = "${emailList.size} Asistentes"
        mUserAdapter = UserAdapter(emailList){
            clickUser(it)
        }
        mBinding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = mUserAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
        mBinding.progress.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setHeader(user: UserHomeResponse) {
        mBinding.tvWelcome.text = "Hola ${user.name}"
        userData = user
        var today = localDate.dayOfWeek.value
        if((today == 6) || (today == 7))
        {
            mBinding.tvDate.text = if(localDate.dayOfWeek == DayOfWeek.SATURDAY)
                String.format(getString(R.string.welcome_date),
                getString(R.string.saturday),localDate.dayOfMonth,
                    resources.getStringArray(R.array.months)[localDate.monthValue-1],localDate.year) else
                String.format(getString(R.string.welcome_date),
                    getString(R.string.sunday),localDate.dayOfMonth,
                    resources.getStringArray(R.array.months)[localDate.monthValue-1],localDate.year)
        }else {
            mBinding.tvDate.text = String.format(getString(R.string.welcome_date),
                resources.getStringArray(R.array.days)[localDate.dayOfWeek.value-1],localDate.dayOfMonth,
                resources.getStringArray(R.array.months)[localDate.monthValue-1],localDate.year)
        }

        mCalendarAdapter.imageProfileUrl = user.profilePhoto
        mCalendarAdapter.notifyDataSetChanged()
    }

    private fun setCalendarDays(daysToAttend:List<AttendanceDays>){
        mCalendarAdapter.statusMonth = actualMonth
        mCalendarAdapter.assistedDays = getDaysToAttend(daysToAttend)
        viewModel.setCalendarDays(localDate, localDate.minusMonths(1), daysToAttend, actualMonth)
        //showAttendanceButton(daysToAttend)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = LocalDate.now().format(formatter)
        viewModel.showOrHideAttendanceButton( accountEmail, currentDate)
    }

    private fun showAttendanceButton(daysToAttend: List<AttendanceDays>) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = LocalDate.now().format(formatter)

        val isAttendanceDay = daysToAttend.any {
            if (it.currentDay == currentDate)
                it.emails.any{ email -> email == accountEmail }
            else
                false
        }

        if (isAttendanceDay)
            mBinding.fabConfirmAsit.visibility = View.VISIBLE
        else
            mBinding.fabConfirmAsit.visibility = View.GONE
    }

    private fun getDaysToAttend(daysToAttend: List<AttendanceDays>): List<Int> {
        val userAssistanceDays = arrayListOf<Int>()
        daysToAttend.forEach{ day ->
            if(day.emails.any {it == accountEmail}) {
                val date = day.currentDay.split("-")
                val assistanceDay = date[0].toInt()
                val currentMonth = localDate.toString().split("-")[1]
                if (date[1]==currentMonth)
                    userAssistanceDays.add(assistanceDay)
            }
        }
        return userAssistanceDays
    }

    private fun setListeners() {
        mBinding.vBack.setOnClickListener{
            previusMonthAction()
        }
        mBinding.vNext.setOnClickListener{
            nextMonthAction()
        }

        mBinding.tvMenuIcon.setOnClickListener{
            context?.toast("TopMenu")
        }
        mBinding.fabConfirmAsit.setOnClickListener {
            if(tools.isEnableGeolocation())
                viewModel.applyAttendance()
            else
                showAlert(getString(R.string.gps_desable_message)){
                    startActivity( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendarTitle() {
        mBinding.tvMonth.text = (resources.getStringArray(R.array.months)[localDate.monthValue-1])
        (activity as? HomeActivity)?.hideBottomBar(true)
    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun updateCurrentDateInCalendar(month:Month) {
        mCalendarAdapter.apply {
            daysToFormatNextMonth = if (month.daysToFormatNextMonth > 0 ) month.daysToFormatNextMonth else 0
            setCalendarData(month.daysList)
            today = month.today
            notifyDataSetChanged()
        }
        setCalendarTitle()
        mBinding.loader.visibility = View.GONE
    }

    private fun setLaunch() {
        viewModel.setEmail(accountEmail)
        viewModel.getAccountData(accountEmail)
        mBinding.progress.visibility = View.VISIBLE
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedString = localDate.format(formatter)
        viewModel.getListEmails(formattedString)
        viewModel.setDay(formattedString)
        viewModel.confirmStatus(accountEmail, formattedString)
    }

    private fun setCalendarAdapter(){
        viewModel.getUserDate()
        mCalendarAdapter = CalendarAdapter {
            if(it.isCurrentMonth){
                click(it)
            }else{
                nextMonthAction()
            }
        }
        mBinding.recyclerCalendar.apply {
            layoutManager = GridLayoutManager(activity?.applicationContext,5)
            adapter = mCalendarAdapter
        }
    }

    private fun previusMonthAction(){
        if (actualMonth == PAST_MONTH ) return
        mBinding.loader.visibility = View.VISIBLE
        localDate = localDate.minusMonths(1)
        actualMonth = if (actualMonth == NEXT_MONTH) CURRENT_MONTH else PAST_MONTH
        viewModel.getUserDate()
    }

    private fun nextMonthAction(){
        if (actualMonth == NEXT_MONTH ) return
        mBinding.loader.visibility = View.VISIBLE
        localDate = localDate.plusMonths(1)
        actualMonth = if (actualMonth == PAST_MONTH) CURRENT_MONTH else NEXT_MONTH
        viewModel.getUserDate()
    }

    private fun monthYearFromDate(date:LocalDate):String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun clickUser(u: UserHomeResponse){
        UserDialog(u).show(parentFragmentManager,"Yep")
    }
    private fun click(day:Day){
        viewModel.setDay(day.date)
        viewModel.setObjectDay(day)
        findNavController().navigate(AssistenceMainFragmentDirections.actionAssistenceMainFragmentToAssistenceWeekFragment())
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.now().format(formatter)
    }
}