package com.example.myapplication.ui.home


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.Month
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentAssistenceMainBinding
import com.example.myapplication.ui.home.adapters.CalendarAdapter
import com.example.myapplication.ui.home.adapters.UserAdapter
import com.example.myapplication.ui.login.EMAIL_KEY
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue


const val CURRENT_MONTH = 1
const val PAST_MONTH = 2
const val NEXT_MONTH = 3

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main) {

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding:FragmentAssistenceMainBinding
    private val viewModel: HomeViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate= LocalDate.now()
    private var pastDate = localDate.minusMonths(1)
    private var actualMonth = CURRENT_MONTH
    private var accountEmail = ""

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountEmail = sharedPreferences.getString(EMAIL_KEY, "").toString()
        viewModel.setEmail(accountEmail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAssistenceMainBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setCalendarAdapter()
        setUserAdapter()
        setCalendarTitle()
        setListeners()
    }

    private fun setObservers() {
        viewModel.assistanceDays.observe(viewLifecycleOwner, this::setCalendarDays)
        viewModel.currentMonth.observe(viewLifecycleOwner, this::updateCurrentDateInCalendar)
        viewModel.accountData.observe(viewLifecycleOwner, this::setHeader)
        viewModel.newUsers.observe(viewLifecycleOwner, this::updateUsersList)
        viewModel.newUserEmails.observe(viewLifecycleOwner){
            viewModel.getUserDatastore(it, 1)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUsersList(emailList:ArrayList<User>) {
        mBinding.tvAssist.text = "${emailList.size} Asistentes"
        mUserAdapter = UserAdapter(emailList)
        mBinding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = mUserAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setHeader(user: User) {
        mBinding.tvWelcome.text = "Hola ${user.name}"
        mBinding.tvDate.text = "Hoy es $pastDate"
        mCalendarAdapter.imageProfileUrl = user.profilePhoto
        mCalendarAdapter.notifyDataSetChanged()
    }

    private fun setCalendarDays(daysToAttend:List<AttendanceDays>){
        mCalendarAdapter.statusMonth = actualMonth
        mCalendarAdapter.assistedDays = getDaysToAttend(daysToAttend)
        viewModel.setCalendarDays(localDate, localDate.minusMonths(1), daysToAttend, actualMonth)
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
        mBinding.containerHomeNav.setOnClickListener{
            moveNavSelector(it)
        }
        mBinding.containerTeamNav.setOnClickListener{
            moveNavSelector(it)
        }
        mBinding.containerMyProfileNav.setOnClickListener{
            moveNavSelector(it)
        }
        mBinding.tvMenuIcon.setOnClickListener{
            Toast.makeText(requireContext(), "Top menu", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendarTitle() {
        mBinding.tvMonth.text = monthYearFromDate(localDate)
    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun updateCurrentDateInCalendar(month:Month) {
        mCalendarAdapter.daysToFormatNextMonth = month.daysToFormatNextMonth.absoluteValue
        mCalendarAdapter.setCalendarData(month.daysList)
        mCalendarAdapter.today = month.today
        mCalendarAdapter.notifyDataSetChanged()
        setCalendarTitle()
        mBinding.loader.visibility = View.GONE
    }

    private fun setUserAdapter() {
        viewModel.getAccountData(accountEmail)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedString = localDate.format(formatter)
        viewModel.getListEmails(formattedString, 1)
    }

    private fun setCalendarAdapter(){
        viewModel.getUserDate()
        mCalendarAdapter = CalendarAdapter {
            click(it)
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
        pastDate = pastDate.minusMonths(1)
        actualMonth = if (actualMonth == NEXT_MONTH) CURRENT_MONTH else PAST_MONTH
        viewModel.getUserDate()
    }

    private fun nextMonthAction(){
        if (actualMonth == NEXT_MONTH ) return
        mBinding.loader.visibility = View.VISIBLE
        localDate = localDate.plusMonths(1)
        pastDate = pastDate.minusMonths(1)
        actualMonth = if (actualMonth == PAST_MONTH) CURRENT_MONTH else NEXT_MONTH
        viewModel.getUserDate()
    }

    private fun monthYearFromDate(date:LocalDate):String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun moveNavSelector(view: View) {
        when(view.id){
            R.id.container_home_nav ->{
                mBinding.containerHomeNav.setBackgroundResource(R.drawable.bg_buttom_nav)
                mBinding.containerTeamNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                mBinding.containerMyProfileNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                Toast.makeText(requireContext(), "Home", Toast.LENGTH_SHORT).show()
            }
            R.id.container_team_nav ->{
                mBinding.containerTeamNav.setBackgroundResource(R.drawable.bg_buttom_nav)
                mBinding.containerHomeNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                mBinding.containerMyProfileNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                Toast.makeText(requireContext(), "Equipo", Toast.LENGTH_SHORT).show()
            }
            R.id.container_my_profile_nav ->{
                mBinding.containerMyProfileNav.setBackgroundResource(R.drawable.bg_buttom_nav)
                mBinding.containerHomeNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                mBinding.containerTeamNav.setBackgroundColor(requireContext().getColor(R.color.grey1))
                Toast.makeText(requireContext(), "Perfil", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun click(day:Day){
        viewModel.setDay(day.date)
        viewModel.getListEmails(day.date)
        viewModel.setWeekList(day)
        findNavController().navigate(AssistenceMainFragmentDirections.actionAssistenceMainFragmentToAssistenceWeekFragment())
    }
}