package org.sdhanbit.mobile.android.managers;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;
import java.util.List;

public class FeedEntryCategoryManager extends BaseRssFeedDatabaseManager {

    private static final String TAG = FeedEntryCategoryManager.class.getName();

    public void insertFeedEntryCategories(FeedEntry feedEntry, List<Category> categories) {
        try {
            Dao<FeedEntryCategory, Integer> dao = getFeedEntryCategoryDao();
            for (Category category : categories) {
                dao.create(new FeedEntryCategory(category, feedEntry));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't insert FeedEntryCategories", e);
        }
    }
}
