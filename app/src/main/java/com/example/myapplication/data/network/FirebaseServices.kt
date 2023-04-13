package com.example.myapplication.data.network

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.*
import com.example.myapplication.core.utils.FirebaseClientModule
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServices @Inject constructor(
    private val firebase: FirebaseClientModule
) {
    private var url: Uri? = null
    private var isConfirmAssistAlready: Boolean = false

    suspend fun login(mail: String, pass: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(mail, pass).await()
    }.toLoginResult()

    suspend fun register(email: String, pass: String) = runCatching {
        firebase.auth.createUserWithEmailAndPassword(email, pass).await()
    }

    fun uploadPhoto(uri: Uri) = runCatching {
       /* val ref: StorageReference = firebase.dataStorage.child("image${uri.lastPathSegment}")
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
        }*/
    }

    suspend fun getUrlF(): Uri? = run {
        while (url == null) {
            delay(1000)
        }
        url
    }

    fun registerUserData(user: User) = run {
        firebase.userCollection.document(user.email).set(
            hashMapOf(
                "email" to user.email,
                "name" to user.name,
                "lastName1" to user.lastName1,
                "lastName2" to user.lastName2,
                "position" to user.position,
                "birthDate" to user.birthDate,
                "team" to user.team,
                "profilePhoto" to user.profilePhoto,
                "phone" to user.phone,
                "employee" to user.employee,
                "assistDay" to user.assistDay
            )
        )
    }.isSuccessful

    fun updateUsersList(currentDay: String, emails: ArrayList<String>) = run {
        firebase.dayCollection.document(currentDay).set(
            hashMapOf(
                "currentDay" to currentDay,
                "email" to emails
            )
        )
    }

    fun getUserInfo(listEmail: ArrayList<String>, user: (ArrayList<User>) -> Unit) = runCatching {
        var user1: User
        val list = arrayListOf<User>()
        for (i in listEmail) {
            firebase.userCollection.whereEqualTo("email", i).get().addOnSuccessListener {
                it.forEach { i ->
                    user1 = User(
                        i.get("email") as String,
                        i.get("name") as String,
                        i.get("lastName1") as String,
                        i.get("lastName2") as String,
                        i.get("position") as String,
                        i.get("birthDate") as String,
                        i.get("team") as String,
                        i.get("profilePhoto") as String,
                        i.get("phone") as String,
                        i.get("employee") as Long
                    )
                    list.add(user1)
                }
                user(list)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getListUsers(day: String, mails: (ArrayList<String>) -> Unit) = runCatching {
        var emails = AttendanceDays(arrayListOf(), "")
        firebase.dayCollection.whereEqualTo("currentDay", day).get()
            .addOnSuccessListener {
                it.forEach { j ->
                    emails = AttendanceDays(
                        j.get("email") as ArrayList<String>,
                        j.get("currentDay") as String
                    )
                }
                mails(emails.emails)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllRegistersDays(
        success: (List<AttendanceDays>) -> Unit,
        errorObserver: (String) -> Unit
    ): List<AttendanceDays> {
        val list = mutableListOf<AttendanceDays>()
        firebase.dayCollection
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    val day = AttendanceDays(
                        document.get("email") as ArrayList<String>,
                        document.get("currentDay") as String
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
        when (val result = getOrNull()) {
            null -> LoginResult.Error
            else -> {
                val userId = result.user
                checkNotNull(userId)
                LoginResult.Success(result.user?.isEmailVerified ?: false)
            }
        }

    fun registerUserConfirmationAssist(assistConfirm: AssistConfirm) = run {
        firebase.dayConfirmCollection.document(assistConfirm.day).set(
            hashMapOf(
                "day" to assistConfirm.day,
                "userAssist" to assistConfirm.users
            )
        )
    }

    fun consultUserConfirmationAssist(
        day: String,
        email: String,
        returnData: (List<AssistConfirm>) -> Unit
    ) {
        var dayConfirm: ArrayList<AssistConfirm> = arrayListOf()
        firebase.dayConfirmCollection.whereEqualTo("day", day).get()
            .addOnSuccessListener { result ->
                result.documents.forEach { j ->
                    var newRegister = AssistConfirm(
                        j.get("day") as String,
                        j.get("userAssist") as ArrayList<UserOk>
                    )
                    dayConfirm.add(newRegister)
                }

                returnData(dayConfirm)
            }.addOnFailureListener {
                returnData(listOf())
            }
    }

    suspend fun getNotifications(): ArrayList<Notify> {
        var notifications: ArrayList<Notify> = arrayListOf()
        firebase.dayCollection.get().addOnSuccessListener { result ->
            result.documents.forEach { j ->
                var newNotify = Notify(
                    iconNoty = j.get("iconNoty") as Int,
                    notifyId = j.get("notifyId") as String,
                    text = j.get("text") as String,
                    timer = j.get("timer") as String
                )
                notifications.add(newNotify)
            }
            notifications
            return@addOnSuccessListener
        }.addOnFailureListener {
            return@addOnFailureListener
        }
        while (notifications.isNullOrEmpty()) {
            delay(1000)
        }
        return notifications
    }

    fun getAllUsers(users: (ArrayList<User>) -> Unit) {
        val list: ArrayList<User> = arrayListOf()
        var user1 = User()
        firebase.userCollection.get().addOnSuccessListener {
            it.forEach { i ->
                user1 = User(
                    i.get("email") as String,
                    i.get("name") as String,
                    i.get("lastName1") as String,
                    i.get("lastName2") as String,
                    i.get("position") as String,
                    i.get("birthDate") as String,
                    i.get("team") as String,
                    i.get("profilePhoto") as String,
                    i.get("phone") as String,
                    i.get("employee") as Long
                )
                list.add(user1)
            }
            users(list)
        }
    }
}