package org.sdhanbit.mobile.android.rss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.R;
import org.xmlpull.v1.XmlPullParserException;
import roboguice.receiver.RoboBroadcastReceiver;
import roboguice.util.RoboAsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

            // TODO: Save to MySql database
            List entries = syndFeed.getEntries();
            for (int i = 0; i < entries.size(); i++) {
                SyndEntry entry = (SyndEntry) entries.get(i);
                Log.d(TAG, entry.getTitle());
                Log.d(TAG, entry.getLink());
                Log.d(TAG, entry.getDescription().getValue());
            }
        }
    }
}
