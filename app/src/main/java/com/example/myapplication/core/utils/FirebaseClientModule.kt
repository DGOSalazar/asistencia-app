package com.example.myapplication.core.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton


class FirebaseClientModule @Inject constructor() {
    val auth : FirebaseAuth get() = FirebaseAuth.getInstance()
    val userCollection = Firebase.firestore.collection(Collection.USERS_COLLECTION.value)
    val dayCollection = Firebase.firestore.collection(Collection.DAY_COLLECTION.value)
    val teamsCollection = Firebase.firestore.collection(Collection.TEAM_COLLECTION.value)
    val positionCollection = Firebase.firestore.collection(Collection.POSITION_COLLECTION.value)
    val notifyCollection = Firebase.firestore.collection(Collection.NOTIFY_COLLECTION.value)
    val dayConfirmCollection = Firebase.firestore.collection(Collection.DAY_CONFIRMATION_COLLECTION.value)
    val storage= FirebaseStorage.getInstance().getReference(Constants.PATH_FIREBASE_STORAGE).child(Constants.PATH_CHILD_FIREBASE_STORAGE)
   // val dataStorage = FirebaseStorage.getInstance().getReference("profilePhotos").child("usersPhotos")
}