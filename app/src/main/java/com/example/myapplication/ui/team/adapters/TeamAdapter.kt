package com.example.myapplication.ui.team.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.databinding.TeamViewBinding
import com.example.myapplication.data.models.User

class TeamAdapter(var listTeams: ArrayList<TeamGroup>, private var click : (String) -> Unit)
    :RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var selected :Int = 0
        var mBinding = TeamViewBinding.bind(view)
        private val param = mBinding.clTeam.layoutParams as ViewGroup.MarginLayoutParams
        lateinit var mAdapter: ColaboratorAdapter

        fun setView(){
            setTitle()
            if (adapterPosition == 5){
                with(mBinding) {
                    listTeams[5].isSelected = true
                    vFix.visibility = View.GONE
                    rvUsers.visibility = View.VISIBLE
                    param.setMargins(0, 0, 0, 0)
                    clTeam.layoutParams = param
                    launchAdapter(listTeams[adapterPosition].users)
                }
            }else showUsers()
        }

        private fun setTitle() {
            mBinding.tvPositionTeam.apply {
                text = String.format(resources.getString(R.string.titleTeamsText), "    ",
                    listTeams[adapterPosition].team, listTeams[adapterPosition].users.size)
                setBackgroundColor(resources.getColor(setColor(adapterPosition)))
            }
        }
        private fun setColor(position: Int): Int{
            return when(position){
                0 -> {R.color.analyst_color}
                1 -> {R.color.scrum_color}
                2 -> {R.color.ios_color}
                3 -> {R.color.android_color}
                4 -> {R.color.qa_color}
                else -> {R.color.back_color}
            }
        }

        private fun showUsers(){
            with(mBinding) {
                if (listTeams[adapterPosition].isSelected) {
                    iconDown.setImageResource(R.drawable.ic_up_arro)
                    rvUsers.visibility = View.VISIBLE
                    launchAdapter(listTeams[adapterPosition].users)
                } else {
                    iconDown.setImageResource(R.drawable.ic_down_arrow)
                    rvUsers.visibility = View.GONE
                    vFix.visibility = View.VISIBLE
                    param.setMargins(0, 0, 0, -340)
                    clTeam.layoutParams = param
                }
            }
        }
        fun setClick(click:(String)-> Unit){
            mBinding.tvPositionTeam.setOnClickListener {
                click(listTeams[adapterPosition].team)
                listTeams[adapterPosition].isSelected = !listTeams[adapterPosition].isSelected
                notifyDataSetChanged()
            }
        }
        private fun launchAdapter(users : ArrayList<User>){
            mAdapter = ColaboratorAdapter(users)
            mBinding.rvUsers.apply {
                adapter = mAdapter
                layoutManager = GridLayoutManager(mBinding.root.context,2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.team_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            setView()
            selected = adapterPosition
            setClick(click)
        }
    }

    override fun getItemCount(): Int = listTeams.size
}