package com.example.myapplication.ui.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.Day
import com.example.myapplication.databinding.DaySelectedViewBinding

class WeekAdapter(var weekDays: List<Day>, var click: (Day) -> Unit): RecyclerView.Adapter<WeekAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val mBinding = DaySelectedViewBinding.bind(view)
        var isChecked=true
        fun setWeek(){
            mBinding.tvDay.text = weekDays[adapterPosition].name
            mBinding.tvNumDay.text = weekDays[adapterPosition].num.toString()
        }
        fun selectDay(click:(Day)->Unit){
            mBinding.root.setOnClickListener {
                click(weekDays[adapterPosition])
                setSelectedDay()
            }
        }
        private fun setSelectedDay(){

            val param = mBinding.mcMain.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,30,0)
            if(isChecked)
            with(mBinding) {
                mcMain.layoutParams.height = 180
                mcMain.layoutParams.width = 180
                tvDay.setBackgroundColor(Color.BLUE)
                tvNumDay.setTextColor(Color.BLACK)
                param.setMargins(0,0,0,0)
                mcMain.layoutParams = param
                isChecked=false
            }else{
                with(mBinding){
                    mcMain.layoutParams.height = 150
                    mcMain.layoutParams.width = 150
                    tvDay.setBackgroundColor(Color.GRAY)
                    tvNumDay.setTextColor(Color.GRAY)
                    param.setMargins(0,0,0,0)
                    mcMain.layoutParams = param
                    isChecked=true
                }
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