package com.example.myapplication.data.remote.webDS

import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class LoginWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun doLogin(email: String, password: String) = service.getLogin(email,password)
}


