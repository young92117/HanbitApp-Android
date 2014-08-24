package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

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
        browser.getSettings().setBuiltInZoomControls(true);
        browser.loadUrl("https://docs.google.com/spreadsheet/pub?key=0Ahw6lNCJGfZ6dDNJcm9IT0lqVWVZNU5Zc3B0ZklfSGc&output=html");
    }

    //public void onCreate(Bundle savedInstanceState) {
        //WebView browser = (WebView) findViewById(R.id.WebViewAct);
        //setContentView(R.layout.actions);
        //browser.loadUrl("http://www.google.com");
    //}

}
