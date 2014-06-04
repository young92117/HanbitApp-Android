package org.sdhanbit.mobile.android.activities;

import java.util.List;

import org.sdhanbit.mobile.android.R;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

public class Ministry {

	private static String TAG = "Ministry";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	private static SimpleExpandableListItemAdapter mExpandableListItemAdapter;
	private static DisplayMetrics metrics;
	private static ListView list;
	
	public Ministry(Context context, FeedEntryManager feedEntryManager, View mainView)
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
    	Log.v(TAG, "Starting Ministry");
    	list = (ListView)(mainView.findViewById(R.id.list));
   	 	try
   	 	{
   	 		List<FeedEntry> entries = feedEntryManager.getFeedEntries("14");
	   	 	mExpandableListItemAdapter = new SimpleExpandableListItemAdapter(mContext, entries);
			AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
			alphaInAnimationAdapter.setAbsListView(list);
			list.setAdapter(alphaInAnimationAdapter);
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
	    public View getContentView(int position, View convertView, final ViewGroup parent) {
	    	
	    	 RelativeLayout rl = (RelativeLayout) convertView;
		        
		     if (rl == null) {
		    	final RelativeLayout rl1 = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.ministry_content, null);
		        rl1.setMinimumWidth(metrics.widthPixels);
		        rl1.setMinimumHeight(metrics.heightPixels/2);
		        final WebView wv = (WebView)rl1.findViewById(R.id.webview);
		        wv.loadDataWithBaseURL(null, getItem(position).getContent().replace('\n'+"", "<br>"), "text/html", "UTF-8",null);
       		    wv.setInitialScale(200);
       		    wv.getSettings().setBuiltInZoomControls(true);
       		    wv.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(rl1.getHeight() <= metrics.heightPixels/2)
						{
//							rl1.setMinimumHeight(wv.getMeasuredHeight());
							mExpandableListItemAdapter.notifyDataSetChanged();
						}
						return false;
					}
				});
  	            return rl1;
		     }
		     
	    	 return rl;
	    }
	}
}
