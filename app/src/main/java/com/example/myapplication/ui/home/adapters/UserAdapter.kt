package com.example.myapplication.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ColaboratorViewBinding
import com.example.myapplication.data.models.User

class UserAdapter(private var user: List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private var mBinding= ColaboratorViewBinding.bind(view)
        fun mountUsers(){
            with(mBinding) {
                Glide.with(mBinding.ivUser.context).load(user[adapterPosition].profilePhoto).into(mBinding.ivProfile)
                tvName.text = user[adapterPosition].name
                tvPosition.text = ("         ${user[adapterPosition].position}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.colaborator_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountUsers()
        }
    }
    override fun getItemCount(): Int = user.size
}