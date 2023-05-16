package com.example.myapplication.ui.userScreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.models.ProjectsDomainModel
import com.example.myapplication.databinding.ProjectListViewBinding

class ProjectsAdapter(
    var projectList : ProjectsDomainModel,
    var fieldName : String,
    var fieldQ : String
)
    : RecyclerView.Adapter<ProjectsAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var mBinding = ProjectListViewBinding.bind(view)

        fun mountView(){
            with(mBinding){
                tvNotiText.text  = String.format(fieldName,projectList.projectInfo[adapterPosition].projectName)
                tvTimer.text = String.format(fieldQ,projectList.projectInfo[adapterPosition].releaseDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.project_list_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            mountView()
        }
    }
    override fun getItemCount(): Int = projectList.projectInfo.size
}