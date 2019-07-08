package com.bleacherreport.flickrfindr.api

import com.bleacherreport.flickrfindr.model.Photo
import com.bleacherreport.flickrfindr.model.PhotoSearchResponse
import com.bleacherreport.flickrfindr.model.PhotoSearchResult
import com.bleacherreport.flickrfindr.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrPhotoService {

    @GET("?method=" + Constants.FLICKR_METHOD)
    fun getPhotos(
        @Query("api_key") api_key: String, @Query("text") searchTerm: String, @Query("per_page") perPage: Int, @Query(
            "format"
        ) format: String, @Query("sort") sort: String, @Query("nojsoncallback") nojsoncallback: Int
    ): Call<PhotoSearchResponse>

    @GET("?method=flickr.photos.search&extras=url_l,url_n")
    fun getPhoto(
        @Query("api_key") api_key: String,
        @Query("farm") farm: String,
        @Query("server") server: String,
        @Query("id") id: String,
        @Query("secret") secret: String
    ): Call<Photo>


}
