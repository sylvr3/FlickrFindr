package com.bleacherreport.flickrfindr.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bleacherreport.flickrfindr.BuildConfig
import com.bleacherreport.flickrfindr.api.FlickrPhotoService
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.db.DatabaseInitializer
import com.bleacherreport.flickrfindr.db.RecentSearch
import com.bleacherreport.flickrfindr.model.Photo
import com.bleacherreport.flickrfindr.model.PhotoSearchResult
import com.bleacherreport.flickrfindr.utils.Constants
import com.bleacherreport.flickrfindr.utils.UtilityFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder


class PhotoRepository {
    private val flickrPhotoService: FlickrPhotoService
    private var context: Context? = null
    private var appDatabase: AppDatabase? = null


    constructor(context: Context) {
        this.context = context
        appDatabase = AppDatabase.getInstance(context)

    }

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        appDatabase = AppDatabase.getInstance(context)
        this.flickrPhotoService = retrofit.create(FlickrPhotoService::class.java)

    }

    fun getSearchResults(query: String, perPage: Int): LiveData<PhotoSearchResult> {
        val data = MutableLiveData<PhotoSearchResult>()
        this.flickrPhotoService.getPhotos(
            BuildConfig.FLICKR_API_KEY,
            query,
            perPage,
            Constants.FLICKR_FORMAT,
            Constants.FLICKR_SORT,
            Constants.FLICKR_JSON_CALLBACK
        )
            .enqueue(object : Callback<PhotoSearchResult> {
                override fun onResponse(call: Call<PhotoSearchResult>, response: Response<PhotoSearchResult>) {
                    if (response.isSuccessful) {
                        data.value = response.body()
                        println("success!")
                        var results = data.value?.photos?.photo as ArrayList<Photo>
                        if (results.isEmpty())
                            Toast.makeText(context, "No search results found", Toast.LENGTH_LONG).show()
                        for (photo in results) {
                            var photoUrl = UtilityFunctions.generatePhotoURL(
                                photo.farm,
                                photo.id,
                                photo.secret,
                                photo.server
                            )
                        }
                    }
                    var recentSearch = RecentSearch(query)
                    DatabaseInitializer.addRecentSearch(appDatabase, recentSearch)

                }


                override fun onFailure(call: Call<PhotoSearchResult>, t: Throwable) {
                    Log.e("Failed to get results", t.message)

                }
            })
        return data

    }

}