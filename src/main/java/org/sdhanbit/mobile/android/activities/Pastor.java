package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Pastor {

	private static String TAG = "Pastor"; //This is actually a youtube
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	public Pastor(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Pastor");
    }
}
