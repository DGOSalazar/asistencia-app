package com.example.myapplication.domain

import com.example.myapplication.data.remote.api.FirebaseApiService
import com.example.myapplication.data.remote.webDS.UserHomeWebDS
import javax.inject.Inject

class UserHomeRepository  @Inject constructor(
    private val webDS: UserHomeWebDS
){
    suspend fun getUserInfo(listEmail:ArrayList<String>) = webDS.getInfoAllUsers(listEmail)

    suspend fun getEmailList(day:String) = webDS.getListUser(day)

    suspend fun getUsersByTeams() = webDS.getAllUsers()

}