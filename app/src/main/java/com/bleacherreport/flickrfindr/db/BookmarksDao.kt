package com.bleacherreport.flickrfindr.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Bookmark)

    @Delete
    fun delete(bookmark: Bookmark)

    @Query("SELECT * FROM Bookmark")
    fun getAllBookmarks(): LiveData<List<Bookmark>>
}