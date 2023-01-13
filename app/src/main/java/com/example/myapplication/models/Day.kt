package com.example.myapplication.models

data class Day(var num: Int?=12,
               var name: String="01",
               var places: List<String> = listOf("","",""),
               var freePlaces: Boolean = false,
               var profilePhoto: Boolean=false,
               var isCurrentMonth: Boolean=true,
               var isToday: Boolean= false)