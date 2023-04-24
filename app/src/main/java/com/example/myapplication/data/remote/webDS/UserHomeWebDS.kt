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
/*
    suspend fun getInfoAllUsers(email: ArrayList<String>): Resource<ArrayList<UserHomeDomainModel>> =
         withContext(Dispatchers.IO) {
             val response = async { service.getUserInfo(email) }
             val result = response.await()
             if (result is ResponseStatus.Success) {
                 val list = arrayListOf<UserHomeDomainModel>()
                 result.data?.forEach {
                     list.add(UserHomeMapper().map(it))
                 }
                 Resource.success(list)
             } else
                 Resource.error(R.string.error_get_all_users)
         }*/

    suspend fun getInfoAllUsers(email:ArrayList<String>) : Flow<Resource2<ArrayList<UserHomeDomainModel>>> =
        service.getUserInfo(email).map {
                var list = arrayListOf<UserHomeDomainModel>()
                it.data?.forEach {
                    list.add(UserHomeMapper().map(it))
                }
                Resource2.success(list)
        }
    suspend fun getListUser(day: String) =
        withContext(Dispatchers.IO) {
            val response = async { service.getListUser(day) }
            val result = response.await()
            if (result is ResponseStatus.Success) {
                Resource.success(result.data)
            } else {
                Resource.error(R.string.error_get_list_user)
            }
        }

    suspend fun getAllUsers() = withContext(Dispatchers.IO) {
        val response = async { service.getAllUsers() }
        val result = response.await()
        if (result is ResponseStatus.Success){
            Resource.success(result.data)
        }
        else{
            Resource.error(R.string.error_get_all_users)
        }
    }

}