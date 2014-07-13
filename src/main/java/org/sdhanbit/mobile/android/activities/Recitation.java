package org.sdhanbit.mobile.android.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
//import roboguice.activity.RoboActivity;

//import android.app.PendingIntent.getActivity;

//import static android.app.PendingIntent.getActivity;
//import static android.support.v4.app.ActivityCompat.startActivity;

public class Recitation extends Activity {

	private static String TAG = "Recitation";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;
    private PackageManager pm;

    public Recitation(){}

	public Recitation(Context context, FeedEntryManager feedEntryManager, View mainView)
	{
		mContext = context;
		this.feedEntryManager = feedEntryManager;
		this.mainView = mainView;
	}
	
	public void construct()
    {
		
		//Right now, it doesn't reach here.
    	Log.v(TAG, "Starting Recitation");
    }




}
