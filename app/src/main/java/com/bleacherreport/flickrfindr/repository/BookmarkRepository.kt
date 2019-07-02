package com.bleacherreport.flickrfindr.repository

import androidx.lifecycle.LiveData
import com.bleacherreport.flickrfindr.db.Bookmark
import com.bleacherreport.flickrfindr.db.BookmarksDao

class BookmarkRepository private constructor(private val bookmarksDao: BookmarksDao) {

    fun getAllBookmarks(): LiveData<List<Bookmark>> = bookmarksDao.getAllBookmarks()

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: BookmarkRepository? = null

        fun getInstance(bookmarksDao: BookmarksDao) =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepository(bookmarksDao).also { instance = it }
            }
    }
}