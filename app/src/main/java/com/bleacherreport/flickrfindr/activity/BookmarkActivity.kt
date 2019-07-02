package com.bleacherreport.flickrfindr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.fragments.BookmarkListFragment

class BookmarkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        val bookmarksListFragment = BookmarkListFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fragmentContainer, bookmarksListFragment as Fragment).commit()
    }
}



