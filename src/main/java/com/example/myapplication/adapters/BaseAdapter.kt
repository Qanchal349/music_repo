package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MediaItemData


abstract class BaseAdapter (
      private val layoutId : Int

        ) : RecyclerView.Adapter<BaseAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
        )
    }

    override fun getItemCount()=songs.size

    protected val diffCallback = object : DiffUtil.ItemCallback<MediaItemData>(){
        override fun areItemsTheSame(oldItem: MediaItemData, newItem: MediaItemData): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: MediaItemData, newItem: MediaItemData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var onItemClickListener:((MediaItemData)->Unit)?=null

    fun setOnItemClickListener(listener:(MediaItemData)->Unit){
        onItemClickListener = listener
    }



  protected abstract var differ: AsyncListDiffer<MediaItemData>

    var songs:List<MediaItemData>
        get() = differ.currentList
        set(value) = differ.submitList(value)
}