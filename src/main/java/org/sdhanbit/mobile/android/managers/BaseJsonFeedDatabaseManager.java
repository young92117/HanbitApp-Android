package org.sdhanbit.mobile.android.managers;

import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.sdhanbit.mobile.android.MainApplication;
import org.sdhanbit.mobile.android.database.JsonFeedDatabaseHelper;
import org.sdhanbit.mobile.android.database.RssFeedDatabaseHelper;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;

/**
 * Created by syum on 2/15/14.
 */
public abstract class BaseJsonFeedDatabaseManager {

    @Inject
    private MainApplication application;

    private JsonFeedDatabaseHelper jsonFeedDatabaseHelper;

    protected Dao<FeedEntry, Integer> getFeedEntryDao() throws SQLException {
        return getHelper().getFeedEntryDao();
    }

    private JsonFeedDatabaseHelper getHelper() {
        if (jsonFeedDatabaseHelper == null) {
            jsonFeedDatabaseHelper = OpenHelperManager.getHelper(application, JsonFeedDatabaseHelper.class);
        }

        return jsonFeedDatabaseHelper;
    }
}
