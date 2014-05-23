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

import static android.app.PendingIntent.getActivity;

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
    	Log.v(TAG, "Starting Recitation");

        //Uri BA = Uri.parse("org.sdhanbit.android");
        //Intent intent = new Intent(Intent.ACTION_VIEW,BA);
        //Intent intent = new Intent("org.sdhanbit.android");

        //Intent intent = new Intent(Intent.ACTION_MAIN);
        //intent.setClassName("org.sdhanbit.android","org.sdhanbit.android.ActivityToStart");
        //startActivity(intent);

        pm = mContext.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("org.sdhanbit.android");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);

        ///*
        if (intent != null)
        {
          // Activity was found, launch new app
          //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        }
        else
        {
          // Activity not found. Send user to market
          //intent = new Intent(Intent.ACTION_VIEW);
          //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.setData(Uri.parse("market://details?id=+org.sdhanbit.android"));
          startActivity(intent);
        }
        //*/

    }




}
