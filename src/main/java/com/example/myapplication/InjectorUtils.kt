package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import com.example.myapplication.shared.MusicService
import com.example.myapplication.shared.MusicServiceConnection

object InjectorUtils {

     fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
        return MusicServiceConnection.getInstance(
            context,
            ComponentName(context, MusicService::class.java)
        )
    }
}