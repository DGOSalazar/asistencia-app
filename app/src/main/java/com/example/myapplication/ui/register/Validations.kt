package com.example.myapplication.ui.register

import android.text.TextUtils
import javax.inject.Inject

class Validations @Inject constructor() {

    fun isValidEmail(email:String):Boolean{
        return  !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password1:String):Boolean{
        return password1.length >= 8
    }
    fun isValidText(text:String):Boolean{
        return  text.isNotEmpty()
    }
    fun isValidPhone(phone:String):Boolean{
        return  phone.length in 8..10
    }
}