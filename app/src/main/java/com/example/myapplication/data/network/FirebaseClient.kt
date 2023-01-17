package com.example.myapplication.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor() {
    val auth : FirebaseAuth get() = FirebaseAuth.getInstance()
    val dataBase = Firebase.firestore
    val dataStorage = FirebaseStorage.getInstance().getReference("profilePhotos").child("usersPhotos")
}