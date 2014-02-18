package org.sdhanbit.mobile.android.managers;

import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import org.sdhanbit.mobile.android.MainApplication;
import org.sdhanbit.mobile.android.database.RssFeedDatabaseHelper;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;

/**
 * Created by syum on 2/15/14.
 */
public abstract class BaseRssFeedDatabaseManager {

    @Inject
    private MainApplication application;

    private RssFeedDatabaseHelper rssFeedDatabaseHelper;

    protected Dao<Category,Integer> getCategoryDao() throws SQLException {
        return getHelper().getCategoryDao();
    }

    protected Dao<FeedEntry, Integer> getFeedEntryDao() throws SQLException {
        return getHelper().getFeedEntryDao();
    }

    protected Dao<FeedEntryCategory, Integer> getFeedEntryCategoryDao() throws SQLException {
        return getHelper().getFeedEntryCategoryDao();
    }

    private RssFeedDatabaseHelper getHelper() {
        if (rssFeedDatabaseHelper == null) {
            rssFeedDatabaseHelper = OpenHelperManager.getHelper(application, RssFeedDatabaseHelper.class);
        }

        return rssFeedDatabaseHelper;
    }
}
