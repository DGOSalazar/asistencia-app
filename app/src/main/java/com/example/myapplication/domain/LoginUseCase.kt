package com.example.myapplication.domain

import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val firebaseServices: FirebaseServices
) {
    suspend operator fun invoke(email: String, password: String): LoginResult =
        firebaseServices.login(email, password)
}