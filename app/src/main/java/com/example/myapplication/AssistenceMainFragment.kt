package com.example.myapplication

import UserAdapter
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.CalendarAdapter
import com.example.myapplication.databinding.FragmentAssistenceMainBinding
import com.example.myapplication.models.Day
import java.time.LocalDate
import java.time.YearMonth


class AssistenceMainFragment : Fragment(R.layout.fragment_assistence_main) {

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mBinding:FragmentAssistenceMainBinding
    private var dayList : ArrayList<Day> = arrayListOf()

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
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUi() {
        mBinding.tvMonth.text=("${localDate.month}/${localDate.year}")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentDate() {
        val  yearMonth = YearMonth.from(localDate)
        val firstOfMonth= localDate.withDayOfMonth(1)
        val numMonth= localDate.dayOfMonth

        val daysOfMonth= yearMonth.lengthOfMonth()
        val dayOfWeek= firstOfMonth.dayOfWeek.value
        var j=0

        //Cargar  array con arrays; array de meses y array de sus dias correspondientes

        for (i in 1..41){
            if(i <= dayOfWeek || i > daysOfMonth + dayOfWeek)
            {
                if (i > daysOfMonth + dayOfWeek)
                    dayList.add(Day(num=i-(daysOfMonth+dayOfWeek), isCurrentMonth = false, freePlaces = true))
            }
            else
            {
                j++
                if (localDate.withDayOfMonth(j).dayOfWeek.value<6) {
                    if (j==numMonth)
                        dayList.add(Day(num = i-dayOfWeek, isToday = true, profilePhoto = true))
                    else
                        dayList.add(Day(num = i-dayOfWeek, profilePhoto = true,freePlaces = true))
                }
            }
        }
        mCalendarAdapter.setCalendarData(dayList)
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
}