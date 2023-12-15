package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.shared.MusicServiceConnection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object MobileServiceModule {
//    @Provides
//    @ServiceScoped
//    fun provideMusicServiceConnection(
//        @ApplicationContext context: Context
//    )= MusicServiceConnection()

}