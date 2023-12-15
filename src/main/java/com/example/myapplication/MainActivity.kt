package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.adapters.SongAdapter
import com.example.myapplication.adapters.SwipeAdapter
import com.example.myapplication.shared.*
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.absoluteValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var swipeAdapter: SwipeAdapter
    private var curPlayingSong:MediaItemData?=null
    private var playbackState:PlaybackStateCompat?=null

    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(InjectorUtils.provideMusicServiceConnection(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          navController = findNavController(R.id.navHostFragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.mainFragment,
            R.id.nowPlayingFragment

        ))
        setupActionBarWithNavController(navController,appBarConfiguration)
        swipeAdapter=SwipeAdapter(navHostFragment,this)
         setupSwipeRecyclerView()
         subscribeToObservers()
         vpSong.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
             override fun onPageSelected(position: Int) {
                 super.onPageSelected(position)
                 if(playbackState?.isPlaying==true){
                      mainViewModel.playOrToggleSong(swipeAdapter.songs[position])
                 }else{
                      curPlayingSong=swipeAdapter.songs[position]
                 }
             }
         })


        ivPlayPause.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it,true)
            }
        }


        navHostFragment.findNavController().addOnDestinationChangedListener{_,destination,_ ->
             when(destination.id){
                 R.id.nowPlayingFragment-> hideBottomBar()
                 R.id.mainFragment->showBottomBar()
                 else-> showBottomBar()
             }
        }
    }

    fun setupSwipeRecyclerView()=vpSong.apply {
        adapter=swipeAdapter
    }

    private fun hideBottomBar(){
         ivCurSongImage.isVisible=false
         vpSong.isVisible=false
         ivPlayPause.isVisible=false
    }

    private fun showBottomBar(){
        ivCurSongImage.isVisible=true
        vpSong.isVisible=true
        ivPlayPause.isVisible=true
    }

    private fun switchViewPagerToCurrentSong(song:MediaItemData){
        if(swipeAdapter.songs.isNotEmpty()){
            val newSong= swipeAdapter.songs.filter { it.title==song.title }
            if(newSong.isNotEmpty()){
                val newItemIndex = swipeAdapter.songs.indexOf(newSong[0])
                if(newItemIndex!=-1){
                    vpSong.currentItem=newItemIndex
                    curPlayingSong=song
                }
            }
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(this,{
              when(it.status){
                  Status.SUCCESS->{
                       it.data?.let { songs->
                           swipeAdapter.songs=songs
                           switchViewPagerToCurrentSong(curPlayingSong?:return@observe)
                       }
                  }
                  Status.ERROR->{

                  }
              }
        })

       mainViewModel.curPlayingSong.observe(this){
           if(it==null) return@observe
           val song = MediaItemData(
                it.description.mediaId!!,
                it.description.title.toString(),
                it.description.subtitle.toString(),
                it.description.iconUri,
                 false
               )
            curPlayingSong=song
           switchViewPagerToCurrentSong(curPlayingSong?:return@observe)
       }

       mainViewModel.playbackState.observe(this,{
           playbackState=it
            ivPlayPause.setImageResource(
                if(playbackState?.isPlaying==true) R.drawable.ic_pause else R.drawable.ic_play
            )
       })

      mainViewModel.isConnected.observe(this){
           it?.getContentIfNotHandle()?.let { result->
                 when(result.status) {
//                     Status.ERROR -> Snackbar.make(
//                          R.layout.activity_main,
//                         result.message ?: "An unknown error occured",
//                         Snackbar.LENGTH_SHORT
//                     ).show()
                     else->Unit
                 }
           }
      }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}