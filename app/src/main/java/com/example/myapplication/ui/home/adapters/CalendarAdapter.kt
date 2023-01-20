package com.example.myapplication.ui.home.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DayViewBinding
import com.example.myapplication.data.models.Day

class CalendarAdapter(private var days: ArrayList<Day> = arrayListOf(),private var click:(Day)-> Unit ): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {


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
        fun getDay(click:(Day)->Unit){
            mBinding.root.setOnClickListener {
                click(days[adapterPosition])
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
            getDay(click)
        }
    }

    override fun getItemCount(): Int = days.size

}