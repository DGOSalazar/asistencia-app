package com.example.myapplication.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewMode @Inject constructor(
    private val registerUseCase: RegisterUseCase
 ): ViewModel() {

    private val _registerFlag = MutableLiveData<Boolean>()
    val registerFlag:LiveData<Boolean> = _registerFlag

    fun register(name:String,pass:String){
            viewModelScope.launch {
                val result = registerUseCase.register(name,pass)
                _registerFlag.value=true
            }
    }
    fun saveUserData(name: String, birthdate: String, position: String, email: String, team: String, profilePhoto: String="url", phone: String){
            viewModelScope.launch {
                registerUseCase.registerUserData(email,name,position,birthdate,team,profilePhoto,phone)
            }
    }

}