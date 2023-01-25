package com.example.myapplication.ui.home


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main) {

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding:FragmentAssistenceMainBinding
    private val viewModel:HomeViewModel by activityViewModels()

    private lateinit var bundleNum :Bundle
    private lateinit var bundleDay : Bundle

    var localDate: LocalDate= LocalDate.now()
    private var pastDate = localDate.minusMonths(1)
    private var actualMonth = 1

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
        setUi()
        setListeners()
    }

    private fun setObservers() {
        viewModel.userData.observe(viewLifecycleOwner, this::setCalendarDays)

        viewModel.currentMonth.observe(viewLifecycleOwner, this::setCurrentDate)
    }

    private fun setCalendarDays(daysToAttend:List<AttendanceDays>){
        mCalendarAdapter.statusMonth = actualMonth
        mCalendarAdapter.assistedDays = getDaysToAttend(daysToAttend)
        viewModel.setCalendarDays(localDate, pastDate, daysToAttend)
    }

    private fun getDaysToAttend(daysToAttend: List<AttendanceDays>): List<Int> {
        val userAssistanceDays = arrayListOf<Int>()
        daysToAttend.forEach{ day ->
            if(day.emails.any {it == "humberto.macias@coppel.com"}) {
                val date = day.currentDay.split("-")
                val assistanceDay = date[0].toInt()
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
        mBinding.home.setOnClickListener{
            moveNavSelector(it)
        }
        mBinding.team.setOnClickListener{
            moveNavSelector(it)
        }
        mBinding.myProfile.setOnClickListener{
            moveNavSelector(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUi() {
        mBinding.tvMonth.text = monthYearFromDate(localDate)
    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun setCurrentDate(month:Month) {
        mCalendarAdapter.setCalendarData(month.daysList)
        mCalendarAdapter.today = month.today
        mCalendarAdapter.isPastMonth = month.pastMonth
        mCalendarAdapter.notifyDataSetChanged()
    }

    private fun setUserAdapter() {
        viewModel.getUserDate(date = localDate)
        mUserAdapter = UserAdapter(
            user= listOf(
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior"),
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior"),
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior"),
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior"),
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior"),
                User("example@coppel.com", "Ramon", "Coppel", "Coppel", "Gerente Senior")
            )
        )
        mBinding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = mUserAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun setCalendarAdapter(){
        mCalendarAdapter = CalendarAdapter(){
            click(it)
        }
        mBinding.recyclerCalendar.apply {
            layoutManager = GridLayoutManager(activity?.applicationContext,5)
            adapter = mCalendarAdapter
        }
    }

    private fun previusMonthAction(){
        if (actualMonth == 0 )
            return
        localDate = localDate.minusMonths(1)
        pastDate = pastDate.minusMonths(1)
        actualMonth -= 1
        viewModel.getUserDate(date = localDate)
        setUi()
    }

    private fun nextMonthAction(){
        if (actualMonth == 2 )
            return
        localDate = localDate.plusMonths(1)
        pastDate = pastDate.minusMonths(1)
        actualMonth += 1
        viewModel.getUserDate(date = localDate)
        setUi()
    }

    private fun monthYearFromDate(date:LocalDate):String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun moveNavSelector(view: View) {
        when(view.id){
            R.id.home ->{
                mBinding.containerSelectorNav.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = mBinding.home.id
                    endToEnd = mBinding.home.id
                }
                mBinding.imgSelectorNav.setImageDrawable(requireContext().getDrawable(R.drawable.ic_home))
            }
            R.id.team ->{
                mBinding.containerSelectorNav.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = mBinding.team.id
                    endToEnd = mBinding.team.id
                }
                mBinding.imgSelectorNav.setImageDrawable(requireContext().getDrawable(R.drawable.ic_group))
            }
            R.id.myProfile ->{
                mBinding.containerSelectorNav.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = mBinding.myProfile.id
                    endToEnd = mBinding.myProfile.id
                }
                mBinding.imgSelectorNav.setImageDrawable(requireContext().getDrawable(R.drawable.ic_my_profile))
            }
            else -> { }
        }
    }

    private fun click(day:Day){
        findNavController().navigate(AssistenceMainFragmentDirections.actionAssistenceMainFragmentToAssistenceWeekFragment(day))
    }

}