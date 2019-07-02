package com.bleacherreport.flickrfindr.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecentSearch {

    @PrimaryKey
    @NonNull
    public String search;

    public RecentSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }
}
