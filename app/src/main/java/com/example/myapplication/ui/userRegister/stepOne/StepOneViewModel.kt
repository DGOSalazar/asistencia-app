package com.example.myapplication.ui.userRegister.stepOne

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.sys.utils.checkIfIsValidEmail
import com.example.myapplication.sys.utils.checkIfIsValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StepOneViewModel @Inject constructor() : ViewModel() {

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton


    fun validateEmailAndPassword(email: String, pass: String, confirmationPass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty() && confirmationPass.isNotEmpty()) {
            _activeButton.value =
                email.checkIfIsValidEmail() &&
                        pass.checkIfIsValidPassword() &&
                        confirmationPass.checkIfIsValidPassword() &&
                        pass == confirmationPass
        }
    }
}