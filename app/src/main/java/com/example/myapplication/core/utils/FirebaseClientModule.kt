package com.example.myapplication.core.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton


class FirebaseClientModule @Inject constructor() {
    val auth : FirebaseAuth get() = FirebaseAuth.getInstance()
    val userCollection = Firebase.firestore.collection("UsersCollection")
    val dayCollection = Firebase.firestore.collection("DayCollection")
    val teamsCollection = Firebase.firestore.collection("TeamsCollection")
    val notifyCollection = Firebase.firestore.collection("NotifyCollection")
    val dayConfirmCollection = Firebase.firestore.collection("DayConfirmCollection")
    val storage= FirebaseStorage.getInstance().getReference(Constants.PATH_FIREBASE_STORAGE).child(Constants.PATH_CHILD_FIREBASE_STORAGE)
   // val dataStorage = FirebaseStorage.getInstance().getReference("profilePhotos").child("usersPhotos")
}