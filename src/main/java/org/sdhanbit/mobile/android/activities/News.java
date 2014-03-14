package org.sdhanbit.mobile.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
   	 	ListView news_list = (ListView)(mainView.findViewById(R.id.news_list));
   	 	List<FeedEntry> entries = feedEntryManager.getFeedEntries("15");
   	    final FeedArrayAdapter adapter = new FeedArrayAdapter(mContext,
    	        android.R.layout.simple_list_item_1, entries);
    	news_list.setAdapter(adapter);
    	news_list.setOnItemClickListener(new FeedItemClickListener());
    }
	
	class FeedItemClickListener implements AdapterView.OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
//			View row_view = parent.getAdapter().getView(position, view, parent);
//			((TextView)(row_view.findViewById(R.id.content)))
//			    .setText(((FeedEntry)(parent.getAdapter().getItem(position))).getContent());
			
			final Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.news_content);
			dialog.setTitle(((FeedEntry)(parent.getAdapter().getItem(position))).getTitle());
 
			TextView text = (TextView) dialog.findViewById(R.id.content);
			text.setText(((FeedEntry)(parent.getAdapter().getItem(position))).getContent());
 
//			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//			// if button is clicked, close the custom dialog
//			dialogButton.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
 
			dialog.show();
		}
	}
	
	class FeedArrayAdapter extends ArrayAdapter<FeedEntry> {

	    HashMap<FeedEntry, Integer> mIdMap = new HashMap<FeedEntry, Integer>();

	    public FeedArrayAdapter(Context context, int textViewResourceId,
	        List<FeedEntry> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }
	    
	    public View getView(int position, View convertView,	ViewGroup parent) 
	    {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View row=inflater.inflate(R.layout.news_row, parent, false);
			TextView label=(TextView)row.findViewById(R.id.content);
			label.setText((CharSequence)(getItem(position)).getContent().subSequence(0, 20)+"...");
			TextView date=(TextView)row.findViewById(R.id.date);
			date.setText((CharSequence)(getItem(position)).getDate());
			return(row);
	    }

	    @Override
	    public long getItemId(int position) {
	      FeedEntry item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	}
}
