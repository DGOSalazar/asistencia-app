package com.example.myapplication.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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