package org.sdhanbit.mobile.android.managers;

import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndCategory;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import org.json.JSONObject;
import org.sdhanbit.mobile.android.MainApplication;
import org.sdhanbit.mobile.android.database.RssFeedDatabaseHelper;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeedEntryManager extends BaseJsonFeedDatabaseManager {

    private static final String TAG = "FeedEntryManager";

    public void addJsonFeed(String date, String title, String content, String type) 
    {
        try {
            // get our Dao
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();
            FeedEntry feedEntry = new FeedEntry(date, title, content, type);
            feedEntryDao.create(feedEntry);
            Log.d(TAG, "added " + date+" "+type);
        } catch (SQLException exception) {
            Log.e(TAG, "Database exception", exception);
        }

    }

    public List<FeedEntry> getFeedEntries(String type) {

        List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();

        try {
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();

            PreparedQuery<FeedEntry> query = feedEntryDao.queryBuilder()
            		                         .orderBy("date", false).where().eq("type", type).prepare();
            feedEntries = feedEntryDao.query(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Database exception", e);
        }

        return feedEntries;
    }
    
    public String getTheRecentDate(String type) {
    	
    	String date="";
        List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();

        try {
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();

            PreparedQuery<FeedEntry> query = feedEntryDao.queryBuilder()
            		                         .orderBy("date", false).where().eq("type", type).prepare();
            feedEntries = feedEntryDao.query(query);
            if(feedEntries.size() != 0)
            	date=feedEntries.get(0).getDate();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Database exception", e);
        }

        return date;
    }

    public void markFeedEntryAsViewed(FeedEntry feedEntry) {
        try {
            Dao<FeedEntry, Integer> feedEntryIntegerDao = getFeedEntryDao();

            feedEntry.setIsViewed(true);
            feedEntryIntegerDao.update(feedEntry);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Database exception", e);
        }
    }

//    private boolean doesFeedEntryExist(SyndEntry entry) {
//
//        boolean entryExist = false;
//
//        try {
//            Dao<FeedEntry, Integer> feedEntryIntegerDao = getFeedEntryDao();
//
//            FeedEntry feedEntry = feedEntryIntegerDao.queryForFirst(
//                    feedEntryIntegerDao.queryBuilder()
//                            .where()
//                            .eq("author", entry.getAuthor())
//                            .and()
//                            .eq("title", entry.getTitle()).prepare());
//            entryExist = feedEntry != null;
//            Log.v(TAG, "Duplicate feed entry found.");
//
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//            Log.e(TAG, "Database exception", exception);
//        }
//
//        return entryExist;
//    }
}
