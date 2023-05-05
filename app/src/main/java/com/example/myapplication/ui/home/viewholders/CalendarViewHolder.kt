package com.example.myapplication.ui.home.viewholders

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.core.utils.Constants
import com.example.myapplication.core.utils.Constants.FREE_PLACES_VALUE
import com.example.myapplication.data.models.NewDayModel
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.databinding.DayViewBinding

class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = DayViewBinding.bind(view)
    private val ctx = view.context
    private var isAssistedDay:Boolean = false


    fun setView(
        day: NewDayModel,
        currentDate:String,
        assistedDays:ArrayList<DayCollectionResponse>,
        userType:Int
    ){
        if (userType == 0){
            setViewAsSuperUser(day)
            return
        }

        val remoteDate = assistedDays.filter{ it.currentDay == day.date}
        isAssistedDay = if (remoteDate.isEmpty()) false else remoteDate[0].email!!.any { it == day.date}

        val dayType = day.date.compareTo(currentDate)

        when{
            dayType > 0 -> setViewAsNextMonthDay(day)
            dayType < 0 -> setViewAsLastMonthDay()
            else -> setViewAsCurrentMonthDay(day)
        }
    }

    private fun setViewAsSuperUser(day: NewDayModel) {
        with(binding) {
            ivProfile.visibility = View.GONE
            assistedDay.visibility = View.GONE
            freePlaces.text = ( FREE_PLACES_VALUE - day.freePlaces ).toString()
            tvDay.text = day.date.substring(0,2)
        }
    }

    private fun setViewAsCurrentMonthDay(day:NewDayModel) {

    }

    private fun setViewAsLastMonthDay() {
        with(binding) {
            mcFreePlaces.visibility = View.GONE
            ivProfile.visibility = View.GONE
            assistedDay.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
            tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
            container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
        }
    }

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