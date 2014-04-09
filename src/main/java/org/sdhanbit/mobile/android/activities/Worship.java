package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Worship {

	private static String TAG = "Worship";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	public Worship(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Worship");
    }
}
