package com.example.myapplication.data.models

sealed class LoginResult {
    object Error : LoginResult()
    data class Success(var okResult : Boolean) : LoginResult()
}