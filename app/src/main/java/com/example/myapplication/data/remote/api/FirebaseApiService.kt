package com.example.myapplication.data.remote.api

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.ProjectsDomainModel
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.UserAdditionalData
import com.example.myapplication.data.remote.request.UserRegisterRequest
import com.example.myapplication.data.remote.response.AttendanceDaysResponse
import com.example.myapplication.data.remote.response.DayCollectionResponse
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
            emit(Resource2.error(it))
        }
    }


    suspend fun sendRegisterUser(user: UserRegisterRequest) =
        flow {
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
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            Resource2.success(isSuccess)
        })
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    //UserRepository; Left add flows and mappers
    suspend fun getUserData(email: String) = flow {
        var user = User()
        emit(Resource2.success(client.userCollection.whereEqualTo("email", email).get().await().let { doc->
            doc.forEach {
               user = it.toObject()
            }
            user
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it,null))
        }
    }

    suspend fun saveMoreUserData(user: UserAdditionalData)
    = flow {
        emit(client.userMoreDataCollection.document(user.email).set(user).let {
            var isSuccess = false
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            Resource2.success(isSuccess)
        })
        }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun getUserMoreData(email: String) = flow {
        var user = UserAdditionalData()
        emit(Resource2.success(client.userMoreDataCollection.whereEqualTo("email", email).get().await().let { doc->
            doc.forEach { data->
                user = data.toObject()
            }
            user
        }))
    }.catch {
        emit(Resource2.error("error001",null))
    }

    fun deleteProfile(user: User): Resource<Unit>{
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

    fun saveProjectsForUser(projects :ProjectsDomainModel) = flow {
        emit(client.userProjectsDoneCollection.document(projects.email).set(projects).let {
            var isSuccess = false
            it.addOnCompleteListener { done ->
                isSuccess = done.isSuccessful
            }.await()
            Resource2.success(isSuccess)
        })
     }.catch { error ->
         error.message?.let {
             emit(Resource2.error(it))
         }
    }


    suspend fun getProjectsForUser(email: String) = flow{
        var listRes = ProjectsDomainModel()
        emit(Resource2.success(client.userProjectsDoneCollection.whereEqualTo("email", email).
        get().await().let { doc ->
            doc.forEach { data ->
                listRes = data.toObject()
            }
            listRes
        }))
    }.catch {
            error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
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
        }))
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

    suspend fun getAllRegisterDays() = flow {
        emit(Resource2.success(client.dayCollection.get().await().documents.let {
            val list = arrayListOf<DayCollectionResponse>()
            it.forEach { doc ->
                doc.toObject<DayCollectionResponse>()?.let { it1 -> list.add(it1) }
            }
            list
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

    suspend fun getNewUserInfo() = flow {
        emit(Resource2.success(
            client.userCollection.get().await().documents.let {
                val list = arrayListOf<UserHomeResponse>()
                it.forEach { doc ->
                    doc.toObject<UserHomeResponse>()?.let { it1 -> list.add(it1) }
                }
                list
        }))
    }.catch { error ->
        error.message?.let {
            emit(Resource2.error(it))
        }
    }

}
