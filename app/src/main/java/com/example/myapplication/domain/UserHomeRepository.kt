package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.mappers.AttendanceDaysMapper
import com.example.myapplication.data.mappers.UserHomeMapper
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.webDS.UserHomeWebDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserHomeRepository @Inject constructor(
    private val webDS: UserHomeWebDS
) {
    suspend fun getUserInfo(listEmail: ArrayList<String>): Flow<Resource2<ArrayList<UserHomeDomainModel>>> =
        webDS.getInfoAllUsers(listEmail).map {
            var list = arrayListOf<UserHomeDomainModel>()
            it.data?.forEach {
                list.add(UserHomeMapper().map(it))
            }
            Resource2.success(list)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getEmailList(day: String): Flow<Resource2<ArrayList<String>>> =
        webDS.getListUser(day).map {
            var list = arrayListOf<String>()
            it.data?.let { data ->
                data.first().email.forEach { item ->
                    list.add(item)
                }
            }
            Resource2.success(list)
        }
}