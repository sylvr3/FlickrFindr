package com.bleacherreport.flickrfindr.model

data class PhotoSearchResponse(
        val page: Int,
        val total_pages: Int,
        var results: MutableList<Photo>,
        val photos: Photos? = null
)