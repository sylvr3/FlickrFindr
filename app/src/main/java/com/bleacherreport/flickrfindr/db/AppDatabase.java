package com.bleacherreport.flickrfindr.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Bookmark.class, RecentSearch.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase mInstance;

    public abstract RecentSearchesDao recentSearchesDao();

    public abstract BookmarksDao bookmarksDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = create(context);
        }
        return mInstance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "photos").build();
    }

}
