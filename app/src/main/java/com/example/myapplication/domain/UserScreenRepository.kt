package com.example.myapplication.domain

import com.example.myapplication.data.models.ProjectsDomainModel
import com.example.myapplication.data.models.UserAdditionalData
import com.example.myapplication.data.remote.api.FirebaseApiService
import javax.inject.Inject

class UserScreenRepository @Inject constructor(
    private val firebaseRepository: FirebaseApiService
) {
    suspend fun getUser(email: String) = firebaseRepository.getUserData(email)

    //Additional Info
    suspend fun setAdditionalData(user:UserAdditionalData) = firebaseRepository.saveMoreUserData(user)
    suspend fun getAdditionalData(email: String) = firebaseRepository.getUserMoreData(email)

    //projects
    suspend fun getProjectUser(email: String) = firebaseRepository.getProjectsForUser(email)
   suspend fun saveProjectsByUser(projects: ProjectsDomainModel) =
        firebaseRepository.saveProjectsForUser(projects)
}