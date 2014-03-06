package org.sdhanbit.mobile.android.database;

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
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;
import java.util.Date;

public class JsonFeedDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "JsonFeedDatabaseHelper";

    private static final String DATABASE_NAME = "JsonFeed.db";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<FeedEntry, Integer> feedEntryDao = null;

    private RuntimeExceptionDao<FeedEntry, Integer> feedEntryRuntimeExceptionDao = null;

    @Inject
    public JsonFeedDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.json_ormlite_config);
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
        insertTestData();
    }

    private void insertTestData() {
        // here we try inserting data in the on-create as a test
        try {
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();
            FeedEntry entry1 = new FeedEntry("20110201135959", "title1", "http://www.sdhanbit.org/", "40");
            feedEntryDao.create(entry1);
            Log.i(TAG, "created new entries in onCreate: " + entry1.getDate());
            FeedEntry entry2 = new FeedEntry("20130201135959", "title2", "http://www.sdhanbit.org/", "40");
            feedEntryDao.create(entry2);
            Log.i(TAG, "created new entries in onCreate: " + entry2.getDate());

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't insert the sample data", e);
        }
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
    public Dao<FeedEntry, Integer> getFeedEntryDao() throws SQLException {
        if (feedEntryDao == null) {
            feedEntryDao = getDao(FeedEntry.class);
        }
        return feedEntryDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        feedEntryDao = null;
    }
}