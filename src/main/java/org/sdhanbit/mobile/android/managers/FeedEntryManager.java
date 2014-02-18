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
import org.sdhanbit.mobile.android.MainApplication;
import org.sdhanbit.mobile.android.database.RssFeedDatabaseHelper;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeedEntryManager extends BaseRssFeedDatabaseManager {

    private static final String TAG = FeedEntryManager.class.getName();

    @Inject
    CategoryManager categoryManager;
    @Inject
    FeedEntryCategoryManager feedEntryCategoryManager;

    public void addSyndFeed(SyndFeed syndFeed) {
        try {
            // get our Dao
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();
            List entries = syndFeed.getEntries();
            for (int i = 0; i < entries.size(); i++) {

                SyndEntry entry = (SyndEntry) entries.get(i);
                if (!doesFeedEntryExist(entry)) {
                    String content = getMergedContent(entry);
                    List<Category> categories = getCategories(entry.getCategories());

                    categoryManager.insertCategories(categories);

                    FeedEntry feedEntry = new FeedEntry(entry.getAuthor(), entry.getTitle(), entry.getLink(),
                            content, entry.getDescription().getValue(), entry.getPublishedDate());
                    feedEntryDao.create(feedEntry);

                    feedEntryCategoryManager.insertFeedEntryCategories(feedEntry, categories);

                    Log.d(TAG, "added " + entry.getTitle());
                }
            }

        } catch (SQLException exception) {
            Log.e(TAG, "Database exception", exception);
        }

    }

    private List<Category> getCategories(List syndCategories) {
        List<Category> categories = new ArrayList<Category>();

        if (categories != null) {
            for (Iterator<SyndCategory> iterator = syndCategories.iterator(); iterator.hasNext();) {
                SyndCategory syndCategory = iterator.next();
                categories.add(new Category(syndCategory.getName()));
            }
        }

        return categories;
    }

    private String getMergedContent(SyndEntry entry) {
        StringBuilder builder = new StringBuilder();

        if (entry.getContents() != null) {
            for (Iterator<?> it = entry.getContents().iterator(); it.hasNext();) {
                SyndContent syndContent = (SyndContent)it.next();

                if (syndContent != null) {
                    builder.append(syndContent.getValue());
                }
            }
        }
        return builder.toString();
    }

    public List<FeedEntry> getFeedEntries(String category) {

        List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();

        try {
            Dao<FeedEntry, Integer> feedEntryDao = getFeedEntryDao();

            PreparedQuery<FeedEntry> query = feedEntryDao.queryBuilder().where().like("category", "%" + category + "%").prepare();
            feedEntries = feedEntryDao.query(query);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Database exception", e);
        }

        return feedEntries;
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

    private boolean doesFeedEntryExist(SyndEntry entry) {

        boolean entryExist = false;

        try {
            Dao<FeedEntry, Integer> feedEntryIntegerDao = getFeedEntryDao();

            FeedEntry feedEntry = feedEntryIntegerDao.queryForFirst(
                    feedEntryIntegerDao.queryBuilder()
                            .where()
                            .eq("author", entry.getAuthor())
                            .and()
                            .eq("title", entry.getTitle()).prepare());
            entryExist = feedEntry != null;
            Log.v(TAG, "Duplicate feed entry found.");

        } catch (SQLException exception) {
            exception.printStackTrace();
            Log.e(TAG, "Database exception", exception);
        }

        return entryExist;
    }
}
