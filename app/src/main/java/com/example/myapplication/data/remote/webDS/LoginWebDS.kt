package com.example.myapplication.data.remote.webDS

import com.example.myapplication.R
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.data.remote.api.FirebaseApiService
import com.example.myapplication.data.remote.response.LoginResponse
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.mappers.LoginResponseMapper
import com.example.myapplication.data.models.LoginDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun doLogin(email: String, password: String): Resource<LoginDomainModel> =
        withContext(Dispatchers.IO) {
            val getLoginFeared = async { service.getLogin(email, password) }
            val getLoginResponse = getLoginFeared.await()
            if (getLoginResponse is ResponseStatus.Success) {
                Resource.success(LoginResponseMapper().map(getLoginResponse.data))
            } else {
                Resource.error(R.string.user_not_exist)
            }
        }
}


