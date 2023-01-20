package com.example.myapplication.data.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Day(var num: Int?=12,
               var name: String="Lunes",
               var places: List<String> = listOf("","",""),
               var freePlaces: Boolean = false,
               var profilePhoto: Boolean=false,
               var isCurrentMonth: Boolean=true,
               var isToday: Boolean= false,
               var selected: Boolean= false) : Parcelable