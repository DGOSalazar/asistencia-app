package com.example.myapplication.ui.team

import androidx.lifecycle.*
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.domain.GetUserInfoUseCase
import com.example.myapplication.domain.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: TeamRepository
): ViewModel() {
    fun getTeams() = liveData(Dispatchers.IO){
        repository.getUsersByTeams().collect{
            emit(it)
        }
    }

}