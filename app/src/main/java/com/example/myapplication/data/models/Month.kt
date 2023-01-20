package com.example.myapplication.data.models

data class Month(
    val daysList: ArrayList<Day>,
    val today: Int,
    val pastMonth:Boolean
)