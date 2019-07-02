package com.bleacherreport.flickrfindr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bleacherreport.flickrfindr.repository.BookmarkRepository

class BookmarkViewModelFactory(private val repository: BookmarkRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = BookmarksViewModel(repository) as T
}