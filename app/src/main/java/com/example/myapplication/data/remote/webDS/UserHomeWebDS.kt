package com.example.myapplication.data.remote.webDS

import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.mappers.UserHomeMapper
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.api.FirebaseApiService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserHomeWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun getInfoAllUsers() = flow {
        emit(
            service.getNewUserInfo().map {
                if (it.status == Status.SUCCESS) {
                    Resource2.success(it.data)
                } else
                    Resource2.error(it.message)
            }
        )
    }
    suspend fun getDays() = flow {
        emit(
            service.getAllRegisterDays().map {
                if (it.status == Status.SUCCESS) {
                    Resource2.success(it.data)
                } else
                    Resource2.error(it.message)
            }
        )
    }

}