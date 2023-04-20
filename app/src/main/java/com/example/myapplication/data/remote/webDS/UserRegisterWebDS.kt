package com.example.myapplication.data.remote.webDS

import android.net.Uri
import com.example.myapplication.R
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.remote.api.FirebaseApiService
import com.example.myapplication.data.remote.request.UserRegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRegisterWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun doUserRegister(userRegisterRequest: UserRegisterRequest) = service.sendRegisterUser(userRegisterRequest)



    suspend fun doAuthRegister(email: String, pass: String) = service.sendAuthRegister(email,pass)


    suspend fun doUploadImage(uri: Uri) = service.sendUploadImage(uri)


    suspend fun getAllPositions() = service.getAllPositions()
    suspend fun getAllTeams() = service.getAllTeams()


}