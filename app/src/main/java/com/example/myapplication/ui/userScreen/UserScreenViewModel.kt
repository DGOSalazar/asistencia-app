package com.example.myapplication.ui.userScreen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.models.Notify
import com.example.myapplication.data.models.ProjectsDomainModel
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.UserAdditionalData
import com.example.myapplication.domain.SharePreferenceRepository
import com.example.myapplication.domain.UserScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(
    private val userScreenRepository: UserScreenRepository,
    private val sharePreferenceRepository: SharePreferenceRepository
) : ViewModel() {

    private val _userData = MutableLiveData<Resource2<User>>()
    val userData: LiveData<Resource2<User>> = _userData

    private val _userMoreData = MutableLiveData<Resource2<UserAdditionalData>>()
    val userMoreData: LiveData<Resource2<UserAdditionalData>> = _userMoreData

    private val _userProjects = MutableLiveData<Resource2<ProjectsDomainModel>>()
    val userProjects: LiveData<Resource2<ProjectsDomainModel>> = _userProjects

    private val _setUserMoreData = MutableLiveData<Resource2<Boolean>>()
    val setUserMoreData: LiveData<Resource2<Boolean>> = _setUserMoreData

    private val _setUserProjects = MutableLiveData<Resource2<Boolean>>()
    val setUserProjects: LiveData<Resource2<Boolean>> = _setUserProjects

    private val _notifyData = MutableLiveData<ArrayList<Notify>>()
    val notifyData: LiveData<ArrayList<Notify>> = _notifyData

    private var _statusForImage = MutableLiveData<Resource2<Uri>>()
    val statusForUrl: LiveData<Resource2<Uri>> get() = _statusForImage

    fun getUserData() {
        _userData.value = Resource2.loading(null)
        viewModelScope.launch {
            userScreenRepository.getUser(getEmail()).collect{
                _userData.value = it
            }
        }
    }

    fun setMoreData(user: UserAdditionalData){
        user.email = getEmail()
        _userData.value = Resource2.loading(null)
        viewModelScope.launch {
            userScreenRepository.setAdditionalData(user).collect{
                _setUserMoreData.value = it
            }
        }
    }

    fun getMoreUserData() {
        _userMoreData.value = Resource2.loading(null)
        viewModelScope.launch {
            userScreenRepository.getAdditionalData(getEmail()).collect{
                _userMoreData.value = it
            }
        }
    }

    fun saveProjects(projects: ProjectsDomainModel){
        _setUserProjects.value = Resource2.loading(null)
        projects.email = getEmail()
        viewModelScope.launch {
            userScreenRepository.saveProjectsByUser(projects).collect{
                _setUserProjects.value = it
            }
        }
    }

    fun getProjects() {
        _userProjects.value = Resource2.loading(null)
        viewModelScope.launch {
            userScreenRepository.getProjectUser(getEmail()).collect{
                _userProjects.value = it
            }
        }
    }
    private fun getEmail() = sharePreferenceRepository.getEmail().toString()
}