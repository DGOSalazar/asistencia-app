package com.example.myapplication.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.myapplication.domain.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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