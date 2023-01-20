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

    private var isSelected : Int? = null
    private var selected=false

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val mBinding = DaySelectedViewBinding.bind(view)


        fun setWeek(){
            mBinding.tvDay.text = weekDays[adapterPosition].name
            mBinding.tvNumDay.text = weekDays[adapterPosition].num.toString()
            mBinding.mcMain.visibility = if(weekDays[adapterPosition].selected){View.GONE} else {View.VISIBLE}
            mBinding.mcSelected.visibility = if(weekDays[adapterPosition].selected){View.VISIBLE} else {View.GONE}
        }
        fun selectDay(click:(Day)->Unit){
            mBinding.mcMain.setOnClickListener {
                click(weekDays[adapterPosition])
                setSelectedDay()
            }
        }
        private fun setSelectedDay(){
            if (!selected) {
                mBinding.mcMain.visibility = View.GONE
                mBinding.mcSelected.visibility = View.VISIBLE
                selected=true
            }else{
                mBinding.mcMain.visibility = View.VISIBLE
                mBinding.mcSelected.visibility = View.GONE
                selected=false
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