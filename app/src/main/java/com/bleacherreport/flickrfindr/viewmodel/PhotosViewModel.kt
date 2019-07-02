package com.bleacherreport.flickrfindr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.model.Photo
import com.bleacherreport.flickrfindr.model.PhotoSearchResult
import com.bleacherreport.flickrfindr.repository.PhotoRepository


class PhotosViewModel(application: Application) : AndroidViewModel(application) {
    var searchResponse: LiveData<PhotoSearchResult> = MutableLiveData<PhotoSearchResult>()
    private var queryString: String? = null
    private var appDatabase: AppDatabase? = null

    fun initialize(query: String, perPage: Int) {
        queryString = query
        appDatabase = AppDatabase.getInstance(getApplication<Application>().applicationContext)
        searchResponse = PhotoRepository(getApplication<Application>().applicationContext).getSearchResults(
            queryString as String,
            perPage
        )
    }

    fun getPhoto(photoId: Long): Photo? {
        return searchResponse.value!!.photos.photo.find {
            it.id.toLong() == (photoId)
        }
    }
}
