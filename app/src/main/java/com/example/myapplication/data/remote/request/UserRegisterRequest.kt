package com.example.myapplication.data.remote.request

data class UserRegisterRequest(
    val email:String,
    val name:String,
    val lastName1:String,
    val lastName2:String,
    val position:String,
    val birthDate:String,
    val team:String,
    val profilePhoto:String,
    val employee:Int,
    val assistDay:String)