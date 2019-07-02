package com.bleacherreport.flickrfindr.adapter

import com.bleacherreport.flickrfindr.db.Bookmark
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bleacherreport.flickrfindr.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.bookmark_item.view.*


class BookmarksAdapter(val onItemClick: (Bookmark) -> Unit) : RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder>() {

    private var bookmarksList = listOf<Bookmark>()
    private var originalList = listOf<Bookmark>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder =
        BookmarkViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.bookmark_item,
                parent,
                false
            )
        )

    fun loadItems(items: List<Bookmark>) {

        if (bookmarksList != items) {
            originalList = items
            bookmarksList = items
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = bookmarksList.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarksList[position]
        val url = bookmark.getUrl()
        Glide.with(holder.itemView.context)
            .load(url).placeholder(R.drawable.not_available)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.bookmarkPicture)
        holder.bookmarkId = bookmark.id.toString()
        holder.bookmarkTitle.text = bookmark.title

        holder.view.setOnClickListener {
            onItemClick(bookmarksList[position])
        }
    }

    class BookmarkViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var bookmarkId: String? = null
        val bookmarkTitle: TextView = view.bookmarkTitle
        val bookmarkPicture: ImageView = view.bookmarkPicture
    }
}