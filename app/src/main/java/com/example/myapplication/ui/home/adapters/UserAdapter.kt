package com.example.myapplication.ui.home.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.core.dialog.UserDialog
import com.example.myapplication.databinding.ColaboratorViewBinding
import com.example.myapplication.data.models.User
import kotlinx.coroutines.NonDisposableHandle.parent

class UserAdapter(private var user: List<User>, private var click: (User) -> Unit): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private var mBinding= ColaboratorViewBinding.bind(view)
        fun mountUsers(){
            with(mBinding) {
                Glide.with(mBinding.ivUser.context).load(user[adapterPosition].profilePhoto).into(mBinding.ivProfile)
                tvName.text = "${user[adapterPosition].name} ${user[adapterPosition].lastName1}"
                tvPosition.text = user[adapterPosition].position
                bgOccupation.setBackgroundResource(getColorPosition(user[adapterPosition].position))
            }
        }

        private fun getColorPosition(position: String): Int {
            return when(position){
                "Android" -> {R.drawable.background_android}
                "IOS" -> {R.drawable.background_ios}
                "Analyst" -> {R.drawable.background_analyst}
                "Scrum Master" -> {R.drawable.background_sm}
                "Tester/QA" -> {R.drawable.background_qa}
                else ->{R.drawable.background_back}
            }
        }

        fun setClick(click: (User) -> Unit){
            mBinding.root.setOnClickListener {
                click(user[adapterPosition])
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
            setClick(click)
        }
    }
    override fun getItemCount(): Int = user.size
}