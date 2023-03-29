package com.example.myapplication.sys.di

import com.example.myapplication.sys.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModuleHilt {

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseReferenceStorage(): StorageReference {
        val firebaseStorage = FirebaseStorage.getInstance()
        return firebaseStorage.getReference(Constants.PATH_FIREBASE_STORAGE)
            .child(Constants.PATH_CHILD_FIREBASE_STORAGE)
    }
}