package com.example.myapplication

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import com.example.myapplication.shared.mediaUri

data class MediaItemData(
    val mediaId: String,
    val title: String,
    val subtitle: String,
    val albumArtUri: Uri?,
    val browsable: Boolean,
   // var playbackRes: Int
)


fun MediaMetadataCompat.toMediaItemData(): MediaItemData?{
return MediaMetadataCompat.Builder().build().let {
         val subtitle = it.description.subtitle ?: ""
    it.description.iconUri?.let { it1 ->
        MediaItemData(
            it.mediaUri.toString(),
            it.description.title.toString(),
            subtitle.toString(),
            it1,
            false,
            // getResourceForMediaId(child.mediaId!!)
        )
    }
     }

}


