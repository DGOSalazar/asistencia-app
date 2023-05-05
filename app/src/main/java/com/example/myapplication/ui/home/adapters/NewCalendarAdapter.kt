package com.example.myapplication.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.NewDayModel
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.ui.home.viewholders.CalendarViewHolder

class NewCalendarAdapter :
    RecyclerView.Adapter<CalendarViewHolder>() {

    var dataSet: ArrayList<NewDayModel> = arrayListOf()
    var onClickDay: ((NewDayModel) -> Unit)? = null
    var currentDate: String = ""
    var assistedDays: ArrayList<DayCollectionResponse> = arrayListOf()
    var userType:Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.day_view, viewGroup, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CalendarViewHolder, position: Int) {
        val day = dataSet[position]
        viewHolder.setView(
            day,
            currentDate,
            assistedDays,
            userType
        )
        viewHolder.binding.container.setOnClickListener { onClickDay?.invoke(day) }
    }

    override fun getItemCount() = dataSet.size


}