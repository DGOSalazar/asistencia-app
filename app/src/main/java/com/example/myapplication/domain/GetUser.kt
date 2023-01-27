package com.example.myapplication.domain

import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class GetUser  @Inject constructor(
    val firebase: FirebaseServices
){
    operator fun invoke() = firebase.email
}