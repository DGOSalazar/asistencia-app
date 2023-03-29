package com.example.myapplication.ui.userRegister.stepTwo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.sys.utils.isValidPhone
import com.example.myapplication.sys.utils.isValidText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StepTwoRegisterViewModel @Inject constructor(): ViewModel() {
    private var _setModel = MutableLiveData<UserRegister>()
    val setModel: LiveData<UserRegister> get() = _setModel


    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton


    fun setModel(userRegister: UserRegister) {
        _setModel.value = userRegister
    }

    fun validateInputs(name: String, lastName: String, birthDate: String, phoneNumber: String) {
        _activeButton.value =
            name.isValidText() && lastName.isValidText() && birthDate.isNotEmpty() && phoneNumber.isValidPhone()
    }
}