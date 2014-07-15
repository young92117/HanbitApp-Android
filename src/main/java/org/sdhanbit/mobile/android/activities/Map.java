package org.sdhanbit.mobile.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;

public class Map extends Activity {

	private static String TAG = "Map";
	private Context mContext;
	private FeedEntryManager feedEntryManager;
	private View mainView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        
        setContentView(R.layout.map);

        GoogleMap mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setTrafficEnabled(true);
        UiSettings settings = mMap.getUiSettings();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(new LatLng(32.827608, -117.162542),
                        18f, 30f, 112.5f))); // zoom, tilt, bearing
        mMap.setTrafficEnabled(true);
        settings.setAllGesturesEnabled(true);
        settings.setCompassEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setRotateGesturesEnabled(true);
        settings.setScrollGesturesEnabled(true);
        settings.setTiltGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setZoomGesturesEnabled(true);

		ImageView imgView = (ImageView)findViewById(R.id.imageView);
    	imgView.setOnClickListener(new View.OnClickListener() {
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
				startActivity(Intent.createChooser(i,"Please choose map application"));
			}
		});
	}

}
