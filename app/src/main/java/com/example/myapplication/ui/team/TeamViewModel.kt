package com.example.myapplication.ui.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.domain.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    private var _teams = MutableLiveData<ArrayList<TeamGroup>>()
    var teams: LiveData<ArrayList<TeamGroup>> = _teams

    fun getTeams(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _teams.postValue(getUserInfoUseCase.getUsersByTeams())
            }
        }
    }

}