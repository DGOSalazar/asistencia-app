package com.example.myapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _userExist = MutableLiveData<Boolean>()
    var userExist : LiveData<Boolean> = _userExist

    fun login(name:String,pass:String){
        viewModelScope.launch {
           val result = loginUseCase(name,pass)
            _userExist.value = result != LoginResult.Error
        }
    }
}