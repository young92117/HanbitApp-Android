package org.sdhanbit.mobile.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.google.inject.Inject;
import org.sdhanbit.mobile.android.R;
import org.sdhanbit.mobile.android.managers.FeedEntryManager;
import org.sdhanbit.mobile.android.schedulers.JsonReaderScheduler;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends RoboActivity {

//    @Inject
//    private MainActivityController controller;
//    @Inject
//    private RssReaderScheduler mRssReaderScheduler;
	@Inject
    private JsonReaderScheduler mJsonReaderScheduler;
	
	public static FeedEntryManager feedEntryManager;

    @InjectView(R.id.top)
    private DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer)
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    @InjectResource(R.array.menu_array)
    private String[] mMenuTitles;
    public static Context mContext;
    public static String TAG = "MainActivity";
    
    private static boolean isFrontMenu = false;
    
    public Calendar getDateAdjusted(String dateStr)
    {
    	Calendar cal = null;
    	try
    	{
	    	 SimpleDateFormat db_date_fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		     Date date = db_date_fmt.parse(dateStr);
		     cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"), Locale.US);
		     cal.setTime(date);
		     for(int i = 0; i < 7 && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY; i++)
		     {
		    	 cal.setTimeInMillis((cal.getTimeInMillis() - 86400000));
		     }
    	}catch(java.text.ParseException e)
	    {
	    }
	    return cal;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerList != null && !isFrontMenu) {
        	
           selectItem(0);
           isFrontMenu = true;
           return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public class CustomAdapter extends BaseAdapter {
        private Context context;

        public CustomAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return mMenuTitles.length;
        }

        @Override
        public Object getItem(int position) {    
            return mMenuTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.text1);
            int res;
            switch(position)
            {
            case 0:
            	res = R.drawable.cross_32x32;
            	break;
            case 1:
                res = R.drawable.main_sdhanbit_icon;
                   break;
            case 2:
            	res = R.drawable.main_pastor_icon;
            	break;
            case 3:
            	res = R.drawable.main_staff_icon;
            	break;
            case 4:
            	res = R.drawable.main_map_icon;
            	break;
            case 5:
            	res = R.drawable.main_worship_icon;
            	break;
            case 6:
            	res = R.drawable.main_sermon_icon;
            	break;
            case 7:
            	res = R.drawable.main_share_icon;
            	break;
            case 8:
            	res = R.drawable.main_recitation_icon;
            	break;
            case 9:
            	res = R.drawable.main_ministry_icon;
            	break;
            case 10:
            	res = R.drawable.main_words_icon;
            	break;
            case 11:
            	res = R.drawable.main_news_icon;
            	break;
            case 12:
            	res = R.drawable.main_actions_icon;
            	break;
            case 13:
            	res = R.drawable.main_sdhanbit_icon;
            	break;
            default:
            	res = R.drawable.main_sdhanbit_icon;
            }
            imgIcon.setImageResource(res);     
            txtTitle.setText(mMenuTitles[position]);     
            return convertView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mContext = this;
        
        if(feedEntryManager == null)
        {
        	feedEntryManager = new FeedEntryManager();
        	feedEntryManager.setContext(this);
        }
        mJsonReaderScheduler.receiveData();
        
        mTitle = mDrawerTitle = getTitle();

      // set a custom shadow that overlays the main content when the drawer opens
//      mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

      // set up the drawer's list view with items and click listener
      mDrawerList.setAdapter(new CustomAdapter(this));
      mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

      //mDrawerList.setBackground(getResources().getDrawable(R.drawable.drawerbackground));
      mDrawerList.setDividerHeight(1);

      // enable ActionBar app icon to behave as action to toggle nav drawer
      getActionBar().setDisplayHomeAsUpEnabled(true);
      getActionBar().setHomeButtonEnabled(true);
//      getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0cdec"))); // Hanbit Light Blue
//      getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d3b85"))); // Hanbit dark blue
      getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5f5f67"))); // dark gray

      // ActionBarDrawerToggle ties together the the proper interactions
      // between the sliding drawer and the action bar app icon
      mDrawerToggle = new ActionBarDrawerToggle(
              this,                  /* host Activity */
              mDrawerLayout,         /* DrawerLayout object */
              R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
              R.string.drawer_open,  /* "open drawer" description for accessibility */
              R.string.drawer_close  /* "close drawer" description for accessibility */
      ) {
          public void onDrawerClosed(View view) {
              getActionBar().setTitle(mTitle);
              invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
          }

          public void onDrawerOpened(View drawerView) {
//              getActionBar().setTitle(mDrawerTitle);  메뉴를 띄웠을 때 현재 상태를 유지
              invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
          }
      };
      mDrawerLayout.setDrawerListener(mDrawerToggle);

      if (savedInstanceState == null) {
          selectItem(0);
      }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
//            case R.id.action_websearch:
//                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_MENU_NUMBER = "menu_number";

        public static int tmap[] = {4,/*map*/  11, /* news */  12, /*Actions*/
        						    13, /*School*/ 9, /*Ministry*/ 8, /*Recitation*/
        						    6,/*Sermon*/ 7, /*Share*/ 10/*words*/}; //by jin

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_MENU_NUMBER);
            View rootView = null;
            String menu;
            int imageId;
            isFrontMenu = false;

            switch(i)
            {
            case 0: //Main front page
            	isFrontMenu = true;
            	rootView = inflater.inflate(R.layout.front_page, container, false);
                GridView gridView = (GridView)(rootView.findViewById(R.id.main_grid));
                gridView.setAdapter(new GridViewContent(mContext));
                gridView.setOnItemClickListener(gridItemClickListener);

            	menu = getResources().getStringArray(R.array.menu_array)[i];
	            getActivity().setTitle(menu);
	            break;
            case 1: //overview
                    rootView = inflater.inflate(R.layout.overview, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Pastor(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 2: //pastor
                    rootView = inflater.inflate(R.layout.pastor, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Pastor(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 3: //people
                    //rootView = inflater.inflate(R.layout.people, container, false);
                    //menu = getResources().getStringArray(R.array.menu_array)[i];
                    //new People(mContext, feedEntryManager, rootView).construct();
                    //getActivity().setTitle(menu);
                    rootView = inflater.inflate(R.layout.people, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new People(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 4: //map
            		{
		            	Intent mapI = new Intent();
		                mapI.setClass(mContext, Map.class);
		                startActivity(mapI);
		                
		                isFrontMenu = true;
		            	rootView = inflater.inflate(R.layout.front_page, container, false);
		                GridView grid = (GridView)(rootView.findViewById(R.id.main_grid));
		                grid.setAdapter(new GridViewContent(mContext));
		                grid.setOnItemClickListener(gridItemClickListener);

		            	menu = getResources().getStringArray(R.array.menu_array)[0];
			            getActivity().setTitle(menu);
            		}
                    break;
            case 5: //worship
                    rootView = inflater.inflate(R.layout.worship, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Worship(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 6: //Sermon
                    rootView = inflater.inflate(R.layout.sermon, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Sermon(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 7: //Share
                    rootView = inflater.inflate(R.layout.share, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Share(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 8: //Recitation
                    {
                    	PackageManager pm = mContext.getPackageManager();
                        Intent intent = pm.getLaunchIntentForPackage("org.sdhanbit.android");
                        if (intent != null)
                        {
                         	intent.addCategory(Intent.CATEGORY_LAUNCHER);
                         	mContext.startActivity(intent);
                        }
                        else
                        {
                         	String appPackageName = "org.sdhanbit.android";
                         	try {
                         	    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                         	} catch (android.content.ActivityNotFoundException anfe) {
                         	    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                         	}
                        }
                        
		                isFrontMenu = true;
		            	rootView = inflater.inflate(R.layout.front_page, container, false);
		                GridView grid = (GridView)(rootView.findViewById(R.id.main_grid));
		                grid.setAdapter(new GridViewContent(mContext));
		                grid.setOnItemClickListener(gridItemClickListener);

		            	menu = getResources().getStringArray(R.array.menu_array)[0];
			            getActivity().setTitle(menu);
                    }
//                    rootView = inflater.inflate(R.layout.recitation, container, false);
//                    menu = getResources().getStringArray(R.array.menu_array)[i];
//                    new Recitation(mContext, feedEntryManager, rootView).construct();
//                    getActivity().setTitle(menu);
                    break;
            case 9: //Ministry
                    rootView = inflater.inflate(R.layout.ministry, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Ministry(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 10: //Words
                    rootView = inflater.inflate(R.layout.words, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Words(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 11:  // News
                    rootView = inflater.inflate(R.layout.news, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new News(mContext, feedEntryManager, rootView).constructNews();
                    getActivity().setTitle(menu);
                    break;
            case 12: //Actions
                    rootView = inflater.inflate(R.layout.actions, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new Actions(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            case 13: //School
                    rootView = inflater.inflate(R.layout.school, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
                    new School(mContext, feedEntryManager, rootView).construct();
                    getActivity().setTitle(menu);
                    break;
            default:
            		rootView = inflater.inflate(R.layout.fragment_planet, container, false);
                    menu = getResources().getStringArray(R.array.menu_array)[i];
    	            imageId = getResources().getIdentifier(menu.toLowerCase(Locale.getDefault()),
    	                    "drawable", getActivity().getPackageName());
    	            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
    	            getActivity().setTitle(menu);
            }

            return rootView;
        }
        private OnItemClickListener gridItemClickListener = new OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
    		{
        		//((MainActivity)(MainActivity.mContext)).selectItem(position+1);
                ((MainActivity)(MainActivity.mContext)).selectItem(tmap[position]); //by jin
    		}
        };
    }
    
    @Override
    protected void onStop() {
//        mRssReaderScheduler.cancelAlarm(this);
        mJsonReaderScheduler.cancelAlarm(this);
        super.onStop();
    }
}

class GridViewContent extends BaseAdapter {
	 
	private Context context;
	
	public int [] gv_fill = {
			R.drawable.main_sdhanbit,
			R.drawable.main_news,
			R.drawable.main_actions,
			R.drawable.main_school,   // 소망의씨앗 --> 문화학교
			R.drawable.main_ministry,
			R.drawable.main_recitation,
			R.drawable.main_sermon,
			R.drawable.main_share,
			R.drawable.main_words,
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int width = arg2.getWidth();
        int height = arg2.getBottom();
        imageView.setLayoutParams(new GridView.LayoutParams(width/3-1, height/3-1));

        return imageView;
    }
	
	
}