package com.bleacherreport.flickrfindr.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecentSearchesDao {
    @Query("SELECT search FROM RecentSearch")
    List<String> selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecentSearch... model);
}
