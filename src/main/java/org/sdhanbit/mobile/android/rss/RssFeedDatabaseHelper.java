package org.sdhanbit.mobile.android.rss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.sdhanbit.mobile.android.MainApplication;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entity.FeedEntry;

import java.sql.SQLException;
import java.util.Date;

public class RssFeedDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = RssFeedDatabaseHelper.class.getName();

    public static final String TABLE_FEED_ENTRY = "FeedEntry";
    // Id, Author, Title, Link, Description, Content, PublishedDate, Category
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_PUBLISHED_DATE = "publishedDate";
    public static final String COLUMN_CATEGORY = "category";

    private static final String DATABASE_NAME = "RssFeed.db";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<FeedEntry, Integer> feedEntryDao = null;
    private RuntimeExceptionDao<FeedEntry, Integer> feedEntryRuntimeExceptionDao = null;

    @Inject
    public RssFeedDatabaseHelper(MainApplication context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            TableUtils.createTable(connectionSource, FeedEntry.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<FeedEntry, Integer> dao = getFeedEntryRuntimeExceptionDao();
        // create some entries in the onCreate
        FeedEntry entry = new FeedEntry("Sang Yum", "New Entry", "http://www.sdhanbit.org/", "Content", "Description", new Date(System.currentTimeMillis()), "Testing");
        dao.create(entry);
        entry = new FeedEntry("Sang Yum", "New Entry1", "http://www.sdhanbit.org/", "Content1", "Description1", new Date(System.currentTimeMillis()), "Testing");
        dao.create(entry);
        Log.i(TAG, "created new entries in onCreate: " + entry.getDescription());
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            Log.i(TAG, "onUpgrade");
            TableUtils.dropTable(connectionSource, FeedEntry.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our FeedEntry class. It will create it or just give the cached
     * value.
     */
    public Dao<FeedEntry, Integer> getDao() throws SQLException {
        if (feedEntryDao == null) {
            feedEntryDao = getDao(FeedEntry.class);
        }
        return feedEntryDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<FeedEntry, Integer> getFeedEntryRuntimeExceptionDao() {
        if (feedEntryRuntimeExceptionDao == null) {
            feedEntryRuntimeExceptionDao = getRuntimeExceptionDao(FeedEntry.class);
        }
        return feedEntryRuntimeExceptionDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        feedEntryDao = null;
        feedEntryRuntimeExceptionDao = null;
    }
}