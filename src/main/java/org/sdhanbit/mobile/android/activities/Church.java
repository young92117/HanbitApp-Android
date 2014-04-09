package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Church {

	private static String TAG = "Church";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	public Church(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Church");
    }
}
