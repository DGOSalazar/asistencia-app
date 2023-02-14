package com.example.myapplication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClientModule @Inject constructor() {
    val auth : FirebaseAuth get() = FirebaseAuth.getInstance()
    val userCollection = Firebase.firestore.collection("UsersCollection")
    val dayCollection = Firebase.firestore.collection("DayCollection")
    val dayConfirmCollection = Firebase.firestore.collection("DayConfirmCollection")
    val dataStorage = FirebaseStorage.getInstance().getReference("profilePhotos").child("usersPhotos")
}