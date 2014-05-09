package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Map {

	private static String TAG = "Map";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	public Map(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Map");
    }
}
