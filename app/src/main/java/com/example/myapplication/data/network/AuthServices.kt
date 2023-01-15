package com.example.myapplication.data.network

import com.example.myapplication.data.models.LoginResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthServices @Inject constructor(
    private val firebase: FirebaseClient
){
   suspend fun login(email: String, pass: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email,pass).await()
    }.toLoginResult()

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