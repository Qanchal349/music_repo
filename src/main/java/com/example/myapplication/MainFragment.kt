package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapters.SongAdapter
import com.example.myapplication.shared.Status
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    lateinit var songAdapter: SongAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mainViewModel = ViewModelProvider(requireActivity(),MainViewModelFactory(InjectorUtils.provideMusicServiceConnection(requireActivity()))).get(MainViewModel::class.java)
        songAdapter= SongAdapter(mainViewModel)

        view.songRecyclerview.apply {
            adapter=songAdapter
        }
        subscribeToObserve(view)
        return view

    }

    private fun subscribeToObserve(view: View) {
         mainViewModel.mediaItems.observe(viewLifecycleOwner){
             when(it.status){
                  Status.SUCCESS-> {
                      view.allSongsProgressBar.isVisible = false
                      it.data?.let { songs->
                          songAdapter.songs=songs
                      }
                  }
                  Status.ERROR->Unit
                  Status.LOADING-> view.allSongsProgressBar.isVisible =true
             }
         }

    }



}