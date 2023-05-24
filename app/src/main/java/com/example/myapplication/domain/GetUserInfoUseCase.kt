package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.data.remote.api.FirebaseApiService
import com.example.myapplication.data.remote.api.FirebaseServices
import com.example.myapplication.data.remote.response.UserHomeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebase: FirebaseApiService
) {
    suspend fun userInfo(
        listEmails: ArrayList<String>,
    ) =
        firebase.getUserInfo(listEmails)

    @RequiresApi(Build.VERSION_CODES.O)
   suspend fun emailList(day: String) =
        firebase.getListUsers(day)


}