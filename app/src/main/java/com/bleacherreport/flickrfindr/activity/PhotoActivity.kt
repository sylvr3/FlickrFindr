package com.bleacherreport.flickrfindr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.fragments.PhotoListFragment
import com.bleacherreport.flickrfindr.utils.Constants

class PhotoActivity : AppCompatActivity() {
    private lateinit var query: String
    private var perPage: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        query = intent.getStringExtra(MainActivity.QUERY_STRING)
        perPage = intent.getIntExtra(MainActivity.NUM_OF_RESULTS_STRING,Constants.DEFAULT_NUMBER_OF_RESULTS)
        val photoListFragment = PhotoListFragment.newInstance(query,perPage)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, photoListFragment, PhotoListFragment.PHOTO_LIST_FRAGMENT)
        ft.commit()
    }
}
