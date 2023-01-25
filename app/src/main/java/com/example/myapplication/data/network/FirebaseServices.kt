package com.example.myapplication.data.network

import android.net.Uri
import android.util.Log
import com.example.myapplication.data.models.AttendanceDays
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.models.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServices @Inject constructor(
    private val firebase: FirebaseClient
){
    private var url: Uri? =null

    suspend fun login(email: String, pass: String): LoginResult = runCatching {
         firebase.auth.signInWithEmailAndPassword(email,pass).await()
     }.toLoginResult()

    suspend fun register(email: String, pass: String) = runCatching {
        firebase.auth.createUserWithEmailAndPassword(email,pass).await()
    }

    fun uploadPhoto(uri:Uri) = runCatching {
        val ref: StorageReference = firebase.dataStorage.child("image${uri.lastPathSegment}")
        val uploadTask = ref.putFile(uri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnFailureListener {
            Log.d("Upload", "error to upload image")
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                url = task.result!!
                Log.d("Upload", "successfully upload")
            }
        }
    }
    suspend fun getUrlF(): Uri? = run {
        while (url==null){
            delay(1000)
        }
        url
    }

    fun registerUserData(user: User) = run {
        firebase.userCollection.document(user.email).set(
            hashMapOf("name" to user.name,
                "lastName1" to user.lastName1,
                "lastName2" to user.lastName2,
                "position" to user.position,
                "birthDate" to user.birthDate,
                "team" to user.team,
                "profilePhoto" to user.profilePhoto,
                "phone" to user.phone,
                "employee" to user.employee,
                "assistDay" to user.assistDay)
        )
    }.isSuccessful

    fun registerUserOnSelectedDay(email: String, currentDay: Day){
        firebase.dayCollection.document(email).set(
            hashMapOf(
                "email" to email,
                "currentDay" to currentDay
            )
        )
    }

    fun getCurrentRegisters(date: String, currentDay: Day): Query = run {
        return firebase.dayCollection.whereEqualTo(date,currentDay)
    }

    fun getAllRegistersDays(
        success:(List<AttendanceDays>) -> Unit,
        errorObserver:(String) -> Unit
    ):List<AttendanceDays> {
        val list = mutableListOf<AttendanceDays>()

        firebase.dayCollection
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    val day = AttendanceDays(
                        document.get("email") as ArrayList<String>,
                        document.get("currentDay") as String,
                    )
                    list.add(day)
                }
                success(list)
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents.", exception)
                exception.message?.let { errorObserver(it) }
            }
        return list
    }

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