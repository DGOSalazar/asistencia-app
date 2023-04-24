package com.example.myapplication.data.remote.webDS

import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class TeamWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun getAllUsersTeams() = service.getAllUsers()
}