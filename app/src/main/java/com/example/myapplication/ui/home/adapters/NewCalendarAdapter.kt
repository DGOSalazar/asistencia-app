package com.example.myapplication.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.core.utils.MonthType
import com.example.myapplication.core.utils.UserType
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.models.CalendarDay
import com.example.myapplication.ui.home.viewholders.CalendarViewHolder

class NewCalendarAdapter :
    RecyclerView.Adapter<CalendarViewHolder>() {

    var calendarDaysList: ArrayList<CalendarDay> = arrayListOf()
    var onClickDay: ((CalendarDay) -> Unit)? = null
    var attendanceDaysList: List<DayCollection> = arrayListOf()
    var userType:Int = UserType.COLLABORATOR.value
    var monthType:MonthType = MonthType.CURRENT

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.day_view, viewGroup, false)
        return CalendarViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: CalendarViewHolder, position: Int) {
        val day = calendarDaysList[position]

        viewHolder.binding.container.setOnClickListener {
            if(day.isEnable)
                onClickDay?.invoke(day)
        }

        if (userType == UserType.SUPERUSER.value){
            viewHolder.setViewAsSuperUser(day)
            return
        }

        when(monthType){
            MonthType.NEXT -> {
                viewHolder.setNextMonthView(
                    day,
                    attendanceDaysList
                )
            }
            MonthType.LAST -> {
                viewHolder.setViewBeforeToday(
                    day,
                    attendanceDaysList
                )
            }
            MonthType.CURRENT -> {
                viewHolder.setCurrentMonthView(
                    day,
                    attendanceDaysList,
                )
            }
        }
    }

    override fun getItemCount() = calendarDaysList.size


}