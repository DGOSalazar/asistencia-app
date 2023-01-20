package com.example.myapplication.data.models

data class Day(var num: Int=12,
               var name: String="01",
               var places: Int=0,
               var freePlaces: Boolean = false,
               var profilePhoto: String= "",
               var isCurrentMonth: Boolean=true,
               var isToday: Boolean= false)