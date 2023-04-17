package com.example.myapplication.domain

import com.example.myapplication.data.remote.webDS.LoginWebDS
import javax.inject.Inject

class LoginRepository @Inject constructor(private val webDS: LoginWebDS) {
    suspend fun doLogin(email: String, password: String) = webDS.doLogin(email, password)
}