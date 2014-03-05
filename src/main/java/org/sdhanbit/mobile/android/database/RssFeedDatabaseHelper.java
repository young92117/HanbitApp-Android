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

public class RssFeedDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = RssFeedDatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "RssFeed.db";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<FeedEntry, Integer> feedEntryDao = null;
    private Dao<Category, Integer> categoryDao = null;
    private Dao<FeedEntryCategory, Integer> feedEntryCategoryDao = null;

    private RuntimeExceptionDao<Category, Integer> categoryRuntimeExceptionDao = null;
    private RuntimeExceptionDao<FeedEntry, Integer> feedEntryRuntimeExceptionDao = null;
    private RuntimeExceptionDao<FeedEntryCategory, Integer> feedEntryCategoryRuntimeExceptionDao = null;

    @Inject
    public RssFeedDatabaseHelper(Context context) {
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
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, FeedEntryCategory.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
        insertTestData();
    }

    private void insertTestData() {
        // here we try inserting data in the on-create as a test
        try {
            Dao<Category, Integer> categoryDao = getCategoryDao();
            Category category = new Category("Testing");
            categoryDao.create(category);
            Log.i(TAG, "created new category in onCreate: " + category.getName());

//            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();
//            FeedEntry entry1 = new FeedEntry("Sang Yum", "New Entry", "http://www.sdhanbit.org/", "Content", "Description", new Date(System.currentTimeMillis()));
//            feedEntryDao.create(entry1);
//            Log.i(TAG, "created new entries in onCreate: " + entry1.getDescription());
//            FeedEntry entry2 = new FeedEntry("Sang Yum", "New Entry1", "http://www.sdhanbit.org/", "Content1", "Description1", new Date(System.currentTimeMillis()));
//            feedEntryDao.create(entry2);
//            Log.i(TAG, "created new entries in onCreate: " + entry2.getDescription());

//            Dao<FeedEntryCategory, Integer> feedEntryCategoryDao = getFeedEntryCategoryDao();
//            FeedEntryCategory feedEntryCategory1 = new FeedEntryCategory(category, entry1);
//            feedEntryCategoryDao.create(feedEntryCategory1);
//            FeedEntryCategory feedEntryCategory2 = new FeedEntryCategory(category, entry2);
//            feedEntryCategoryDao.create(feedEntryCategory2);
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
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, FeedEntryCategory.class, true);
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

    public Dao<Category, Integer> getCategoryDao() throws SQLException {
        if (categoryDao == null) {
            categoryDao = getDao(Category.class);
        }
        return categoryDao;
    }

    public Dao<FeedEntryCategory, Integer> getFeedEntryCategoryDao() throws SQLException {
        if (feedEntryCategoryDao == null) {
            feedEntryCategoryDao = getDao(FeedEntryCategory.class);
        }
        return feedEntryCategoryDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        feedEntryDao = null;
        feedEntryCategoryDao = null;
        categoryDao = null;
    }
}