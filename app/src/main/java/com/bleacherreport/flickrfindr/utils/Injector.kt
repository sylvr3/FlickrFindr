package com.bleacherreport.flickrfindr.utils

import android.content.Context
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.repository.BookmarkRepository
import com.bleacherreport.flickrfindr.viewmodel.BookmarkViewModelFactory

/**
 * Creates a singleton instance of BookmarkViewModelFactory so that it can be injected in fragments and activities
 */
object Injector {

    private fun getBookmarkRepository(context: Context): BookmarkRepository {
        return BookmarkRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).bookmarksDao()
        )
    }

    fun provideBookmarkViewModelFactory(context: Context): BookmarkViewModelFactory {
        val repository = getBookmarkRepository(context)
        return BookmarkViewModelFactory(repository)
    }
}