package com.example.myapplication.ui.userRegister.stepThree

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.isValidCollaboratorNumber
import com.example.myapplication.core.utils.isValidSpinner
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.domain.UserRegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepThreeRegisterViewModel @Inject constructor(
    private val userRegisterRepository: UserRegisterRepository
) : ViewModel() {

    private var _setModel = MutableLiveData<UserRegister>()
    val setModel: LiveData<UserRegister> get() = _setModel

    private var _setNameUser = MutableLiveData<String>()
    val setNameUser: LiveData<String> get() = _setNameUser

    private var _activeButton = MutableLiveData<Boolean>()
    val activeButton: LiveData<Boolean> get() = _activeButton

    private var _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String> get() = _photoUri

    private var _status = MutableLiveData<Resource<Boolean>>()
    val status: LiveData<Resource<Boolean>> get() = _status

    private var _statusForImage = MutableLiveData<Resource<Uri>>()
    val statusForUrl: LiveData<Resource<Uri>> get() = _statusForImage

    private var _statusForDataUser = MutableLiveData<Resource<Boolean>>()
    val statusForDataUser: LiveData<Resource<Boolean>> get() = _statusForDataUser

    private var _statusForDoUser  = MutableLiveData<Resource<Boolean>>()
    val statusForDoUser: LiveData<Resource<Boolean>> get() = _statusForDoUser

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
            _statusForDoUser.value=Resource.loading(null)
            _statusForDoUser.value = userRegisterRepository.doAuthRegister(user.email, user.password)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    fun saveNewUser(model:UserRegister) {
        viewModelScope.launch {
            _statusForDataUser.value = userRegisterRepository.doUserRegister(model)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    fun upLoadImage(uri:Uri) {
        viewModelScope.launch {
            _statusForImage.value = userRegisterRepository.doUploadImage(uri)
        }
    }


}