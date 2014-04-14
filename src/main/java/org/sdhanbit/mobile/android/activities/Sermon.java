package org.sdhanbit.mobile.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Sermon {
	private static String TAG = "Sermon";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	private SimpleExpandableListItemAdapter mExpandableListItemAdapter;
	private static DisplayMetrics metrics;
	
	public Sermon(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
		metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
	}
	public void construct()
    {
    	Log.v(TAG, "Starting Sermon");
   	 	ListView news_list = (ListView)(mainView.findViewById(R.id.sermon_list));
   	 	List<FeedEntry> entries = feedEntryManager.getFeedEntries("30");
    	
    	mExpandableListItemAdapter = new SimpleExpandableListItemAdapter(mContext, entries);
		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
		alphaInAnimationAdapter.setAbsListView(news_list);
		news_list.setAdapter(alphaInAnimationAdapter);
    }
	
	private static class SimpleExpandableListItemAdapter extends ExpandableListItemAdapter<FeedEntry> {

	    private Context mContext;

	    /*
	     * This will create a new ExpandableListItemAdapter, providing a custom layout resource, 
	     * and the two child ViewGroups' id's. If you don't want this, just pass either just the
	     * Context, or the Context and the List<T> up to super.
	     */
	    private SimpleExpandableListItemAdapter(Context context, List<FeedEntry> items) {
	        super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
	        mContext = context;         
	    }

	    @Override
	    public View getTitleView(int position, View convertView, ViewGroup parent) {
	        TextView tv = (TextView) convertView;
	        if (tv == null) {
	            tv = new TextView(mContext);
	            tv.setTextColor(Color.BLACK);
	        }
	        tv.setText((CharSequence)(getItem(position)).getTitle());
	        return tv;
	    }

	    @Override
	    public View getContentView(int position, View convertView, ViewGroup parent) {
	    	RelativeLayout rl = (RelativeLayout) convertView;
	        
		     if (rl == null) {
		         rl = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.news_content, null);
		         rl.setMinimumHeight(metrics.heightPixels);
		         rl.setMinimumWidth(metrics.widthPixels);
		         WebView wv = (WebView)rl.findViewById(R.id.news_webview);
		         wv.setVerticalScrollBarEnabled(true);
		         wv.setHorizontalScrollBarEnabled(true);
		         wv.getSettings().setJavaScriptEnabled(true);
			     wv.loadDataWithBaseURL(null, getItem(position).getContentDisplay(), "text/html", "UTF-8",null);
			     wv.setWebChromeClient(new WebChromeClient());
		     }
		       
		    return rl;
	    }
	}
}
