package com.example.myapplication.ui.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.User
import com.example.myapplication.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewMode @Inject constructor(
    private val registerUseCase: RegisterUseCase
 ): ViewModel() {

    private val _registerFlag = MutableLiveData<Boolean>()
    private var _urlPhoto = MutableLiveData<Uri?>()
    val registerFlag:LiveData<Boolean> = _registerFlag
    var urlPhoto: LiveData<Uri?> = _urlPhoto

    fun register(name:String,pass:String){
            viewModelScope.launch {
                registerUseCase.register(name,pass)
                _registerFlag.value = true
            }
    }
    fun saveUserData(user:User){
            viewModelScope.launch {
                registerUseCase.registerUserData(user)
            }
    }
    fun uploadImage(uri: Uri){
        viewModelScope.launch {
            registerUseCase.upPhoto(uri)
            withContext(Dispatchers.IO){
                _urlPhoto.postValue(registerUseCase.getPhoto())
            }
        }
    }

}