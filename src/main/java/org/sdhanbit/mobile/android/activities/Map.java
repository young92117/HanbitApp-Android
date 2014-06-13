package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Map {

	private static String TAG = "Map";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	public Map(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
    	Log.v(TAG, "Starting Map");
    	ImageView map_iv = (ImageView)(mainView.findViewById(R.id.compass_imageview));
    	map_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.CATEGORY_BROWSABLE);
				i.setAction("android.intent.action.VIEW");
				i.addCategory("android.intent.category.BROWSABLE");
				Uri uri = Uri
						.parse("http://maps.google.com/maps?f=d&utm_campaign=en&utm_medium=ha&utm_source=en-ha-na-us-bk-dd&utm_term=google%20direction"
								+ "&daddr="
								+ "32.827608"
								+ ","
								+ "-117.162542"
								+ "&saddr=");
				i.setData(uri);
				mContext.startActivity(Intent.createChooser(i,"Please choose map application"));
			}
		});
    }
}
