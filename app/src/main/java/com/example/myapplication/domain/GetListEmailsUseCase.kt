package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetListEmailsUseCase @Inject constructor(
    private val firebase: FirebaseServices
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke (day: String, mails:(ArrayList<String>)->Unit) = firebase.getListUsers(day,mails)
}