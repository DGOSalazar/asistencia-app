package com.example.myapplication.data.models

import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.remote.response.UserHomeResponse

data class HomeInitialData(
    val remoteDays: Resource2<List<DayCollection>>?,
    val userList: Resource2<List<UserHomeResponse>>?,
    val userData: Resource2<UserHomeDomainModel>?
    )
