package com.example.myapplication.domain

import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.User
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebase : FirebaseServices
) {
    operator fun invoke(listEmails:ArrayList<String>,users:(ArrayList<User>)->Unit) = firebase.getUserInfo(listEmails,users)
}