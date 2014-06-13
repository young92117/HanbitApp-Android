package org.sdhanbit.mobile.android.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import com.haarman.listviewanimations.ArrayAdapter;
import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Sermon {
	private static String TAG = "Sermon";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	private static SimpleExpandableListItemAdapter mAdapter;
	private static DisplayMetrics metrics;
	private static ArrayList<Calendar> adjusted_cals;
	
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
   	 	ListView list = (ListView)(mainView.findViewById(R.id.sermon_list));
   	 	try
   	 	{
   	 		List<FeedEntry> entries = feedEntryManager.getFeedEntries("30");
   	 		mAdapter = new SimpleExpandableListItemAdapter(mContext, entries);
			AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mAdapter);
			alphaInAnimationAdapter.setAbsListView(list);
			list.setAdapter(alphaInAnimationAdapter);
			
	    	if(adjusted_cals == null)
	    	{
	    		adjusted_cals = new ArrayList<Calendar>();
	    		for(int i=0; i < entries.size(); i++)
	    		{
	    			adjusted_cals.add(null);
	    		}
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
	        super(context, R.layout.activity_expandablelistitem_card,
	        		R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
	        mContext = context;         
	    }

	    @Override
	    public View getTitleView(final int position, View convertView, ViewGroup parent) {
	    	RelativeLayout rl = (RelativeLayout) convertView;
	        
		     if (rl == null) 
		     {
		         rl = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.sermon_content, null);
		     }

		     rl.setMinimumWidth(metrics.widthPixels);
		     TableLayout text_tl = (TableLayout)rl.findViewById(R.id.text_tl);
		     text_tl.setMinimumWidth(metrics.widthPixels/2);
		     final WebView wv = (WebView)rl.findViewById(R.id.sermon_webview);
//		         wv.setVerticalScrollBarEnabled(true);
//		         wv.setHorizontalScrollBarEnabled(true);
		     wv.setInitialScale(30);
		     WebSettings settings = wv.getSettings();
//		         settings.setPluginState(PluginState.ON);
//		         settings.setUseWideViewPort(true);
		     settings.setJavaScriptEnabled(true);
//		         settings.setSaveFormData(true);
//		         settings.setJavaScriptCanOpenWindowsAutomatically(true);
//		         settings.setLoadsImagesAutomatically(true);
		     wv.loadDataWithBaseURL(null, getItem(position).getContentDisplay(), "text/html", "UTF-8",null);
		     wv.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if(event.getAction() == MotionEvent.ACTION_UP)
							{
								((Activity)(mContext)).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).getContent())));
							}
							return true;
						}
			});
		    TextView title_tv = (TextView)rl.findViewById(R.id.sermon_textview);
		    title_tv.setText(getItem(position).getTitle());
		    title_tv.setTextColor(Color.BLACK);
		    title_tv.setMaxWidth(metrics.widthPixels/2);
		         
		    TextView date_tv = (TextView)rl.findViewById(R.id.date_textview);
		    if(adjusted_cals.get(position) == null)
		    	adjusted_cals.add(position,((MainActivity)mContext).getDateAdjusted(getItem(position).getDate()+""));
		    if(adjusted_cals.get(position) != null)
		    {
		    	 date_tv.setText(adjusted_cals.get(position).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)+" "+
		    			 adjusted_cals.get(position).get(Calendar.DAY_OF_MONTH)+ " " + 
		    			 adjusted_cals.get(position).get(Calendar.YEAR));
		    	 date_tv.setTextColor(Color.BLUE);
		    	 date_tv.setMaxWidth(metrics.widthPixels/2);
		    }
		    return rl;
	    }
	    
	    @Override
	    public View getContentView(final int position, View convertView, ViewGroup parent) {
	    	RelativeLayout rl = (RelativeLayout) convertView;
	        
		     if (rl == null) {
		         rl = new RelativeLayout(mContext);
		     }
		       
		    return rl;
	    }
	}
}
