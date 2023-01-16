package com.example.myapplication.domain

import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.network.AuthServices
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authServices: AuthServices
) {
    suspend operator fun invoke(email: String, password: String): LoginResult =
        authServices.register(email, password)
}