package com.example.myapplication.ui.home.viewholders

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.NewDayModel
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.databinding.DayViewBinding

class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = DayViewBinding.bind(view)
    private val ctx = view.context
    private var isAssistedDay:Boolean = false


    @RequiresApi(Build.VERSION_CODES.M)
    fun setView(day: NewDayModel, currentDate:String, assistedDays:ArrayList<DayCollectionResponse>){
        val dayType = day.date.compareTo(currentDate)

        val remoteDate = assistedDays.filter{ it.currentDay == day.date}
        isAssistedDay = if (remoteDate.isEmpty()) false else remoteDate[0].email!!.any { it == day.date}

        when{
            dayType > 0 -> setViewAsNextMonthDay(day)
            dayType < 0 -> setViewAsLastMonthDay()
            else -> setViewAsCurrentMonthDay(day)
        }
    }

    private fun setViewAsCurrentMonthDay(day:NewDayModel) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setViewAsLastMonthDay() {
        with(binding) {
            mcFreePlaces.visibility = View.GONE
            ivProfile.visibility = View.GONE
            assistedDay.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
            tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setViewAsNextMonthDay(day:NewDayModel) {
        with(binding){
            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
            tvDay.visibility = View.VISIBLE
            assistedDay.visibility = View.GONE
            mcFreePlaces.visibility = if (day.isEnable) View.VISIBLE else View.GONE
            ivProfile.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
            tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey4)))
        }
    }


}