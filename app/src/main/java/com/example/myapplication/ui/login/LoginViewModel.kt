package com.example.myapplication.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.checkIfIsValidEmail
import com.example.myapplication.core.utils.checkIfIsValidPassword
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.models.LoginDomainModel
import com.example.myapplication.domain.LoginRepository
import com.example.myapplication.domain.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharePreferenceRepository: SharePreferenceRepository
) : ViewModel() {

    private var _status = MutableLiveData<Resource2<LoginDomainModel>>()
    val status: LiveData<Resource2<LoginDomainModel>> get() = _status

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    fun getDoLogin(
        email: String,
        pass: String
    ) {
        viewModelScope.launch {
            _status.value = Resource2.loading(null)
            loginRepository.doLogin(email, pass).collect{
                handelResponseStatus(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelResponseStatus(apiResponseStatus: Resource2<LoginDomainModel>) {
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