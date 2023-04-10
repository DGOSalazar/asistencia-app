package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.R
import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.datasource.LoginDTO
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.datasource.mappers.LoginMapper
import com.example.myapplication.data.datasource.mappers.UserRegisterMapper
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.data.statusNetwork.makeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


interface FirebaseTask {
    suspend fun doLogin(email: String, pass: String): ResponseStatus<Login>
    suspend fun doUserRegister(user: UserRegister): ResponseStatus<Boolean>
    suspend fun doAuthRegister(email: String, pass: String): ResponseStatus<Boolean>
    suspend fun doUploadImage(uri: Uri): ResponseStatus<Uri>
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

    override suspend fun doUserRegister(user: UserRegister): ResponseStatus<Boolean> {
        return withContext(Dispatchers.IO) {
            val getRegisterUser = async { sendRegisterUser(user) }
            val getRegisterUserResponse = getRegisterUser.await()

            if (getRegisterUserResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getRegisterUserResponse.data)
            } else
                ResponseStatus.Error(R.string.problem_registering_user)
        }
    }

    override suspend fun doAuthRegister(email: String, pass: String): ResponseStatus<Boolean> {
        return withContext(Dispatchers.IO) {
            val registerAuth = sendAuthRegister(email, pass)
            registerAuth
        }
    }

    override suspend fun doUploadImage(uri: Uri): ResponseStatus<Uri> {
        return withContext(Dispatchers.IO) {
            val uploadImage = async { sendUpLoadImage(uri) }
            val getImageResponse = uploadImage.await()

            if (getImageResponse is ResponseStatus.Success) {
                if (getImageResponse.data.uploadSessionUri != null)
                    ResponseStatus.Success(getImageResponse.data.uploadSessionUri!!)
                else
                    ResponseStatus.Error(R.string.upload_image_error)
            } else {
                ResponseStatus.Error(R.string.upload_image_error)
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

    private suspend fun sendAuthRegister(email: String, pass: String): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    isSuccess = it.isSuccessful
                }.await()
            isSuccess
        }


    private suspend fun sendRegisterUser(user: UserRegister): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
            Firebase.firestore.collection("UsersCollection")
                .document(user.email)
                .set(UserRegisterMapper().fromDtoToDomain(user)
            ).addOnCompleteListener {
                isSuccess= it.isSuccessful
            }.await()
            isSuccess
        }

    private suspend fun sendUpLoadImage(uri: Uri): ResponseStatus<UploadTask.TaskSnapshot> =
        makeCall {
            val ref: StorageReference = firebaseStorage.child("image${uri.lastPathSegment}")
            val uploadTask = ref.putFile(uri).await()
            uploadTask
        }
}