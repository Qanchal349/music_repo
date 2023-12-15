package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.currentPlayBackPosition
import com.example.myapplication.shared.MusicService
import com.example.myapplication.shared.MusicServiceConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NowPlayingViewModel (musicServiceConnection: MusicServiceConnection):ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private val _curSongDuration = MutableLiveData<Long>()
    val curSongDuration : LiveData<Long> = _curSongDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition : LiveData<Long> = _curPlayerPosition

    init {
        updateCurrentPlayerPosition()
    }

   private fun updateCurrentPlayerPosition(){
        viewModelScope.launch {
             while (true){
                   val pos = playbackState.value?.currentPlayBackPosition
                   if(curPlayerPosition.value!=pos){
                        _curPlayerPosition.postValue(pos!!)
                       _curSongDuration.postValue(MusicService.curSongDuration)
                   };delay(100L)

             }
        }
   }


}