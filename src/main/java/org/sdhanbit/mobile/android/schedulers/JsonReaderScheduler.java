package org.sdhanbit.mobile.android.schedulers;

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
import org.sdhanbit.mobile.android.managers.UserPreferenceManager;

import roboguice.receiver.RoboBroadcastReceiver;

public class JsonReaderScheduler extends RoboBroadcastReceiver {

    @Inject
    private AlarmManager mAlarmManager;
    @Inject
    private UserPreferenceManager userPreferenceManager;

    private static final String TAG = "JsonReaderScheduler";
    private static FeedAPIRunner mFeedAPIRunner;

    public void setAlarm(Context context) {
        Intent intent = new Intent(context, JsonReaderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // TODO: Read the sleep time from configuration
        int sleepTime = userPreferenceManager.getFeedRefreshFrequencyInMinutes() * 60 * 1000;
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), sleepTime, pendingIntent);
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
   			parameters.putString("pubDate", "");
   			parameters.putString("count", "");
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
			
			Log.v(TAG,response);
			JSONArray feed_array;
			try {
				JSONObject json = Util.parseJson(response);
				feed_array = json.getJSONArray("data");
			} catch (JSONException e) {
			} catch (FeedAPIError e) {
			} catch (Exception e)
			{
			}
			
			//TODO: Insert feed information to the database here.
		}
    }
}
