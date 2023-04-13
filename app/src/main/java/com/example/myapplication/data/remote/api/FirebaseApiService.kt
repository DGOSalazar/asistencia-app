package com.example.myapplication.data.remote.api

import android.net.Uri
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.data.datasource.mappers.UserRegisterMapper
import com.example.myapplication.data.remote.request.UserRegisterRequest
import com.example.myapplication.data.remote.response.LoginResponse
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.data.statusNetwork.makeCall
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseApiService @Inject constructor(private val client: FirebaseClientModule) {
    suspend fun getLogin(email: String, password: String): ResponseStatus<LoginResponse> =
        makeCall {
            val result = client.auth.signInWithEmailAndPassword(email, password).await()
            val response = LoginResponse(
                email = result.user?.email.toString(),
                isEmailVerified = result.user!!.isEmailVerified
            )
            response
        }

    suspend fun sendRegisterUser(user: UserRegisterRequest): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
             client.userCollection.document(user.email)
                .set(user).addOnCompleteListener {
                    isSuccess= it.isSuccessful
                }.await()
            isSuccess
        }


    suspend fun sendUploadImage(uri: Uri): ResponseStatus<UploadTask.TaskSnapshot> =
        makeCall {
            val ref: StorageReference = client.storage.child("image${uri.lastPathSegment}")
            val uploadTask = ref.putFile(uri).await()
            uploadTask
        }

    suspend fun sendAuthRegister(
        email: String,
        password: String
    ): ResponseStatus<Boolean> =
        makeCall {
            var isSuccess = false
            client.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    isSuccess = it.isSuccessful
                }.await()
            isSuccess
        }
}
