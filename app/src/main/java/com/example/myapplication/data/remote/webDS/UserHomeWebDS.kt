package com.example.myapplication.data.remote.webDS

import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class UserHomeWebDS @Inject constructor(private val service: FirebaseApiService) {
    suspend fun getInfoAllUsers(email: ArrayList<String>) = service.getUserInfo(email)
    suspend fun getListUser(day: String) = service.getListUser(day)

}