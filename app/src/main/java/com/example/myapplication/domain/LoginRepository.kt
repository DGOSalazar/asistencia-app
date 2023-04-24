package com.example.myapplication.domain

import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.mappers.LoginResponseMapper
import com.example.myapplication.data.models.LoginDomainModel
import com.example.myapplication.data.remote.webDS.LoginWebDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepository @Inject constructor(private val webDS: LoginWebDS) {
    suspend fun doLogin(email: String, password: String): Flow<Resource2<LoginDomainModel>> =
        webDS.doLogin(email, password).map {
            if(it.status==Status.SUCCESS)
                Resource2.success(LoginResponseMapper().map(it.data!!))
            else
                Resource2.error(it.message)
        }
}