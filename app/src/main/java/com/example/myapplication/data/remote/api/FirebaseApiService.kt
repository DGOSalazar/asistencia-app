package com.example.myapplication.data.remote.api

import android.net.Uri
import android.util.Log
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.core.utils.statusNetwork.makeCall
import com.example.myapplication.data.models.User
import com.example.myapplication.data.remote.request.UserRegisterRequest
import com.example.myapplication.data.remote.response.AttendanceDaysResponse
import com.example.myapplication.data.remote.response.LoginResponse
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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

    suspend fun sendRegisterUser(user: UserRegisterRequest) = makeCall {
        var isSuccess = false
        client.userCollection.document(user.email).set(user).addOnCompleteListener {
            isSuccess = it.isSuccessful
        }.await()
        isSuccess
    }


    suspend fun sendUploadImage(uri: Uri): ResponseStatus<Uri> = makeCall {
        val ref: StorageReference = client.storage.child("image${uri.lastPathSegment}")
        val uploadTask = ref.putFile(uri).await()
        uploadTask.storage.downloadUrl.await()
    }

    suspend fun sendAuthRegister(
        email: String, password: String
    ): ResponseStatus<Boolean> = makeCall {
        var isSuccess = false
        client.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            isSuccess = it.isSuccessful
        }.await()
        isSuccess
    }

    suspend fun getUserData(email: String) : Resource<User> {
        var user = User()
        var response: Resource<User>
        var doc = client.userCollection.whereEqualTo("email", email).get().await()
        doc.forEach { data->
            user = data.toObject()
        }
        if(user == User()) {
            response = Resource.error(101)
            Log.d("error","don t get data")
        }
        else response = Resource.success(user)
        return response
    }

    suspend fun getUserInfo(
        listEmail: ArrayList<String>,
    ) = flow {
        val list = arrayListOf<UserHomeResponse>()
        emit(listEmail.let {
            listEmail.forEach { i ->
                val documents =
                    client.userCollection.whereEqualTo("email", i).get().await().documents
                documents.forEach { d ->
                    val netUser = d.toObject<UserHomeResponse>()
                    list.add(netUser!!)
                }
            }
            Resource2.success(list)
        })
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun getListUser(day: String) = makeCall {
        var emails = AttendanceDaysResponse(arrayListOf(), "")
        val documents = client.dayCollection.whereEqualTo("currentDay", day).get().await().documents
        documents.forEach {
            it.toObject<AttendanceDaysResponse>()?.let {
                emails = it
            }
        }
        emails.email
    }

    suspend fun getAllUsers() = makeCall {
        val list: ArrayList<UserHomeResponse> = arrayListOf()
        val documents = client.userCollection.get().await().documents
        documents?.let {
            it.forEach { document ->
                val netUser = document.toObject<UserHomeResponse>()
                netUser?.let { it1 -> list.add(it1) }
            }
            list
        }

    }

    /*
        suspend fun getAllTeams(){} = makeCall2 {
            var list: ArrayList<String> = arrayListOf()
            val documents = client.teamsCollection.get().await().documents
            documents.let {
                it.forEach {
                    it.toObject<String>()?.let {
                        list.add(it)
                    }
                }
                list
            }
        }
    */
    suspend fun getAllPositions() = flow {
        emit(Resource2.success(client.positionCollection.get().await().documents.let {
            var list = arrayListOf<String>()
            it.forEach { doc ->
                list.add(doc.get("Position") as String)
            }
            list
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun getAllTeams() = flow {
        emit(Resource2.success(client.teamsCollection.get().await().documents.let {
            var list = arrayListOf<String>()
            it.forEach { doc ->
                list.add(doc.get("Team") as String)
            }
            list
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

}
