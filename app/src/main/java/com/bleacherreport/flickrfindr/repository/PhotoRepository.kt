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
import com.bleacherreport.flickrfindr.model.PhotoSearchResponse
import com.bleacherreport.flickrfindr.model.PhotoSearchResult
import com.bleacherreport.flickrfindr.utils.Constants
import com.bleacherreport.flickrfindr.utils.UtilityFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

    suspend fun <T> Call<T>.runCoroutine() : T = suspendCoroutine {
        this.enqueue(object : Callback<T>{
            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(t)
            }
            override fun onResponse(call: Call<T>, response: Response<T>) {
                it.resume(response.body()!!)
            }
        })
    }

    fun getSearchResults(query: String, perPage: Int): LiveData<PhotoSearchResponse> {
        val data = MutableLiveData<PhotoSearchResponse>()
        this.flickrPhotoService.getPhotos(
            BuildConfig.FLICKR_API_KEY,
            query,
            perPage,
            Constants.FLICKR_FORMAT,
            Constants.FLICKR_SORT,
            Constants.FLICKR_JSON_CALLBACK
        )
            .enqueue(object : Callback<PhotoSearchResponse> {
                override fun onResponse(call: Call<PhotoSearchResponse>, response: Response<PhotoSearchResponse>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        // if (response.isSuccessful) {
                        data.value = response.body()
                        println("success!")
                         var results = data.value?.photos?.photo as ArrayList<Photo>
                        val resultsAsync = mutableListOf<Deferred<Photo>>()
                       // val results = mutableListOf<Photo>()
                        val resp = response.body()

                        if (results.isEmpty())
                            Toast.makeText(context, "No search results found", Toast.LENGTH_LONG).show()
                        for (photo in results) {
                            resultsAsync.add(async { flickrPhotoService.getPhoto(BuildConfig.FLICKR_API_KEY, photo.farm, photo.server, photo.id, photo.secret).runCoroutine() })
                        }
                        for (a in resultsAsync) {
                            results.add(a.await())
                        }

                        resp?.results = results
                        data.value = resp

                      /*  for (photo in results) {
                            var photoUrl = UtilityFunctions.generatePhotoURL(
                                photo.farm.toString(),
                                photo.id,
                                photo.secret,
                                photo.server
                            )
                        }*/
                        // }
                        var recentSearch = RecentSearch(query)
                        DatabaseInitializer.addRecentSearch(appDatabase, recentSearch)

                    }
                }


                override fun onFailure(call: Call<PhotoSearchResponse>, t: Throwable) {
                    Log.e("Failed to get results", t.message)

                }
            })
        return data

    }


}