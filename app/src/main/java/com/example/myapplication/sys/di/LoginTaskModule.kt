package com.example.myapplication.sys.di

import com.example.myapplication.domain.FirebaseRepository
import com.example.myapplication.domain.FirebaseTask
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginTaskModule {
    @Binds
    abstract fun bindLoginTask(
        firebaseRepository: FirebaseRepository
    ): FirebaseTask
}