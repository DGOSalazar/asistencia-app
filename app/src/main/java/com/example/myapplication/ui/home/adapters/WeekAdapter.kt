package com.example.myapplication.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.Day
import com.example.myapplication.databinding.DaySelectedViewBinding

class WeekAdapter(var weekDays: List<Day>, private var click: (Day) -> Unit): RecyclerView.Adapter<WeekAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val mBinding = DaySelectedViewBinding.bind(view)

        fun setWeek(){
            mBinding.tvDay.text = weekDays[adapterPosition].name
            mBinding.tvNumDay.text = weekDays[adapterPosition].num.toString()
            mBinding.mcMain.visibility = if(weekDays[adapterPosition].selected){View.GONE} else {View.VISIBLE}
            if(weekDays[adapterPosition].selected) {
                with(mBinding) {
                    mcSelected.visibility = View.VISIBLE
                    mcMain.visibility = View.GONE
                    tvDay1.text=weekDays[adapterPosition].name
                    tvNumDay1.text=weekDays[adapterPosition].num.toString()
                }
            }
        }
        fun selectDay(click:(Day)->Unit){
            mBinding.mcMain.setOnClickListener {
                click(weekDays[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_selected_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            setWeek()
            selectDay(click)
        }
    }
    override fun getItemCount() = weekDays.size
}