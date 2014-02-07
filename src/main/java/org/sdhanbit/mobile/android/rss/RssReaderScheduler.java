package org.sdhanbit.mobile.android.rss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.atom.Feed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.database.RssFeedDatabaseHelper;
import org.sdhanbit.mobile.android.entity.FeedEntry;
import org.xmlpull.v1.XmlPullParserException;
import roboguice.receiver.RoboBroadcastReceiver;
import roboguice.util.RoboAsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

// Schedules RssReading.
// For now, it invokes RssReader every minute.
public class RssReaderScheduler extends RoboBroadcastReceiver {

    @Inject
    private AlarmManager mAlarmManager;

    private static final String TAG = "RssReaderScheduler";

    public void setAlarm(Context context) {
        Intent intent = new Intent(context, RssReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // TODO: Read the sleep time from configuration
        int sleepTime = 1000 * 60 * 10;
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), sleepTime, pendingIntent);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, RssReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager.cancel(pendingIntent);
    }

    @Override
    protected void handleReceive(Context context, Intent intent) {
        try {
            // TODO: Read RSS Feed URL from configuration
            // TODO: Check for Internet Connection before attempting to download RSS Feed
            String feedUrl = "http://www.sdhanbit.org/wordpress/?feed=podcast";
            new RssReadTask(context, feedUrl).execute();

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public class RssReadTask extends RoboAsyncTask<SyndFeed> {

        final String feedUrl;
        private RssFeedDatabaseHelper rssFeedDatabaseHelper;

        public RssReadTask(Context context, String feedUrl) {
            super(context);
            this.feedUrl = feedUrl;
        }

        @Override
        public SyndFeed call() throws Exception {
            return new RssAtomFeedRetriever().getMostRecentNews(feedUrl);
        }

        @Override
        protected void onSuccess(SyndFeed syndFeed) throws Exception {

            List entries = syndFeed.getEntries();
            for (int i = 0; i < entries.size(); i++) {
                SyndEntry entry = (SyndEntry) entries.get(i);
                Log.d(TAG, entry.getTitle());
                Log.d(TAG, entry.getLink());
                Log.d(TAG, entry.getDescription().getValue());
            }

            saveFeedEntries(syndFeed);
        }

        private void saveFeedEntries(SyndFeed syndFeed) {
            try {
                // get our Dao
                Dao<FeedEntry, Integer> feedEntryDao = getHelper().getDao();
                // for now, simply insert everything
                List entries = syndFeed.getEntries();
                for (int i = 0; i < entries.size(); i++) {
                    // TODO: Look for duplicates before inserting
                    SyndEntry entry = (SyndEntry) entries.get(i);

                    if (!doesFeedEntryExist(entry)) {
                        // (String author, String title, String link, String content, String description, Date publishedDate, String category)
                        String content = null;
                        if (entry.getContents() != null) {
                            for (Iterator<?> it = entry.getContents().iterator(); it.hasNext();) {
                                SyndContent syndContent = (SyndContent)it.next();

                                if (syndContent != null) {
                                    content = syndContent.getValue();
                                }
                            }
                        }

                        FeedEntry feedEntry = new FeedEntry(entry.getAuthor(), entry.getTitle(), entry.getLink(),
                                content, entry.getDescription().getValue(), entry.getPublishedDate(), "Testing");
                        feedEntryDao.create(feedEntry);
                    }
                }

            } catch (SQLException exception) {
                Log.e(TAG, "Database exception", exception);
            }
        }

        private boolean doesFeedEntryExist(SyndEntry entry) {

            boolean entryExist = false;

            try {
                Dao<FeedEntry, Integer> feedEntryDao = getHelper().getDao();
                // Look for an entry with same author and title
                String author = entry.getAuthor();
                String title = entry.getTitle();

                long total = feedEntryDao.countOf(feedEntryDao.queryBuilder().setCountOf(true)
                        .where().eq("author", author).and().eq("title", title).prepare());
                entryExist = total > 0;

            } catch (SQLException exception) {
                Log.e(TAG, "Database exception", exception);
            }

            return entryExist;
        }

        private RssFeedDatabaseHelper getHelper() {
            if (rssFeedDatabaseHelper == null) {
                rssFeedDatabaseHelper = OpenHelperManager.getHelper(context, RssFeedDatabaseHelper.class);
            }

            return rssFeedDatabaseHelper;
        }
    }
}
