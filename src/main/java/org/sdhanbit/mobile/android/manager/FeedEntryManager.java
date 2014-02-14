package org.sdhanbit.mobile.android.manager;

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
import org.sdhanbit.mobile.android.entity.FeedEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeedEntryManager {

    private static final String TAG = FeedEntryManager.class.getName();
    @Inject
    private MainApplication application;
    @Inject
    private RssFeedDatabaseHelper rssFeedDatabaseHelper;

    public void addSyndFeed(SyndFeed syndFeed) {
        try {
            // get our Dao
            Dao<FeedEntry, Integer> feedEntryDao = getDao();
            List entries = syndFeed.getEntries();
            for (int i = 0; i < entries.size(); i++) {

                SyndEntry entry = (SyndEntry) entries.get(i);
                if (!doesFeedEntryExist(feedEntryDao, entry)) {
                    // (String author, String title, String link, String content, String description, Date publishedDate, String category)
                    String content = getMergedContent(entry);
                    String category = getCategories(entry.getCategories());

                    FeedEntry feedEntry = new FeedEntry(entry.getAuthor(), entry.getTitle(), entry.getLink(),
                            content, entry.getDescription().getValue(), entry.getPublishedDate(), category);
                    feedEntryDao.create(feedEntry);

                    Log.d(TAG, "added " + entry.getTitle());
                }
            }

        } catch (SQLException exception) {
            Log.e(TAG, "Database exception", exception);
        }

    }

    /*
    Returns comma-delimited categories.
     */
    private String getCategories(List categories) {
        StringBuilder builder = new StringBuilder();

        if (categories != null) {
            for (Iterator<SyndCategory> iterator = categories.iterator(); iterator.hasNext();) {
                SyndCategory syndCategory = iterator.next();
                builder.append(syndCategory.getName() + ",");
            }
        }

        return builder.toString();
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
            Dao<FeedEntry, Integer> feedEntryDao = getDao();

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
            Dao<FeedEntry, Integer> feedEntryIntegerDao = getDao();

            feedEntry.setIsViewed(true);
            feedEntryIntegerDao.update(feedEntry);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Database exception", e);
        }
    }

    private Dao<FeedEntry, Integer> getDao() throws SQLException {
        return getHelper().getDao();
    }

    private RssFeedDatabaseHelper getHelper() {
        if (rssFeedDatabaseHelper == null) {
            rssFeedDatabaseHelper = OpenHelperManager.getHelper(application, RssFeedDatabaseHelper.class);
        }

        return rssFeedDatabaseHelper;
    }

    private boolean doesFeedEntryExist(Dao<FeedEntry, Integer> feedEntryDao, SyndEntry entry) {

        boolean entryExist = false;

        try {
            // Look for an entry with same author and title
            String author = entry.getAuthor();
            String title = entry.getTitle();

            long total = feedEntryDao.countOf(feedEntryDao.queryBuilder().setCountOf(true)
                    .where().eq("author", author).and().eq("title", title).prepare());
            entryExist = total > 0;
            Log.v(TAG, "Duplicate feed entry found.");

        } catch (SQLException exception) {
            Log.e(TAG, "Database exception", exception);
        }

        return entryExist;
    }
}
