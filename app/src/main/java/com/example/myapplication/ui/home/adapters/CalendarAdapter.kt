package com.example.myapplication.ui.home.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.DayViewBinding
import com.example.myapplication.data.models.Day
import com.example.myapplication.ui.home.CURRENT_MONTH
import com.example.myapplication.ui.home.NEXT_MONTH
import com.example.myapplication.ui.home.PAST_MONTH


class CalendarAdapter(private var days: ArrayList<Day> = arrayListOf(),private var click:(Day)-> Unit ): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var today:Int = 0
    var assistedDays = emptyList<Int>()
    var statusMonth = 1
    var daysToFormatNextMonth = 0
    var imageProfileUrl = ""

    @RequiresApi(Build.VERSION_CODES.M)
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        private var mBinding= DayViewBinding.bind(view)
        private val ctx = view.context

        init {
            with(mBinding) {
                container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
                tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey2)))
                mBinding.tvDay.visibility = View.VISIBLE
                assistedDay.visibility = View.GONE
            }
        }

        @SuppressLint("ResourceAsColor")
        fun mountCalendar(){
            val day = days[adapterPosition]
            val isAssistedDay = assistedDays.any { it == day.num && day.isCurrentMonth}
            val isFreePlaces = day.places > 0

            with(mBinding) {
                when(statusMonth){
                    NEXT_MONTH->{
                        val isEnable = daysToFormatNextMonth > adapterPosition +1
                        day.enable = isEnable

                        container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
                        tvDay.visibility = View.VISIBLE
                        assistedDay.visibility = View.GONE
                        mcFreePlaces.visibility =  if(!isAssistedDay && day.num != today && isEnable) View.VISIBLE else View.GONE
                        ivProfile.visibility = if (isAssistedDay && day.isCurrentMonth) View.VISIBLE else View.GONE

                        val dayBgColor = if(day.isCurrentMonth) R.color.grey2 else R.color.grey4
                        tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(dayBgColor)))
                    }
                    PAST_MONTH->{
                        mcFreePlaces.visibility = View.GONE
                        ivProfile.visibility = View.GONE
                        assistedDay.visibility =  if (isAssistedDay && day.isCurrentMonth) View.VISIBLE else View.GONE
                        tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
                        container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
                    }
                    CURRENT_MONTH->{
                        if (day.num < today && day.isCurrentMonth){
                            mcFreePlaces.visibility = View.GONE
                            ivProfile.visibility = View.GONE
                            assistedDay.visibility = if (isAssistedDay) View.VISIBLE else View.GONE

                            val bgContainer = if(day.num == today) R.color.white else R.color.grey4
                            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(bgContainer))

                            tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
                        }else{
                            assistedDay.visibility = View.GONE
                            if (day.enable){
                                val visibilityValue = if(!isAssistedDay && day.num != today ) View.VISIBLE else View.GONE
                                mcFreePlaces.visibility = visibilityValue

                                ivProfile.visibility = if (isAssistedDay && day.isCurrentMonth) View.VISIBLE else View.GONE

                                val bgColor = if(day.isToday) R.color.blue1 else R.color.white
                                container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(bgColor))

                                val dayBgColor = if(day.isCurrentMonth) R.color.grey2 else R.color.grey4
                                tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(dayBgColor)))

                                container.isEnabled = day.num != today
                            }else{
                                container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
                                tvDay.visibility = View.VISIBLE
                                assistedDay.visibility = View.GONE
                                ivProfile.visibility = View.GONE
                                mcFreePlaces.visibility = View.GONE

                                val dayBgColor = if(day.isCurrentMonth) R.color.grey2 else R.color.grey4
                                tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(dayBgColor)))
                            }
                        }
                    }
                }
                Glide.with(ctx).load(imageProfileUrl).into(ivProfile)
                tvDay.text = String.format("%02d", day.num)
                val freeDaysBgColor = if(isFreePlaces) R.color.blue1 else R.color.blue2
                freePlaces.backgroundTintList = ColorStateList.valueOf(ctx.getColor(freeDaysBgColor))
                freePlaces.text = ctx.resources.getString(R.string.free_days, day.places)
            }
        }

        fun getDay(click:(Day)->Unit){
            val day = days[adapterPosition]
            mBinding.root.setOnClickListener {
                if (day.enable)
                    click(day)
            }
        }
    }
    fun setCalendarData(days: ArrayList<Day>){
        this.days=days
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_view,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountCalendar()
            getDay(click)
        }
    }

    override fun getItemCount(): Int = days.size

}