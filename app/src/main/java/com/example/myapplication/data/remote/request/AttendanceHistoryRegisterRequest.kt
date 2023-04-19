package com.example.myapplication.data.remote.request

data class AttendanceHistoryRegisterRequest (
    val email:String,
    val date:String,
    val status:Int=2
)