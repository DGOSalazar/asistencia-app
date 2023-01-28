package com.example.myapplication.ui.login

import android.text.TextUtils

class ValidationHelper {

    fun isValidText(text:String):Boolean{
        text.length > 10
        return true
    }

    fun isValidPassword(password:String):Boolean{
        return password.length > 8
    }

    fun isValidEmail(email:String):Boolean{
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone:String):Boolean{
        return phone.length == 10
    }

    fun isValidCollaboratorNumber(number:String):Boolean{
        return number.length > 8
    }

}