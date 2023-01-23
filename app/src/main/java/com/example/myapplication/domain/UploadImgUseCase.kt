package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.data.network.FirebaseServices
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

class UploadImgUseCase @Inject constructor(
    private val firebase: FirebaseServices
) {
    fun upPhoto(uri: Uri) = firebase.uploadPhoto(uri)
    suspend fun getPhoto() = firebase.getUrlF()
}