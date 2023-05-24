package com.example.myapplication.data.remote.api

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.core.utils.statusNetwork.flowCall
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.ProjectsDomainModel
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.UserAdditionalData
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


    suspend fun getLogin(email: String, password: String) = flowCall {
        client.auth.signInWithEmailAndPassword(email, password).await().let {
            LoginResponse(
                email = it.user?.email.toString(),
                isEmailVerified = it.user!!.isEmailVerified
            )
        }
    }

    suspend fun sendRegisterUser(user: UserRegisterRequest) =
        flowCall {
            client.userCollection.document(user.email).set(user).let {
                var isSuccess = false
                it.addOnCompleteListener {
                    isSuccess = it.isSuccessful
                }.await()
                isSuccess
            }
        }

    suspend fun sendUploadImage(uri: Uri) = flowCall {
        client.storage.child("image${uri.lastPathSegment}").let {
            it.putFile(uri).let { task ->
                task.await().storage.downloadUrl.await()
            }
        }
    }

    suspend fun sendAuthRegister(
        email: String, password: String
    ) = flowCall {
        client.auth.createUserWithEmailAndPassword(email, password).let {
            var isSuccess = false
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            isSuccess
        }
    }

    //UserRepository; Left add flows and mappers
    suspend fun getUserData(email: String) = flowCall {
        var user = User()
        client.userCollection.whereEqualTo("email", email).get().await().let { doc ->
            doc.forEach {
                user = it.toObject()
            }
            user
        }
    }

    suspend fun saveMoreUserData(user: UserAdditionalData) = flowCall {
        client.userMoreDataCollection.document(user.email).set(user).let {
            var isSuccess = false
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            isSuccess
        }
    }

    suspend fun getUserMoreData(email: String) = flow {
        var user = UserAdditionalData()
        emit(
            Resource2.success(
                client.userMoreDataCollection.whereEqualTo("email", email).get().await()
                    .let { doc ->
                        doc.forEach { data ->
                            user = data.toObject()
                        }
                        user
                    })
        )
    }.catch {
        emit(Resource2.error("error001", null))
    }

    fun deleteProfile(user: User): Resource<Unit> {
        var resource = Resource.success(Unit)
        val storageRef = client.storage.storage.reference
        val desertRef = storageRef.child(user.profilePhoto)

        desertRef.delete().addOnSuccessListener {
            resource
        }.addOnFailureListener {
            resource = Resource.error(101)
        }
        return resource
    }

    suspend fun saveProjectsForUser(projects: ProjectsDomainModel) = flowCall {
        client.userProjectsDoneCollection.document(projects.email).set(projects).let {
            var isSuccess = false
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            isSuccess
        }
    }


    suspend fun getProjectsForUser(email: String) = flowCall {
        var listRes = ProjectsDomainModel()
        client.userProjectsDoneCollection.whereEqualTo("email", email).get()
            .await().let { doc ->
                doc.forEach { data ->
                    listRes = data.toObject()
                }
                listRes
            }
    }

    suspend fun getUserInfo(
        listEmail: ArrayList<String>,
    ) = flowCall {
        val list = arrayListOf<UserHomeResponse>()
        listEmail.let {
            listEmail.forEach { i ->
                val documents =
                    client.userCollection.whereEqualTo("email", i).get().await().documents
                documents.forEach { d ->
                    val netUser = d.toObject<UserHomeResponse>()
                    list.add(netUser!!)
                }
            }
            list
        }
    }

    suspend fun getListUser(day: String) = flowCall {
        client.dayCollection.whereEqualTo("currentDay", day).get().await().map {
            it.toObject(AttendanceDaysResponse::class.java)
        }
    }

    suspend fun getAllUsers() = flowCall {
        client.userCollection.get().await().documents.mapNotNull {
            it.toObject(UserHomeResponse::class.java)
        }
    }

    suspend fun getAllPositions() = flowCall {
        client.positionCollection.get().await().documents.let {
            var list = arrayListOf<String>()
            it.forEach { doc ->
                list.add(doc.get("Position") as String)
            }
            list
        }
    }

    suspend fun getAllTeams() = flowCall {
        client.teamsCollection.get().await().documents.let {
            var list = arrayListOf<String>()
            it.forEach { doc ->
                list.add(doc.get("Team") as String)
            }
            list
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
   suspend fun getListUsers(day: String) = flowCall {
        var emails = AttendanceDays(arrayListOf(), "")
        client.dayCollection.whereEqualTo("currentDay", day).get().await().documents.let {
                it.forEach { j ->
                    emails = AttendanceDays(
                        j.get("email") as ArrayList<String>,
                        j.get("currentDay") as String
                    )
                }
                emails.emails
            }
    }


}
