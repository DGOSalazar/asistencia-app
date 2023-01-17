package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.data.network.FirebaseServices
import javax.inject.Inject

class UploadImgUseCase @Inject constructor(
    val firebase: FirebaseServices
) {
    suspend operator fun invoke(uri: Uri) = firebase.uploadPhoto(uri)

}