package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.shared.MusicServiceConnection


class MainViewModelFactory(private val musicServiceConnection: MusicServiceConnection)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(musicServiceConnection) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}