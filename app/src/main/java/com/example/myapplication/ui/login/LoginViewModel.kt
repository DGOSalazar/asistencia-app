package com.example.myapplication.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.domain.FirebaseRepository
import com.example.myapplication.domain.SharePreferenceRepository
import com.example.myapplication.sys.utils.checkIfIsValidEmail
import com.example.myapplication.sys.utils.checkIfIsValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val sharePreferenceRepository: SharePreferenceRepository
) : ViewModel() {

    private var _status = MutableLiveData<ResponseStatus<Any>>()
    val status: LiveData<ResponseStatus<Any>> get() = _status

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    fun getDoLogin(
        email: String,
        pass: String
    ) {
        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            handelResponseStatus(firebaseRepository.doLogin(email, pass))
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelResponseStatus(apiResponseStatus: ResponseStatus<Login>) {
        _status.value = apiResponseStatus as ResponseStatus<Any>
    }


    fun validateEmailAndPassword(email: String, pass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            _activeButton.value = email.checkIfIsValidEmail() && pass.checkIfIsValidPassword()
        }
    }

    fun saveLogin(email: String, pass: String) {
        sharePreferenceRepository.saveLogin(email, pass)
    }

    fun getEmail() = sharePreferenceRepository.getEmail().toString()
    fun getPassWord() = sharePreferenceRepository.getPassword().toString()
}