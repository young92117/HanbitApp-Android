package org.sdhanbit.mobile.android.schedulers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;
import org.sdhanbit.mobile.android.managers.UserPreferenceManager;
import org.sdhanbit.mobile.android.rss.RssAtomFeedRetriever;
import roboguice.inject.InjectResource;
import roboguice.receiver.RoboBroadcastReceiver;
import roboguice.util.RoboAsyncTask;

public class RssReaderScheduler extends RoboBroadcastReceiver {

    @Inject
    private AlarmManager mAlarmManager;
    @Inject
    private UserPreferenceManager userPreferenceManager;

    @InjectResource(R.string.Announcement)
    private String announcementFeedUrl;
    @InjectResource(R.string.PastoralColumn)
    private String pastoralColumnFeedUrl;
    @InjectResource(R.string.Sermon)
    private String sermonFeedUrl;

    private static final String TAG = "RssReaderScheduler";

    public void setAlarm(Context context) {
        Intent intent = new Intent(context, RssReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // TODO: Read the sleep time from configuration
        int sleepTime = userPreferenceManager.getFeedRefreshFrequencyInMinutes() * 60 * 1000;
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
            Log.v(TAG, "Loading announcements...");
            new RssReadTask(context, announcementFeedUrl).execute();
            Log.v(TAG, "Loading pastoral columns...");
            new RssReadTask(context, pastoralColumnFeedUrl).execute();
            //Log.d(TAG, "Loading sermon videos...");
            //new RssReadTask(context, sermonFeedUrl).execute();
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public class RssReadTask extends RoboAsyncTask<SyndFeed> {

        final String feedUrl;

        @Inject
        private FeedEntryManager feedEntryManager;
        @Inject
        private RssAtomFeedRetriever rssAtomFeedRetriever;

        public RssReadTask(Context context, String feedUrl) {
            super(context);
            this.feedUrl = feedUrl;
        }

        @Override
        public SyndFeed call() throws Exception {
            return rssAtomFeedRetriever.getMostRecentNews(feedUrl);
        }

        @Override
        protected void onSuccess(SyndFeed syndFeed) throws Exception {
            feedEntryManager.addSyndFeed(syndFeed);
        }

        @Override
        protected void onThrowable(Throwable t) throws RuntimeException {
            Log.e(TAG, "Failed to retrieve the feed " + feedUrl, t);
        }
    }
}
