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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseApiService @Inject constructor(private val client: FirebaseClientModule) {
    suspend fun getLogin(email: String, password: String) = flow {
        emit(Resource2.success(client.auth.signInWithEmailAndPassword(email, password).await().let {
            LoginResponse(
                email = it.user?.email.toString(),
                isEmailVerified = it.user!!.isEmailVerified
            )
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error("error"))
        }
    }


    suspend fun sendRegisterUser(user: UserRegisterRequest) = flow {
        emit(client.userCollection.document(user.email).set(user).let {
            var isSuccess = false
            it.addOnCompleteListener {
                isSuccess = it.isSuccessful
            }.await()
            Resource2.success(isSuccess)
        })
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun sendUploadImage(uri: Uri) = flow {
        emit(
            client.storage.child("image${uri.lastPathSegment}").let {
                it.putFile(uri).let { task ->
                    Resource2.success(task.await().storage.downloadUrl.await())
                }
            }
        )
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun sendAuthRegister(
        email: String, password: String
    ) = flow {
        emit(client.auth.createUserWithEmailAndPassword(email, password).let {
            var isSuccess = false
            it.addOnCompleteListener {
                isSuccess = it.isSuccessful
            }.await()
            Resource2.success(isSuccess)
        })
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
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

    suspend fun getListUser(day: String) = flow {
        emit(
            Resource2.success(
                client.dayCollection.whereEqualTo("currentDay", day).get().await().map {
                    it.toObject(AttendanceDaysResponse::class.java)
                })
        )
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }


    suspend fun getAllUsers() = flow {
        emit(Resource2.success(client.userCollection.get().await().documents.mapNotNull {
            it.toObject(UserHomeResponse::class.java)
        }
        ))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    fun getAllPositions() = flow {
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
