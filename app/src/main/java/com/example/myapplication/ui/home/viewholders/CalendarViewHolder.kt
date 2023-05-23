package com.example.myapplication.ui.home.viewholders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.models.CalendarDay
import com.example.myapplication.databinding.DayViewBinding
import com.example.myapplication.sys.utils.Tools.Companion.getTodayDate
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var binding:DayViewBinding
    private val ctx = view.context
    private var isAssistedDay:Boolean = false


    init {
        binding = DayViewBinding.bind(view)
        with(binding){
            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
            mcFreePlaces.visibility = View.GONE
            ivProfile.visibility = View.GONE
            assistedDayMark.visibility = View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun setCurrentMonthView(
        day: CalendarDay,
        attendanceDays:List<DayCollection>,
    ){
        isAssistedDay = attendanceDays.any{ it.currentDay == day.date}
        val dayType = getDayType(day.date)

        when{
            dayType > 0 -> setViewAfterToday(day)
            dayType < 0 -> setViewBeforeToday(day,attendanceDays)
            else -> {
                binding.container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.blue1))
                setViewAfterToday(day)
                binding.mcFreePlaces.visibility = View.GONE
            }
        }
    }

    fun setViewAsSuperUser(day: CalendarDay) {
        with(binding) {
            ivProfile.visibility = View.GONE
            assistedDayMark.visibility = View.GONE
        }
    }

    fun setViewBeforeToday(
        day:CalendarDay,
        attendanceDays:List<DayCollection>
    ) {
        isAssistedDay = attendanceDays.any{ it.currentDay == day.date}
        with(binding) {
            freePlaces.text = day.freePlaces.toString()
            tvDay.text = day.date.substring(0,2)
            mcFreePlaces.visibility = View.GONE
            ivProfile.visibility = View.GONE
            assistedDayMark.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
            tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
        }
    }

    fun setNextMonthView(
        day:CalendarDay,
        attendanceDays:List<DayCollection>
    ){
        with(binding) {
            isAssistedDay = attendanceDays.any{ it.currentDay == day.date}
            mcFreePlaces.visibility = if(!isAssistedDay && day.isEnable) View.VISIBLE else View.GONE
            val colorTextDay = if (!isCurrentMonth(day)) R.color.grey2 else R.color.grey4
            tvDay.apply {
                visibility = View.VISIBLE
                setTextColor(ColorStateList.valueOf(ctx.getColor(colorTextDay)))
                text = day.date.substring(0,2)
            }
        }
    }

    private fun setViewAfterToday(day:CalendarDay) {
        with(binding){
            val colorTextDay = if (isCurrentMonth(day)) R.color.grey2 else R.color.grey4
            tvDay.apply {
                visibility = View.VISIBLE
                setTextColor(ColorStateList.valueOf(ctx.getColor(colorTextDay)))
            }
            assistedDayMark.visibility = View.GONE
            mcFreePlaces.visibility = if(!isAssistedDay && day.isEnable) View.VISIBLE else View.GONE
            ivProfile.visibility = if (isAssistedDay ) View.VISIBLE else View.GONE
            binding.freePlaces.text =  day.freePlaces.toString()
            binding.tvDay.text = day.date.substring(0,2)
        }
    }

    private fun isCurrentMonth(day:CalendarDay):Boolean  {
        val currentDate = getTodayDate()
        return day.date.substring(3,5).toInt() == currentDate.substring(3,5).toInt()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDayType(inputDate:String):Int{
        val compareFormat = SimpleDateFormat("yyyy-MM-dd")
        val inputDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = inputDateFormat.parse(inputDate)
        return compareFormat.format(date!!).compareTo(getTodayDate(format =   true))
    }

}