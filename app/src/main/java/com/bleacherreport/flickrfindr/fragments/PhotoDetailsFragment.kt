package com.bleacherreport.flickrfindr.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.db.Bookmark
import com.bleacherreport.flickrfindr.db.DatabaseInitializer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.bookmark_details.*
import kotlinx.android.synthetic.main.photo_details.*

@SuppressLint("ValidFragment")
class PhotoDetailsFragment(context: Context) : Fragment() {


    private var appDatabase: AppDatabase? = null
    private var mContext: Context
    private val PHOTO_DATA_TITLE = "PHOTO_DATA_TITLE"
    private val PHOTO_DATA_URL = "PHOTO_DATA_URL"

    init {
        this.mContext = context

    }


    fun newInstance(photoDataTitle: String, photoDataURL: String) = PhotoDetailsFragment(mContext).apply {
        arguments = Bundle().apply {
            putString(PHOTO_DATA_TITLE, photoDataTitle)
            putString(PHOTO_DATA_URL, photoDataURL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.photo_details, container, false)

        val bookmarkCheckbox = view.findViewById<CheckBox>(R.id.bookmarkCheckBox)
        val btnBackPhotoDetails = view.findViewById<Button>(R.id.btnBackPhotoDetails)

        appDatabase = AppDatabase.getInstance(mContext)
        var title = arguments!!.getString(PHOTO_DATA_TITLE)
        if (title.isNullOrEmpty())
            title = ""
        val url = arguments!!.getString(PHOTO_DATA_URL)

        bookmarkCheckbox.setOnCheckedChangeListener { _, _ ->
            var bookmark = Bookmark(url)
            bookmark.title = title
            val bookmarkObj = DatabaseInitializer.addBookmark(appDatabase, bookmark)
            if (bookmarkObj != null)
                Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show()
        }

        btnBackPhotoDetails.setOnClickListener {
            activity?.onBackPressed()
        }

        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val titleText = arguments!!.getString(PHOTO_DATA_TITLE)
        val url = arguments!!.getString(PHOTO_DATA_URL)
        Log.i("PHOTO URL", url)

        picture.let {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.not_available)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(it)
        }
            title.text = titleText
    }

}


