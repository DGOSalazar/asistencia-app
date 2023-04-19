package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.mappers.UserRequestMapper
import com.example.myapplication.data.remote.webDS.UserRegisterWebDS
import javax.inject.Inject

class UserRegisterRepository @Inject constructor(private val webDS: UserRegisterWebDS) {
    suspend fun doAuthRegister(email: String, password: String)=
        webDS.doAuthRegister(email, password)

    suspend fun doUserRegister(user: UserRegister) = webDS.doUserRegister(UserRequestMapper().map(user))

    suspend fun doUploadImage(uri: Uri) = webDS.doUploadImage(uri)

    suspend fun getAllPositions() = webDS.getAllPositions()

    suspend fun getAllTeams() = webDS.getAllTeams()
}