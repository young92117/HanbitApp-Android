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
import android.graphics.Bitmap;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class News {
	private static String TAG = "News";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private static View mainView;
	private static SimpleExpandableListItemAdapter mExpandableListItemAdapter;
	private static DisplayMetrics metrics;
	private static ListView news_list;
	private static ArrayList<InvStatus> invStatuses;
	private static View.OnTouchListener touchListener;
	private static WebViewClient webClient;
	
	class InvStatus
	{
		public boolean isNew;
		public InvStatus()
		{
			isNew = false;
		}
	}
	
	public News(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
		metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
	}
	public void constructNews()
    {
    	Log.v(TAG, "Starting News");
   	 	news_list = (ListView)(mainView.findViewById(R.id.news_list));
   	 	try
   	 	{
   	 		List<FeedEntry> entries = feedEntryManager.getFeedEntries("15");
	   	 	mExpandableListItemAdapter = new SimpleExpandableListItemAdapter(mContext, entries);
			AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
			alphaInAnimationAdapter.setAbsListView(news_list);
			news_list.setAdapter(alphaInAnimationAdapter);
			
			invStatuses = new ArrayList<InvStatus>();
			for(int i=0; i < entries.size(); i++)
			{
				invStatuses.add(new InvStatus());
			}
			
   	 	}catch(IllegalArgumentException e)
   	 	{
   	 		Toast.makeText(mContext, "Database is not ready yet. Please try again", Toast.LENGTH_LONG).show();
   	 	}
    	
    	
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
	    public View getContentView(final int position, View convertView, final ViewGroup parent) {
	    	
	    	 RelativeLayout rl = (RelativeLayout) convertView;
		        
		     if (rl == null) {
		    	rl = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.news_content, null);
		    	webClient = null;
		    	touchListener = null;
		     }
		     
		     rl.setMinimumWidth(metrics.widthPixels);
		     rl.setMinimumHeight(metrics.heightPixels/2);
		     final WebView wv = (WebView)rl.findViewById(R.id.news_webview);
		     wv.loadDataWithBaseURL(null, getItem(position).getContent().replace('\n'+"", "<br>"), "text/html", "UTF-8",null);
       		 wv.getSettings().setBuiltInZoomControls(false);
       		 final RelativeLayout rl1 = rl;
       		 if(webClient == null)
       		 {
       			 webClient = new WebViewClient(){
            			public void onPageFinished(WebView view, String url) {
               				rl1.invalidate();
               				invStatuses.get(position).isNew = false;
               			}
               		 }; 
               	 wv.setWebViewClient(webClient);
       		 }
       		 
       		 if(touchListener == null)
       		 {
       			touchListener = new View.OnTouchListener() {
   				 @Override
   				 public boolean onTouch(View v, MotionEvent event) {
   					 if(!invStatuses.get(position).isNew)
   					 {
   						 rl1.setMinimumHeight(wv.getMeasuredHeight());
   						 mExpandableListItemAdapter.notifyDataSetChanged();
   						 invStatuses.get(position).isNew = true;
   					 }
   					 return false;
   				 }
       			};
       			wv.setOnTouchListener(touchListener);
       			rl.setOnTouchListener(touchListener);
   			 }
       		   	         
	    	 return rl;
	    }
	}
}
