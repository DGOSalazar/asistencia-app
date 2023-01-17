package com.example.myapplication.data.network

import android.net.Uri
import com.example.myapplication.data.models.LoginResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FirebaseServices @Inject constructor(
    private val firebase: FirebaseClient
){
    suspend fun login(email: String, pass: String): LoginResult = runCatching {
         firebase.auth.signInWithEmailAndPassword(email,pass).await()
     }.toLoginResult()

    suspend fun register(email: String, pass: String) = runCatching {
        firebase.auth.createUserWithEmailAndPassword(email,pass).await()
    }

    suspend fun uploadPhoto(uri:Uri)= runCatching {
        val imgName: StorageReference = firebase.dataStorage.child("image${uri.lastPathSegment}")
    }

    fun registerUserData(email: String, name: String, position: String="", birthDate: String, team: String, profilePhoto: String, phone: String) = run {
        firebase.dataBase.collection("Users").document(email).set(
            hashMapOf("name" to name,
                "position" to position,
                "birthDate" to birthDate,
                "team" to team,
                "profilePhoto" to profilePhoto,
                "phone" to phone)
        )
    }.isSuccessful

    private fun Result<AuthResult>.toLoginResult() =
        when (val result = getOrNull()){
            null -> LoginResult.Error
            else -> {
                val userId = result.user
                checkNotNull(userId)
                LoginResult.Success(result.user?.isEmailVerified ?: false)
            }
        }
}