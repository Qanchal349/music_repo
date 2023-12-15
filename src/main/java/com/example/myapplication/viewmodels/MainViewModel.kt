package com.example.myapplication.viewmodels


import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.MediaItemData
import com.example.myapplication.isPlayEnabled
import com.example.myapplication.isPlaying
import com.example.myapplication.isPrepared
import com.example.myapplication.shared.*

class MainViewModel (
    private val   musicServiceConnection: MusicServiceConnection
) : ViewModel() {

     private val _mediaItems = MutableLiveData<Resource<List<MediaItemData>>>()
     val mediaItems:LiveData<Resource<List<MediaItemData>>> = _mediaItems
     val networkError = musicServiceConnection.networkError
     val isConnected = musicServiceConnection.isConnected
     val curPlayingSong = musicServiceConnection.curPlayingSong
     val playbackState =musicServiceConnection.playbackState

     init {
          _mediaItems.postValue(Resource.loading(null))
          musicServiceConnection.subscribe("root",object:MediaBrowserCompat.SubscriptionCallback(){
               override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
               ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {child->
                        val subtitle = child.description.subtitle ?: ""
                        MediaItemData(
                            child.mediaId!!,
                            child.description.title.toString(),
                            subtitle.toString(),
                            child.description.iconUri!!,
                            child.isBrowsable,
                           // getResourceForMediaId(child.mediaId!!)
                        )
                    }
                    _mediaItems.postValue(Resource.success(items) as Resource<List<MediaItemData>>?)
               }
          })

     }

     fun skipToNextSong(){
           musicServiceConnection.transportControls.skipToNext()
     }
     fun skipToPreviousSong(){
          musicServiceConnection.transportControls.skipToPrevious()
     }
     fun seekTo(pos:Long){
          musicServiceConnection.transportControls.seekTo(pos)
     }
// if(isPrepared && mediaItem.mediaId == curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID))

     fun playOrToggleSong(mediaItem: MediaItemData, toggle:Boolean=false){
          val isPrepared = playbackState.value?.isPrepared ?: false
          if(isPrepared && mediaItem.mediaId == curPlayingSong.value?.id){
               playbackState.value?.let { playbackState ->
                    when{
                         playbackState.isPlaying -> if(toggle) musicServiceConnection.transportControls.pause()
                         playbackState.isPlayEnabled-> musicServiceConnection.transportControls.play()
                         else->Unit
                    }
               }
          } else {
                musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId ,null)
          }

     }

     override fun onCleared() {
          super.onCleared()
          musicServiceConnection.unsubscribe("root",object :MediaBrowserCompat.SubscriptionCallback(){})
     }

}