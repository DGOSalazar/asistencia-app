package com.example.myapplication.sys.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSharePreference {
    @Singleton
    @Provides
    fun provideSharePreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideSharePreferenceEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}