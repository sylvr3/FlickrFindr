package com.bleacherreport.flickrfindr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.model.Photo
import com.bleacherreport.flickrfindr.utils.UtilityFunctions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotosAdapter(private val photoList: MutableList<Photo>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    var itemClickListener: PhotoClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]
        var url = UtilityFunctions.generatePhotoURL(photo.farm.toString(), photo.id, photo.secret, photo.server)

        Glide.with(holder.picture.context)
            .load(url).placeholder(R.drawable.not_available)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.picture)
        holder.photoId = photo.id
        holder.title.text = photo.title

    }

    override fun getItemCount() = photoList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoId: String? = null
        val title: TextView = itemView.title
        val picture: ImageView = itemView.picture

        init {
            itemView.setOnClickListener {
                photoId?.let { photoId -> itemClickListener?.onItemClick(photoId.toLong()) }
            }
        }
    }

    interface PhotoClickListener {
        fun onItemClick(photoId: Long)
    }
}