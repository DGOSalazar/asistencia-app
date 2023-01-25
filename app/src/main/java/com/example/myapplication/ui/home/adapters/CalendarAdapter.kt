package com.example.myapplication.ui.home.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DayViewBinding
import com.example.myapplication.data.models.Day


const val FIRST_DAY_LAST_WEEK = 15
class CalendarAdapter(private var days: ArrayList<Day> = arrayListOf(),private var click:(Day)-> Unit ): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var today:Int = 0
    var isPastMonth:Boolean = false
    var assistedDays = emptyList<Int>()
    var statusMonth = 1

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

            val isAssistedDay = assistedDays.any { it == day.num }

            if ((day.num < today || isPastMonth) && adapterPosition < FIRST_DAY_LAST_WEEK){
                with(mBinding) {
                    mcFreePlaces.visibility = View.GONE
                    ivProfile.visibility = View.GONE
                    assistedDay.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
                    container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
                    tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
                }
            }else{
                with(mBinding){
                    val mcFreePlacesVisivility = if(day.freePlaces && !isAssistedDay && day.num != today && day.isCurrentMonth) View.VISIBLE else View.GONE
                    mcFreePlaces.visibility = mcFreePlacesVisivility

                    ivProfile.visibility = if (isAssistedDay && day.isCurrentMonth) View.VISIBLE else View.GONE
                    assistedDay.visibility = View.GONE

                    val bgColor = if(day.isToday) R.color.blue1 else R.color.white
                    container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(bgColor))

                    val dayBgColor = if(day.isCurrentMonth) R.color.grey2 else R.color.grey4
                    tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(dayBgColor)))

                    val freeDaysBgColor = if(day.places!=0) R.color.blue1 else R.color.blue2
                    freePlaces.backgroundTintList = ColorStateList.valueOf(ctx.getColor(freeDaysBgColor))
                    freePlaces.text = ctx.resources.getString(R.string.free_days, day.places)
                }
            }
            mBinding.tvDay.text = String.format("%02d", day.num)
        }

        fun setView(){
            val day = days[adapterPosition]
            val isAssistedDay = assistedDays.any { it == day.num }

            with(mBinding) {
                when(statusMonth){
                    0->{//past month
                        mcFreePlaces.visibility = View.GONE
                        ivProfile.visibility = View.GONE
                        assistedDay.visibility = if (isAssistedDay) View.VISIBLE else View.GONE
                        container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.grey4))
                        tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey5)))
                    }
                    1->{//current month
                        mcFreePlaces.visibility = if(!isAssistedDay && day.num != today && day.num > today) View.VISIBLE else View.GONE

                        val freeDaysBgColor = if(day.places!=0) R.color.blue1 else R.color.blue2
                        freePlaces.backgroundTintList = ColorStateList.valueOf(ctx.getColor(freeDaysBgColor))
                        freePlaces.text = ctx.resources.getString(R.string.free_days, day.places)

                        ivProfile.visibility = if (isAssistedDay && day.isCurrentMonth && day.num > today) View.VISIBLE else View.GONE
                        assistedDay.visibility = View.GONE

                        val bgColor = if (day.num > today)
                            if(day.isToday) R.color.blue1 else R.color.white else R.color.grey4

                        val dayBgColor = if(day.isCurrentMonth) R.color.grey2 else R.color.grey4
                        tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(dayBgColor)))
                        tvDay.visibility=View.VISIBLE

                        container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(bgColor))
                    }
                    2->{//next month
                        container.backgroundTintList = ColorStateList.valueOf(ctx.getColor(R.color.white))
                        tvDay.setTextColor(ColorStateList.valueOf(ctx.getColor(R.color.grey2)))
                        mBinding.tvDay.visibility = View.VISIBLE
                        assistedDay.visibility = View.GONE
                        ivProfile.visibility = View.GONE
                        mcFreePlaces.visibility= View.GONE
                    }
                    else->{}
                }
                mBinding.tvDay.text = String.format("%02d", day.num)
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
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_view,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountCalendar()
            //setView()
            getDay(click)
        }
    }

    override fun getItemCount(): Int = days.size

}