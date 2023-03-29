package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.R
import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.datasource.LoginDTO
import com.example.myapplication.data.datasource.mappers.LoginMapper
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.data.statusNetwork.makeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


interface FirebaseTask {
    suspend fun doLogin(email: String, pass: String): ResponseStatus<Login>
}

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: StorageReference,
) :
    FirebaseTask {

    override suspend fun doLogin(email: String, pass: String): ResponseStatus<Login> {
        return withContext(Dispatchers.IO) {
            val getLoginFeared = async { getDoLogin(email, pass) }
            val getLoginResponse = getLoginFeared.await()

            if (getLoginResponse is ResponseStatus.Success) {
                val loginMapper = LoginMapper()
                val data = loginMapper.fromDtoToDomain(getLoginResponse.data)
                ResponseStatus.Success(data)
            } else {
                ResponseStatus.Error(R.string.user_not_exist)
            }
        }
    }

    private suspend fun getDoLogin(email: String, pass: String): ResponseStatus<LoginDTO> =
        makeCall {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val modelDTO =
                LoginDTO(
                    email = result.user?.email.toString(),
                    isEmailVerified = result.user?.isEmailVerified ?: false
                )
            modelDTO
        }
}