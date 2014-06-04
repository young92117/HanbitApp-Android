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
    SimpleDateFormat feed_date_fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    SimpleDateFormat db_date_fmt = new SimpleDateFormat("yyyyMMddHHmmss");

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
    	mFeedAPIRunner = new FeedAPIRunner(new FeedAPI());
    	
    	requestJsonFeed(61); //Share
    	requestJsonFeed(47); //Events
    	requestJsonFeed(14); //Ministry 
    	requestJsonFeed(15); //News
    	requestJsonFeed(30); //Sermon (Youtube)
    	requestJsonFeed(87); //Words
    }
    
    public void requestJsonFeed(int cat)
    {
    	Bundle parameters = new Bundle();
    	String theLastDate;
    	try {
    		theLastDate = feedEntryManager.getTheRecentDate(cat+"");
        	if(!theLastDate.equals(""))
        		parameters.putString("after", theLastDate);
        	parameters.putString("per_page", "20");
   			parameters.putString("cat", cat+"");
        	mFeedAPIRunner.feed(parameters, "GET", new FeedRequestListener(cat));
        	Log.v(TAG,cat + " Json is requested after"+ theLastDate);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
    public class FeedRequestListener extends BaseRequestListener {
    	String type;
    	public FeedRequestListener(int cat)
    	{
    		this.type = cat+"";
    	}
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
			String content_display = "";
			try {
				JSONObject json = Util.parseJson(response);
				feed_array = json.getJSONArray("posts");
				for (int i = 0; i < feed_array.length(); i++) 
				{
					try
					{
						date = db_date_fmt.format(feed_date_fmt.parse(feed_array.getJSONObject(i).getString("date")));
						title = feed_array.getJSONObject(i).getString("title");
						content = feed_array.getJSONObject(i).getString("content");
						content_display = feed_array.getJSONObject(i).getString("content_display");
						Log.v(TAG,"Date: "+date+"\n"+
								  "title: "+title+"\n"+
								  "content: "+content+"\n"+
								  "content_display: "+content_display+"\n"+
								  "type: "+type+"\n\n");
						feedEntryManager.addJsonFeed(date, title, content, content_display, type);
					}catch(Exception e)
					{
						Log.e(TAG,e.getMessage());
					}
				}
			} catch (Exception e) {
				Log.e(TAG,e.getMessage());
			} catch (FeedAPIError e) {
				Log.e(TAG,e.getMessage());
			} 
		}
    }
}
