package org.sdhanbit.mobile.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.controllers.MainActivityController;
import org.sdhanbit.mobile.android.schedulers.RssReaderScheduler;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {

    @Inject
    private MainActivityController controller;
    @Inject
    private RssReaderScheduler mRssReaderScheduler;

//    @InjectView(R.id.textGreeting)
//    private TextView txtGreeting;
//    @InjectView(R.id.btnOk)
//    private Button btnOk;
//    @InjectView(R.id.btnNext)
//    private Button btnNext;
//    @InjectView(R.id.btnTop)
//    private Button btnTop;
//    @InjectResource(R.string.greeting)
//    private String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the activity title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        GridView gridView = (GridView) findViewById(R.id.main_grid);
        
        gridView.setAdapter(new GridViewContent(this));
        gridView.setOnItemClickListener(itemClickListener);

//        txtGreeting.setText(greeting);
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                controller.OnNextButtonClicked(view.getContext());
//            }
//        });
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                controller.OnOkButtonClicked(view.getContext());
//            }
//        });
//
//        btnTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                controller.OnTopButtonClicked(view.getContext());
//            }
//        });

        mRssReaderScheduler.setAlarm(this);
    }
    
    private OnItemClickListener itemClickListener = new OnItemClickListener() 
    {
    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
		{
    		controller.OnTopButtonClicked(view.getContext());
		}
    };

    @Override
    protected void onStop() {
        mRssReaderScheduler.cancelAlarm(this);
        super.onStop();
    }
}

class GridViewContent extends BaseAdapter {
	 
	private Context context;
	
	public int [] gv_fill = {
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
			R.drawable.jupiter,
	};
	
	public GridViewContent(Context c){
 		context = c;
 	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (gv_fill.length);
	}
 
	@Override
	public Object getItem(int position) {
		return gv_fill[position];
 
	}
 
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
 
		ImageView imageView = new ImageView(context);
        imageView.setImageResource(gv_fill[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(350, 200));
        return imageView;
    }
}