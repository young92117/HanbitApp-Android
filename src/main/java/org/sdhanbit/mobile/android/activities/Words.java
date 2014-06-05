package org.sdhanbit.mobile.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.activities.Share.InvStatus;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

public class Words {

	private static String TAG = "Words"; //This is actually a youtube
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	private static SimpleExpandableListItemAdapter mExpandableListItemAdapter;
	private static DisplayMetrics metrics;
	private static ListView list;
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
	
	public Words(Context context, FeedEntryManager feedEntryManager, View mainView)
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
    	Log.v(TAG, "Starting Words");
    	list = (ListView)(mainView.findViewById(R.id.list));
   	 	try
   	 	{
   	 		List<FeedEntry> entries = feedEntryManager.getFeedEntries("87");
	   	 	mExpandableListItemAdapter = new SimpleExpandableListItemAdapter(mContext, entries);
			AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
			alphaInAnimationAdapter.setAbsListView(list);
			list.setAdapter(alphaInAnimationAdapter);
			
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
		    	rl = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.words_content, null);
		    	webClient = null;
		    	touchListener = null;
		     }
		     rl.setMinimumWidth(metrics.widthPixels);
		     rl.setMinimumHeight(metrics.heightPixels/2);
		     final WebView wv = (WebView)rl.findViewById(R.id.webview);
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
    			
    			rl.setOnTouchListener(touchListener);
    			wv.setOnTouchListener(touchListener);
			 }
  	         
	    	return rl;
	    }
	}
}
