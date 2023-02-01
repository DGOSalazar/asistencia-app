package com.example.myapplication.domain

import android.net.Uri
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

    fun upPhoto(uri: Uri) = firebaseServices.uploadPhoto(uri)

    suspend fun getPhoto() = firebaseServices.getUrlF()
}