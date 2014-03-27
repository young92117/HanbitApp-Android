package org.sdhanbit.mobile.android.activities;

import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

public class Words {

	private static String TAG = "Words"; //This is actually a youtube
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	
	public Words(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Words");
    }
}
