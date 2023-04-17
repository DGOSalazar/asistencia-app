package com.example.myapplication.data.models

import com.example.myapplication.data.remote.response.UserHomeResponse


data class TeamGroup (var team: String = "",
                      var users : ArrayList<UserHomeResponse> = arrayListOf(),
                      var isSelected: Boolean = false
)