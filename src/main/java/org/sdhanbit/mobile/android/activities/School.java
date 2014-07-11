package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class School {

	private static String TAG = "School";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

    public School(){}

	public School(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
        Log.v(TAG, "Starting School");
        WebView browser = (WebView) (mainView.findViewById(R.id.WebViewSchool));
        browser.loadUrl("http://www.sdhanbit.org/wordpress/mobile/culture_school.html");
    }
}
