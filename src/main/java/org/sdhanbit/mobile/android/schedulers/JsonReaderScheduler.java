package org.sdhanbit.mobile.android.schedulers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.inject.Inject;

import org.sdhanbit.mobile.android.json.BaseRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sdhanbit.mobile.android.json.FeedAPI;
import org.sdhanbit.mobile.android.json.FeedAPIError;
import org.sdhanbit.mobile.android.json.FeedAPIRunner;
import org.sdhanbit.mobile.android.json.Util;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;
import org.sdhanbit.mobile.android.managers.UserPreferenceManager;

import roboguice.receiver.RoboBroadcastReceiver;

public class JsonReaderScheduler extends RoboBroadcastReceiver {

    @Inject
    private AlarmManager mAlarmManager;
    @Inject
    private UserPreferenceManager userPreferenceManager;
    @Inject
    private FeedEntryManager feedEntryManager;

    private static final String TAG = "JsonReaderScheduler";
    private static FeedAPIRunner mFeedAPIRunner;
    private static String theLastDate = "";
    SimpleDateFormat feed_date_fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    SimpleDateFormat db_date_fmt = new SimpleDateFormat("yyyyMMddHHmmss");

    public void setAlarm(Context context) {
        Intent intent = new Intent(context, JsonReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // TODO: Read the sleep time from configuration
        int sleepTime = userPreferenceManager.getFeedRefreshFrequencyInMinutes() * 60 * 1000;
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), sleepTime, pendingIntent);
        // TODO: Read the database and update theLastDate with the most recent feed's time
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, JsonReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager.cancel(pendingIntent);
    }

    @Override
    protected void handleReceive(Context context, Intent intent) {
        try {
        	mFeedAPIRunner = new FeedAPIRunner(new FeedAPI());
        	Bundle parameters = new Bundle();
        	if(!theLastDate.equals(""))
        		parameters.putString("after", theLastDate);
//   			parameters.putString("count", "");
        	mFeedAPIRunner.feed(parameters, "GET", new FeedRequestListener());
        	Log.v(TAG,"Json is requested");
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
    public class FeedRequestListener extends BaseRequestListener {
		public void onComplete(final String response) {

			if(response.equals("exception") || response.equals("timedout"))
			{
				Log.e(TAG,"Server connection timeout");
				return;
			}
			
			JSONArray feed_array;
			String date = "";
			String title = "";
			String content = "";
			JSONObject taxo;
			String type = "";
			try {
				JSONObject json = Util.parseJson(response);
				feed_array = json.getJSONArray("posts");
				for (int i = 0; i < feed_array.length(); i++) 
				{
					date = db_date_fmt.format(feed_date_fmt.parse(feed_array.getJSONObject(i).getString("date")));
					if(i == 0)
						theLastDate = date;
					title = feed_array.getJSONObject(i).getString("title");
					content = feed_array.getJSONObject(i).getString("content");
					taxo = feed_array.getJSONObject(i).getJSONObject("taxonomies").getJSONObject("category");
					type = taxo.toString().subSequence(2, 4).toString();
					Log.v(TAG,"Date: "+date+"\n"+
							  "title: "+title+"\n"+
							  "content: "+content+"\n"+
							  "type: "+type+"\n\n");
					feedEntryManager.addJsonFeed(date, title, content, type);
				}
			} catch (JSONException e) {
				Log.e(TAG,e.getMessage());
			} catch (FeedAPIError e) {
				Log.e(TAG,e.getMessage());
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			}
			
			//TODO: Insert feed information to the database here.
		}
    }
}
