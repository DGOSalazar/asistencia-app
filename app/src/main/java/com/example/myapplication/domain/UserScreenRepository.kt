package com.example.myapplication.domain


import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class UserScreenRepository @Inject constructor(
    private val firebaseRepository: FirebaseApiService
) {
    suspend fun getUser(email: String) = firebaseRepository.getUserData(email)

    suspend fun changeData() {}

    suspend fun changePhoto() {}
}