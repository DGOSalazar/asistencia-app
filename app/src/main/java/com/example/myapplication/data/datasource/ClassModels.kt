package com.example.myapplication.data.datasource

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Login(
    val email: String,
    val isEmailVerified: Boolean
)

@Parcelize
data class UserRegister(
    var email: String = "",
    var password: String = "",

    var name: String = "",
    var lastName: String = "",
    var birthDate: String = "",
    var phone: String = "",

    var position: String = "",
    var team: String = "",
    var profilePhoto: String = "",
    var employeeNumber: Int = 0
) : Parcelable