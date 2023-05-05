package com.example.myapplication.domain

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.myapplication.core.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharePreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val edit: SharedPreferences.Editor
) {
    fun clearSharePreference() {
        edit.clear().apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveLogin(
        email: String,
        pass: String
    ) {
        edit.putString(Constants.EMAIL_KEY, email)
        edit.putString(Constants.PASSWORD_KEY, pass)
        edit.apply()
    }

    fun saveUserType(userType:Int){
        edit.putInt(Constants.USER_TYPE_KEY, userType)
    }

    fun getEmail(): String? =
        sharedPreferences.getString(Constants.EMAIL_KEY, "")

    fun getPassword(): String? =
        sharedPreferences.getString(Constants.PASSWORD_KEY, "")

    fun getUserType(): Int =
        sharedPreferences.getInt(Constants.USER_TYPE_KEY,0)
}