package com.example.myapplication.data.remote.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.FirebaseClientModule
import com.example.myapplication.data.models.*
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServices @Inject constructor(
    private val firebase: FirebaseClientModule
) {

    fun updateUsersList(currentDay: String, emails: ArrayList<String>) = run {
        firebase.dayCollection.document(currentDay).set(
            hashMapOf(
                "currentDay" to currentDay,
                "email" to emails
            )
        )
    }

    suspend fun getUserInfo(
        listEmail: ArrayList<String>,
        users: (ArrayList<UserHomeResponse>) -> Unit
    ) {
        val list = arrayListOf<UserHomeResponse>()
        listEmail.forEach { i ->
            val documents = firebase.userCollection.whereEqualTo("email", i).get().await().documents
            documents.forEach { d ->
                val netUser = d.toObject<UserHomeResponse>()
                list.add(netUser!!)
            }
            users(list)
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

    suspend fun getAllUsers(): ArrayList<UserHomeResponse>? {
        val list: ArrayList<UserHomeResponse> = arrayListOf()
        val snap = firebase.userCollection.get().await()
        val documents = snap.documents
        return documents?.let {
            it.forEach { document ->
                val netUser = document.toObject<UserHomeResponse>()
                netUser?.let { it1 -> list.add(it1) }
            }
            list
        }
    }

}