package com.example.myapplication.domain

import com.example.myapplication.data.models.Day
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject


class EnrollUserToDayUseCase @Inject constructor(
    private val firebase : FirebaseServices
) {
    operator fun invoke(email: String, day: Day) = firebase.registerUserOnSelectedDay(email, day)
}

