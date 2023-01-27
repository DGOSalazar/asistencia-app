package com.example.myapplication.data.models

data class Month(
    var daysList: ArrayList<Day>,
    var today: Int,
    var userAssistanceDays: List<Int>?= emptyList(),
    var freePlacesOfDays: List<Int>?= emptyList(),
    var daysToFormatNextMonth: Int = 0
)