package org.sdhanbit.mobile.android.activities;

import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import roboguice.activity.event.OnCreateEvent;

public class Actions {//} extends Activity {

	private static String TAG = "Actions";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

    public Actions(){}

	public Actions(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;

	}

	public void construct()
    {
    	Log.v(TAG, "Starting Actions");
        WebView browser = (WebView) (mainView.findViewById(R.id.WebViewAct));
        browser.loadUrl("https://docs.google.com/spreadsheet/pub?key=0Ahw6lNCJGfZ6dDNJcm9IT0lqVWVZNU5Zc3B0ZklfSGc&output=html");
    }

    //public void onCreate(Bundle savedInstanceState) {
        //WebView browser = (WebView) findViewById(R.id.WebViewAct);
        //setContentView(R.layout.actions);
        //browser.loadUrl("http://www.google.com");
    //}

}
