package com.example.myapplication.data.models


data class TeamGroup (var team: String = "",
                      var users : ArrayList<UserHomeDomainModel> = arrayListOf(),
                      var isSelected: Boolean = false
)