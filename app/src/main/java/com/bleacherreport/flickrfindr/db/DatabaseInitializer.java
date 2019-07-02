package com.bleacherreport.flickrfindr.db;

import android.util.Log;
import com.bleacherreport.flickrfindr.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInitializer {

    public static RecentSearch addRecentSearch(final AppDatabase db, final RecentSearch recentSearch) {
        AppExecutors.Companion.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.recentSearchesDao().insert(recentSearch);
                Log.i("Added recent search: ", recentSearch.getSearch());
            }
        });
        return recentSearch;
    }

    public static Bookmark addBookmark(final AppDatabase db, final Bookmark bookmark) {
        AppExecutors.Companion.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.bookmarksDao().insert(bookmark);
                Log.i("Added bookmark: ", bookmark.getUrl());

            }
        });
        return bookmark;
    }

    public static void removeBookmark(final AppDatabase db, final Bookmark bookmark) {
        AppExecutors.Companion.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.bookmarksDao().delete(bookmark);
                Log.i("Removed bookmark: ", bookmark.getUrl());
            }
        });

    }


    public static List<String> getRecentSearches(final AppDatabase db) {
        final List<String> searchesSpinnerValues = new ArrayList<String>();
        AppExecutors.Companion.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<String> recentSearches;
                recentSearches = db.recentSearchesDao().selectAll();
                for (String recentSearch : recentSearches) {
                    searchesSpinnerValues.add(recentSearch);
                }
            }

        });

        return searchesSpinnerValues;
    }

}
