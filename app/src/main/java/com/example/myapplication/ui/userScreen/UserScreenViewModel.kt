package com.example.myapplication.ui.userScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.models.Notify
import com.example.myapplication.data.models.User
import com.example.myapplication.domain.GetNotificationUseCase
import com.example.myapplication.domain.GetUserInfoUseCase
import com.example.myapplication.domain.SharePreferenceRepository
import com.example.myapplication.domain.UserScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val userScreenRepository: UserScreenRepository,
    private val sharePreferenceRepository: SharePreferenceRepository
    ): ViewModel() {

    private val _userData = MutableLiveData<Resource<User>>()
    val userData : LiveData<Resource<User>> = _userData

    private val _notifyData = MutableLiveData<ArrayList<Notify>>()
    val notifyData : LiveData<ArrayList<Notify>> = _notifyData

    fun getNotification(){
        viewModelScope.launch{
            _notifyData.postValue(getNotificationUseCase()!!)
        }
    }
    fun getUserData(){
        _userData.value = Resource.loading(null)
        viewModelScope.launch {
            val res = userScreenRepository.getUser(getEmail())
            _userData.value = res
        }
    }
    private fun getEmail() = sharePreferenceRepository.getEmail().toString()
}