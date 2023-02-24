package com.example.myapplication.domain

import com.example.myapplication.data.models.Notify
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    val firebaseServices: FirebaseServices
) {
    suspend operator fun invoke(): ArrayList<Notify> = firebaseServices.getNotifications()
}