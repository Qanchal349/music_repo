package com.example.myapplication.adapters

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import com.example.myapplication.R
import kotlinx.android.synthetic.main.swipe_item.view.*

class SwipeAdapter(private val navHost: Fragment,private val context: Activity) : BaseAdapter(R.layout.swipe_item) {

    override var differ = AsyncListDiffer(this,diffCallback)

    override fun onBindViewHolder(holder: BaseAdapter.SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
             val text = "${song.title} - ${song.subtitle}"
             tvPrimary.text=text
             setOnClickListener {
                 navHost.findNavController().navigate(R.id.globalActionToSongFragment)
             }
        }
    }

}