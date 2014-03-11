package org.sdhanbit.mobile.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class News {
	private static String TAG = "News";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
	
	public News(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	public void constructNews()
    {
    	Log.v(TAG, "Starting News");
    	final ArrayList<String> list = new ArrayList<String>();
   	 	ListView news_list = (ListView)(mainView.findViewById(R.id.news_list));
   	 	List<FeedEntry> entries = feedEntryManager.getFeedEntries("15");
 		for(int i = 0 ; i < entries.size(); i++)
    	{
    		Log.v(TAG, entries.get(i).getTitle());
    		list.add(entries.get(i).getTitle());
    	}
   	    final StableArrayAdapter adapter = new StableArrayAdapter(mContext,
    	        android.R.layout.simple_list_item_1, list);
    	news_list.setAdapter(adapter);
    }
	
	class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	}
}
