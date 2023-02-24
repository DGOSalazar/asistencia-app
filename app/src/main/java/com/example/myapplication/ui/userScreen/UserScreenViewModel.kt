package com.example.myapplication.ui.userScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Notify
import com.example.myapplication.domain.GetNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase
): ViewModel() {
    private val _notifyData = MutableLiveData<ArrayList<Notify>>()
    val notifyData : LiveData<ArrayList<Notify>> = _notifyData

    fun getNotification(){
        viewModelScope.launch{
            _notifyData.postValue(getNotificationUseCase()!!)
        }
    }
}