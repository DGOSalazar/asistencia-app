package com.example.myapplication.adapters

import android.R.attr.checked
import android.R.attr.typeface
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DayViewBinding
import com.example.myapplication.models.Day


val listDay = listOf(Day(),Day(),Day(freePlaces = true),Day(),Day(profilePhoto = true),Day(),Day(freePlaces = true),Day(),Day(),Day(),Day(),Day(),Day(),Day(profilePhoto = true),Day(),Day(),Day(),Day(profilePhoto = true),Day(freePlaces = true),Day())

class CalendarAdapter(private var days: ArrayList<Day> = arrayListOf()): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var mBinding= DayViewBinding.bind(view)
        fun mountCalendar(){
            mBinding.tvDay.text= days[adapterPosition].num.toString()
            if (days[adapterPosition].isCurrentMonth) mBinding.tvDay.setTypeface(null, Typeface.BOLD)
            mBinding.mcFreePlaces.visibility = if(days[adapterPosition].freePlaces) View.VISIBLE else View.GONE
            mBinding.ivProfile.visibility= if (days[adapterPosition].profilePhoto) View.VISIBLE else View.GONE
            mBinding.mcIsToday.visibility= if(days[adapterPosition].isToday) View.VISIBLE else View.GONE
        }
    }
    fun setCalendarData(days: ArrayList<Day>){
        this.days=days
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountCalendar()
        }
    }

    override fun getItemCount(): Int = days.size

}