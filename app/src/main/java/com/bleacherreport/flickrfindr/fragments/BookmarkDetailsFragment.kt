package com.bleacherreport.flickrfindr.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.db.Bookmark
import com.bleacherreport.flickrfindr.db.DatabaseInitializer
import com.bleacherreport.flickrfindr.utils.Injector
import com.bleacherreport.flickrfindr.viewmodel.BookmarksViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.bookmark_details.*


class BookmarkDetailsFragment constructor(context: Context) : Fragment() {

    private lateinit var viewModel: BookmarksViewModel
    private var appDatabase: AppDatabase? = null

    private var mContext: Context
    private val BOOKMARK_ID = "BOOKMARK_ID"
    private val BOOKMARK_TITLE = "BOOKMARK_TITLE"
    private val BOOKMARK_URL = "BOOKMARK_URL"


    init {
        this.mContext = context
        appDatabase = AppDatabase.getInstance(mContext)
    }

    fun newInstance(bookmarkTitle: String, bookmarkURL: String, bookmarkId: Int) = BookmarkDetailsFragment(mContext).apply {
        arguments = Bundle().apply {
            putInt(BOOKMARK_ID, bookmarkId)
            putString(BOOKMARK_TITLE, bookmarkTitle)
            putString(BOOKMARK_URL, bookmarkURL)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bookmark_details, container, false)

        val btnBackBookmarkDetails = view?.findViewById<Button>(R.id.btnBackBookmarkDetails)
        val removeFromBookmarksCheckBox = view?.findViewById<CheckBox>(R.id.removeFromBookmarksCheckBox)

        val title = arguments!!.getString(BOOKMARK_TITLE)
        val url = arguments!!.getString(BOOKMARK_URL)
        val id = arguments!!.getInt(BOOKMARK_ID)
        removeFromBookmarksCheckBox?.setOnCheckedChangeListener { _, _ ->
            var bookmark = Bookmark(url)
            bookmark.title = title
            bookmark.url = url.toString()
            bookmark.id = id
            DatabaseInitializer.removeBookmark(appDatabase, bookmark)
            Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show()
        }

        btnBackBookmarkDetails?.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup ViewModel
        val factory = Injector.provideBookmarkViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(activity!!, factory).get(BookmarksViewModel::class.java)

        viewModel.getSelectedBookmark().observe(viewLifecycleOwner, Observer {
            updateTitle(it.title)
            updateUrl(it.url)
        })
    }

    private fun updateTitle(title: String) {
        if (title.isNullOrEmpty())
            bookmarkTitle.text = ""
        bookmarkTitle.text = title
    }

    private fun updateUrl(url: String) {
        bookmarkPicture.let {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.not_available)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(it)
        }
    }
}