package com.example.myapplication.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.checkIfIsValidEmail
import com.example.myapplication.core.utils.checkIfIsValidPassword
import com.example.myapplication.data.remote.response.LoginResponse
import com.example.myapplication.domain.FirebaseRepository
import com.example.myapplication.domain.LoginRepository
import com.example.myapplication.domain.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository:FirebaseRepository,
    private val loginRepository: LoginRepository,
    private val sharePreferenceRepository: SharePreferenceRepository
) : ViewModel() {

    private var _status = MutableLiveData<Resource<LoginResponse>>()
    val status: LiveData<Resource<LoginResponse>> get() = _status

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    fun getDoLogin(
        email: String,
        pass: String
    ) {
        viewModelScope.launch {
            _status.value = Resource.loading(null)
            handelResponseStatus(loginRepository.doLogin(email, pass))

        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelResponseStatus(apiResponseStatus: Resource<LoginResponse>) {
        _status.value = apiResponseStatus
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