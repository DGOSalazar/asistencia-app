package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.User
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebase : FirebaseServices
) {
    fun userInfo(listEmails:ArrayList<String>,users:(ArrayList<User>)->Unit) = firebase.getUserInfo(listEmails,users)
    @RequiresApi(Build.VERSION_CODES.O)
    fun emailList (day: String, mails:(ArrayList<String>)->Unit) = firebase.getListUsers(day,mails)
}