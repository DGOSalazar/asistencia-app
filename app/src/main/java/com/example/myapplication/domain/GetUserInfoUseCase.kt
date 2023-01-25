package com.example.myapplication.domain

import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebase : FirebaseServices
) {
    operator fun invoke() = firebase.getUserInfo()
}