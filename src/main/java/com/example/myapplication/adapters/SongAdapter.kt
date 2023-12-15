package com.example.myapplication.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.example.myapplication.R
import com.example.myapplication.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.list_item.view.*

class SongAdapter (
   // private val glide : RequestManager
    private val viewModel: MainViewModel,

):  BaseAdapter(R.layout.list_item){

    override var differ = AsyncListDiffer(this,diffCallback)

    override fun onBindViewHolder(holder: BaseAdapter.SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
             title.text = song.title
             subTitle.text=song.subtitle
            // glide.load(song.image).into(imageView)
        }

        holder.itemView.setOnClickListener {
            viewModel.playOrToggleSong(song)
        }

//        holder.itemView.apply {
//             onItemClickListener?.let {
//
//                 it(song)
//             }
//        }

    }

}