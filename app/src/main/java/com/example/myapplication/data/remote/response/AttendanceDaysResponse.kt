package com.example.myapplication.data.remote.response

data class AttendanceDaysResponse(
    val email: ArrayList<String> = arrayListOf(),
    val currentDay:String = ""
)