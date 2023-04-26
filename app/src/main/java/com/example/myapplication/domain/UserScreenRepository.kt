package com.example.myapplication.domain


import com.example.myapplication.data.models.UserAdditionalData
import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class UserScreenRepository @Inject constructor(
    private val firebaseRepository: FirebaseApiService
) {
    suspend fun getUser(email: String) = firebaseRepository.getUserData(email)

    fun setAdditionalData(user:UserAdditionalData) = firebaseRepository.saveMoreUserData(user)

    suspend fun getAdditionalData(email: String) = firebaseRepository.getUserMoreData(email)

    //fun addProjectsUser(user:UserAdditionalData) = firebaseRepository.saveProjectsForUser(user)

    suspend fun getProjectUser(email: String) = firebaseRepository.getProjectsForUser(email)
}