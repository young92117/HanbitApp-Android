package org.sdhanbit.mobile.android.activities;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    
    @InjectView(R.id.top)
    private DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer)
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    @InjectResource(R.array.planets_array)
    private String[] mPlanetTitles;
    private static Context context;

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

        setContentView(R.layout.main);
        context = this;
        
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

        mTitle = mDrawerTitle = getTitle();
//      mPlanetTitles = getResources().getStringArray(R.array.planets_array);
//      mDrawerLayout = (DrawerLayout) findViewById(R.id.top);
//      mDrawerList = (ListView) findViewById(R.id.left_drawer);

      // set a custom shadow that overlays the main content when the drawer opens
      mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
      // set up the drawer's list view with items and click listener
      mDrawerList.setAdapter(new ArrayAdapter<String>(this,
              R.layout.drawer_list_item, mPlanetTitles));
      mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

      // enable ActionBar app icon to behave as action to toggle nav drawer
      getActionBar().setDisplayHomeAsUpEnabled(true);
      getActionBar().setHomeButtonEnabled(true);

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
              getActionBar().setTitle(mDrawerTitle);
              invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
          }
      };
      mDrawerLayout.setDrawerListener(mDrawerToggle);

      if (savedInstanceState == null) {
          selectItem(0);
      }

      mRssReaderScheduler.setAlarm(this);
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
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
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

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            View rootView;
            if(i == 0)
            {
            	rootView = inflater.inflate(R.layout.front_page, container, false);
                GridView gridView = (GridView)(rootView.findViewById(R.id.main_grid));
                gridView.setAdapter(new GridViewContent(context));
                gridView.setOnItemClickListener(itemClickListener);

            	String planet = getResources().getStringArray(R.array.planets_array)[i];
	            getActivity().setTitle(planet);
            }
            else
            {
            	rootView = inflater.inflate(R.layout.fragment_planet, container, false);
                String planet = getResources().getStringArray(R.array.planets_array)[i];
	            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
	                    "drawable", getActivity().getPackageName());
	            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
	            getActivity().setTitle(planet);
            }
            return rootView;
        }
        private OnItemClickListener itemClickListener = new OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
    		{
//        		controller.OnTopButtonClicked(view.getContext());
    		}
        };
    }
    
    @Override
    protected void onStop() {
        mRssReaderScheduler.cancelAlarm(this);
        super.onStop();
    }
}

class GridViewContent extends BaseAdapter {
	 
	private Context context;
	
	public int [] gv_fill = {
			R.drawable.bg_main_actions,
			R.drawable.bg_main_hopes,
			R.drawable.bg_main_ministry,
			R.drawable.bg_main_news,
			R.drawable.bg_main_recitation,
			R.drawable.bg_main_sdhanbit,
			R.drawable.bg_main_sermon,
			R.drawable.bg_main_share,
			R.drawable.bg_main_words,
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