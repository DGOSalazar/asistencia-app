package com.example.myapplication.ui.userRegister.stepThree

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.sys.utils.isValidCollaboratorNumber
import com.example.myapplication.sys.utils.isValidPhone
import com.example.myapplication.sys.utils.isValidSpinner
import com.example.myapplication.sys.utils.isValidText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StepThreeRegisterViewModel @Inject constructor() : ViewModel() {

    private var _setModel = MutableLiveData<UserRegister>()
    val setModel: LiveData<UserRegister> get() = _setModel

    private var _setNameUser = MutableLiveData<String>()
    val setNameUser: LiveData<String> get() = _setNameUser

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    private var _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String> get() = _photoUri


    fun setModel(userRegister: UserRegister) {
        _setModel.value = userRegister
    }

    fun setNameTexView(name: String) {
        _setNameUser.value = name
    }

    fun validateInputs(position: Int, team: Int, employee: String,  imageUriLocal: Uri?) {
        _activeButton.value =
            position.isValidSpinner() && team.isValidSpinner() && employee.isValidCollaboratorNumber() && imageUriLocal != null
    }

    fun loadPhoto(photo: String) {
        _photoUri.value = photo
    }
}