package com.example.myapplication.domain

import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class EnrollUserToDayUseCase @Inject constructor(
    private val firebase : FirebaseServices
) {
    operator fun invoke(day: String, emails: ArrayList<String>) = firebase.updateUsersList(day,emails)
}

