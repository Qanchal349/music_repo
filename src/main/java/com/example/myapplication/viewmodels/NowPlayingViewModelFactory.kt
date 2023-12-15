package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.shared.MusicServiceConnection


class NowPlayingViewModelFactory(private val musicServiceConnection: MusicServiceConnection)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
            return NowPlayingViewModel(musicServiceConnection) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}