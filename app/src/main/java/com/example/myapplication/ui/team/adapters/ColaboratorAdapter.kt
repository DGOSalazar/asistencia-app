package com.example.myapplication.ui.team.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.ColaboratorViewLiteBinding

class ColaboratorAdapter(var listUsers: ArrayList<User>): RecyclerView.Adapter<ColaboratorAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var mBinding = ColaboratorViewLiteBinding.bind(view)
        fun setView(){
            mBinding.photo.glide(listUsers[adapterPosition].profilePhoto)
            mBinding.tvNme.text = listUsers[adapterPosition].name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.colaborator_view_lite,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            setView()
        }
    }

    override fun getItemCount(): Int = listUsers.size
}