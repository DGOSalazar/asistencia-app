package com.example.myapplication.ui.home


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.models.Day
import com.example.myapplication.databinding.FragmentAssistenceMainBinding
import com.example.myapplication.ui.home.adapters.CalendarAdapter
import com.example.myapplication.ui.home.adapters.UserAdapter
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main), OnTouchListener {

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding:FragmentAssistenceMainBinding
    private var dayList : ArrayList<Day> = arrayListOf()
    private var xPreviousPosition = 0f

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate= LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAssistenceMainBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCalendarAdapter()
        setUserAdapter()
        setUi()
        setCurrentDate()
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        mBinding.vBack.setOnClickListener{
            previusMonthAction()
        }
        mBinding.vNext.setOnClickListener{
            nextMonthAction()
        }
        mBinding.recyclerCalendar.setOnTouchListener(this)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUi() {
        //mBinding.tvMonth.text=("${localDate.month}/${localDate.year}")
        mBinding.tvMonth.text=monthYearFromDate(localDate)
    }
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentDate() {
        val  yearMonth = YearMonth.from(localDate)
        val firstOfMonth= localDate.withDayOfMonth(1)
        val numMonth= localDate.dayOfMonth

        val daysOfMonth= yearMonth.lengthOfMonth()
        val dayOfWeek= firstOfMonth.dayOfWeek.value
        dayList.clear()

        //Cargar  array con arrays; array de meses y array de sus dias correspondientes

        val sunDaysAndSaturdays= arrayOf(1,7,8,14,15,21,22,28,29,35,36,42)

        for(i in 1..42){
            val isSundayOrSaturday = sunDaysAndSaturdays.any{ it == i }
            if(!isSundayOrSaturday) {
                if (i <= dayOfWeek || i> daysOfMonth + dayOfWeek ){
                    if(dayOfWeek !in 6..7 &&  localDate.withDayOfMonth(dayOfWeek).dayOfWeek.value !in 6..7 )
                        dayList.add(Day(num = null, isCurrentMonth = false, freePlaces = true))
                }else{
                    val isSundayOrSaturdy2 = localDate.withDayOfMonth(i-dayOfWeek).dayOfWeek.value !in 6..7
                    if (isSundayOrSaturdy2)
                        dayList.add(Day(num = i-dayOfWeek, profilePhoto = true,freePlaces = true))
                }
            }
        }

        mCalendarAdapter.setCalendarData(dayList)
        mCalendarAdapter.notifyDataSetChanged()
    }
    private fun setUserAdapter() {
        mUserAdapter = UserAdapter()
        mBinding.recyclerUsers.apply {
            layoutManager= LinearLayoutManager(activity?.applicationContext)
            adapter=mUserAdapter
        }
    }
    private fun setCalendarAdapter(){
        mCalendarAdapter = CalendarAdapter()
        mBinding.recyclerCalendar.apply {
            layoutManager= GridLayoutManager(activity?.applicationContext,5)
            adapter=mCalendarAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun previusMonthAction(){
        localDate = localDate.minusMonths(1)
        setCurrentDate()
        setUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonthAction(){
        localDate = localDate.plusMonths(1)
        setCurrentDate()
        setUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date:LocalDate):String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {

        if (motionEvent!!.action == MotionEvent.ACTION_DOWN){
            xPreviousPosition = motionEvent.x
            return false
        }

        if (motionEvent.action == MotionEvent.ACTION_UP){
            if (xPreviousPosition > motionEvent.x)
                previusMonthAction()
            else
                nextMonthAction()
            return true
        }
        return false
    }


}