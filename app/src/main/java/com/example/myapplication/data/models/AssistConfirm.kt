package com.example.myapplication.data.models

data class AssistConfirm(var day : String ="",
                         var users : ArrayList<UserOk> = arrayListOf())

data class UserOk(var email: String = "",
                  var lat: Double = .0,
                  var lon: Double= .0,
                  var isInOffice: Boolean = true)
