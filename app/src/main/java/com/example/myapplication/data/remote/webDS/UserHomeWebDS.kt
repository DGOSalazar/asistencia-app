package com.example.myapplication.data.remote.webDS

import com.example.myapplication.R
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.datasource.mappers.UserDataMapper
import com.example.myapplication.data.mappers.LoginResponseMapper
import com.example.myapplication.data.mappers.UserHomeMapper
import com.example.myapplication.data.models.LoginDomainModel
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.api.FirebaseApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserHomeWebDS @Inject constructor(private val service: FirebaseApiService) {


    suspend fun getInfoAllUsers(email: ArrayList<String>) = service.getUserInfo(email)
    suspend fun getListUser(day: String) = service.getListUser(day)

}