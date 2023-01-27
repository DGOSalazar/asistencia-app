package com.example.myapplication.data.models

data class AttendanceDays(
    val emails:ArrayList<String>,
    val currentDay:String,
    var day:Int=0,
    var freePlaces:Int=1
)
