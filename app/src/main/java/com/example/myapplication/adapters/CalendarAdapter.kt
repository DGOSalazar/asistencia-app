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
            val day = days[adapterPosition]
            if (day.num == null){
                with(mBinding) {
                    tvDay.visibility=View.GONE
                    mcFreePlaces.visibility=View.GONE
                    ivProfile.visibility=View.GONE
                    mcIsToday.visibility=View.GONE
                }
            }else{
                mBinding.tvDay.text= (day.num ?:"" ).toString()
                mBinding.tvDay.visibility=View.VISIBLE
                if (day.isCurrentMonth) mBinding.tvDay.setTypeface(null, Typeface.BOLD)
                mBinding.mcFreePlaces.visibility = if(day.freePlaces) View.VISIBLE else View.GONE
                mBinding.ivProfile.visibility= if (day.profilePhoto) View.VISIBLE else View.GONE
                mBinding.mcIsToday.visibility= if(day.isToday) View.VISIBLE else View.GONE
            }
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