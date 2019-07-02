package com.bleacherreport.flickrfindr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bleacherreport.flickrfindr.db.Bookmark
import com.bleacherreport.flickrfindr.repository.BookmarkRepository

class BookmarksViewModel internal constructor(private val repository: BookmarkRepository) : ViewModel() {

    private val selectedBookmark: MutableLiveData<Bookmark> = MutableLiveData()

    fun updateSelectedBookmark(model: Bookmark) {
        selectedBookmark.value = model
    }

    fun getAllBookmarks(): LiveData<List<Bookmark>> = repository.getAllBookmarks()
    fun getSelectedBookmark() = selectedBookmark as LiveData<Bookmark>
}

