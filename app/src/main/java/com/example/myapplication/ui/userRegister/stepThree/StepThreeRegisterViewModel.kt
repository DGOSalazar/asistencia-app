package com.example.myapplication.ui.userRegister.stepThree

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.domain.FirebaseRepository
import com.example.myapplication.sys.utils.isValidCollaboratorNumber
import com.example.myapplication.sys.utils.isValidSpinner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepThreeRegisterViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var _setModel = MutableLiveData<UserRegister>()
    val setModel: LiveData<UserRegister> get() = _setModel

    private var _setNameUser = MutableLiveData<String>()
    val setNameUser: LiveData<String> get() = _setNameUser

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    private var _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String> get() = _photoUri

    private var _status = MutableLiveData<ResponseStatus<Any>>()
    val status: LiveData<ResponseStatus<Any>> get() = _status


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

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    fun createAccount(user: UserRegister){
        viewModelScope.launch {
            _status.value = firebaseRepository.doAuthRegister(user.email, user.password) as ResponseStatus<Any>
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    fun saveNewUser(model:UserRegister) {
        viewModelScope.launch {
            _status.value = firebaseRepository.doUserRegister(model) as ResponseStatus<Any>
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    fun upLoadImage(uri:Uri) {
        viewModelScope.launch {
            _status.value = firebaseRepository.doUploadImage(uri)  as ResponseStatus<Any>
        }
    }


}