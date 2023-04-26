package com.example.myapplication.data.models

data class NewDayModel (
    val date: String,
    val freePlaces:Int,
    val isEnable:Boolean = false
    )