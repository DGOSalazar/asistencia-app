package com.example.myapplication.domain

import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.models.User
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val firebaseServices: FirebaseServices
) {
    suspend fun register(email: String, password: String) =
        firebaseServices.register(email, password)

    fun registerUserData(user: User) =
        firebaseServices.registerUserData(user)
}