package com.example.myapplication.data.models

data class ProjectsDomainModel(
    var email: String= "",
    var projectInfo: ArrayList<ProjectInfo> = arrayListOf(ProjectInfo())
)

data class ProjectInfo(
    var projectName: String ="",
    var releaseDate: String =""
)