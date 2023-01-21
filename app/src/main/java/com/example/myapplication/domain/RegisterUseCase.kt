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

    suspend fun registerUserData(email: String, name: String, position: String="", birthDate: String, team: String, profilePhoto: String, phone: String) =
        firebaseServices.registerUserData(
            User(email=email, name= name, position = position, birthDate = birthDate, team = team, profilePhoto = profilePhoto, phone = phone )
        )
}