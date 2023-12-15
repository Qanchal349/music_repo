package com.example.myapplication

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import com.example.myapplication.viewmodels.NowPlayingViewModel
import com.example.myapplication.viewmodels.NowPlayingViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.shared.Status
import kotlinx.android.synthetic.main.fragment_now_playing.view.*
import java.text.SimpleDateFormat
import java.util.*

class NowPlayingFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(InjectorUtils.provideMusicServiceConnection(requireActivity()))
    }

    lateinit var songViewModel: NowPlayingViewModel
    private var curPlayingSong:MediaItemData?=null
    var playbackState: PlaybackStateCompat?=null
    private var shouldUpdateSeekbar =true
    val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        songViewModel = ViewModelProvider(this,NowPlayingViewModelFactory(InjectorUtils.provideMusicServiceConnection(requireActivity()))).get(NowPlayingViewModel::class.java)
        subscribeToObserver(view)
        view.ivPlayPauseDetail.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it,true)
            }
        }

        view.ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }
        view.ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        view.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    view.tvCurTime.text = dateFormat.format(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
                shouldUpdateSeekbar=false
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                view.seekBar.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar=true
                }
            }

        })

         return  view
    }


    private fun updateTitleAndSongImage(song:MediaItemData,view: View){
        val title = "${song.title} - ${song.subtitle}"
        view.tvSongName.text = title
    }

    private fun subscribeToObserver(view: View) {
        mainViewModel.mediaItems.observe(viewLifecycleOwner){
            it?.let { result->
                when(result.status){
                    Status.SUCCESS->{
                        result.data?.let {

                            if(curPlayingSong==null && it.isNotEmpty()){
                                curPlayingSong=it[0]
                                updateTitleAndSongImage(it[0],view)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }

        mainViewModel.curPlayingSong.observe(viewLifecycleOwner){
            if(it == null) return@observe
            val description = it.description
            val song = MediaItemData(
                description.mediaId!!,
                description.title.toString(),
                description.subtitle.toString(),
                description.iconUri,
                false
            )
            curPlayingSong = song
            updateTitleAndSongImage(curPlayingSong!!,view)


        }

        mainViewModel.playbackState.observe(viewLifecycleOwner){
            playbackState=it
            view.ivPlayPauseDetail.setImageResource(
                if(playbackState?.isPlaying==true) R.drawable.ic_pause else R.drawable.ic_play
            )
          //  view.seekBar.progress = it?.position?.toInt() ?:0
        }

//        songViewModel.curPlayerPosition.observe(viewLifecycleOwner){
//            if(shouldUpdateSeekbar){
//                view.seekBar.progress = it.toInt()
//                view.tvCurTime.text = dateFormat.format(it)
//            }
//        }
//
//        songViewModel.curPlayerPosition.observe(viewLifecycleOwner){
//            view.seekBar.max = it.toInt()
//            view.tvSongDuration.text=dateFormat.format(it)
//        }

    }


}