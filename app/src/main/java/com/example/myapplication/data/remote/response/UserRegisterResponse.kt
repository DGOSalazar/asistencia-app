package com.example.myapplication.data.remote.response

data class UserRegisterResponse(
    val isSuccessImage:Boolean,
    val isSuccessData:Boolean,
    val isSuccessDoUser:Boolean,
    val message:String
)