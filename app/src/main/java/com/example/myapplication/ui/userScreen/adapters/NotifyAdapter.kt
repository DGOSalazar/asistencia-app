package com.example.myapplication.ui.userScreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.Notify
import com.example.myapplication.databinding.NotificationListViewBinding

class NotifyAdapter(var notifyList : ArrayList<Notify>): RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var mBinding = NotificationListViewBinding.bind(view)

        fun mountView(){
            with(mBinding){
                ivIconNotify.setImageResource(notifyList[adapterPosition].iconNoty)
                tvNotiText.text = notifyList[adapterPosition].text
                tvTimer.text = notifyList[adapterPosition].timer
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.notification_list_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountView()
        }
    }
    override fun getItemCount(): Int = notifyList.size
}