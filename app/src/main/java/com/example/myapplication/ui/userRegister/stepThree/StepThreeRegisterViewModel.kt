package com.example.myapplication.ui.userRegister.stepThree

import android.net.Uri
import androidx.lifecycle.*
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.isValidCollaboratorNumber
import com.example.myapplication.core.utils.isValidSpinner
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.domain.UserRegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private var _statusForImage = MutableLiveData<Resource2<Uri>>()
    val statusForUrl: LiveData<Resource2<Uri>> get() = _statusForImage

    private var _statusForDataUser = MutableLiveData<Resource2<Boolean>>()
    val statusForDataUser: LiveData<Resource2<Boolean>> get() = _statusForDataUser

    private var _statusForDoUser = MutableLiveData<Resource2<Boolean>>()
    val statusForDoUser: LiveData<Resource2<Boolean>> get() = _statusForDoUser


    private var _positions = MutableLiveData<Resource2<ArrayList<String>>>()
    val positionsLiveData: LiveData<Resource2<ArrayList<String>>> get() = _positions

    fun setModel(userRegister: UserRegister) {
        _setModel.value = userRegister
    }

    fun setNameTexView(name: String) {
        _setNameUser.value = name
    }

    fun validateInputs(position: Int, team: Int, employee: String, imageUriLocal: Uri?) {
        _activeButton.value =
            position.isValidSpinner() && team.isValidSpinner() && employee.isValidCollaboratorNumber() && imageUriLocal != null
    }

    fun loadPhoto(photo: String) {
        _photoUri.value = photo
    }


    fun createAccount(user: UserRegister) = viewModelScope.launch {
        _statusForDoUser.value = Resource2.loading(null)
        userRegisterRepository.doAuthRegister(user.email, user.password).collect {
            _statusForDoUser.value = it
        }
    }


    fun saveNewUser(model: UserRegister) = viewModelScope.launch {
        userRegisterRepository.doUserRegister(model).collect { response ->
            _statusForDataUser.value = response
        }
    }


    fun upLoadImage(uri: Uri) = viewModelScope.launch {
        userRegisterRepository.doUploadImage(uri).collect {
            _statusForImage.value = it
        }
    }


    fun getAllPositions() = liveData(Dispatchers.IO) {
        userRegisterRepository.getAllPositions().collect { reponse ->
            emit(reponse)
        }
    }

    fun getAllTeams() = liveData(Dispatchers.IO) {
        userRegisterRepository.getAllTeams().collect { response ->
            emit(response)
        }
    }

}