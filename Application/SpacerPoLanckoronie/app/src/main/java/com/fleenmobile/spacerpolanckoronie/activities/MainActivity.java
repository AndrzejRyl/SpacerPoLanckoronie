package com.fleenmobile.spacerpolanckoronie.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSService;
import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.adapters.MenuItemAdapter;
import com.fleenmobile.spacerpolanckoronie.adapters.MyMenuItem;
import com.fleenmobile.spacerpolanckoronie.fragments.AboutUsFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.HistoryFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.InterestingPlaceFlyweightFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.InterestingPlacesFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.NavFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.WalkFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a main activity containing navigation drawer
 * and allowing user to switch between nav drawer items by
 * swapping fragments
 *
 * @author Fleen Mobile
 */
public class MainActivity extends ActionBarActivity implements IFragmentCommunication {
    public static final String MENU_ITEM_CHOSEN = "item chosen";
    public static final String INTERESTING_PLACE_CHOSEN = "interesting place chosen";
    public static final String GO_BACK_INTERESTING_PLACES = "go back";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private TextView mToolbarTitleTV;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Fragment[] fragments;
    private List<MyMenuItem> menuItems;

    private BroadcastReceiver mMessageReceiver;
    private List<InterestingPlaceFlyweightFragment> interestingPlaceFragments;

    private GPSService gpsService;
    private ServiceConnection connection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeMenuItems();

        /* Get strings:
         * all menu item names
         * initial menu item name (History)
         * toolbar title for opening nav drawer
        */
        mTitle = menuItems.get(2).getName();
        mDrawerTitle = getResources().getString(R.string.menu_title);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbarTitleTV = (TextView) findViewById(R.id.toolbar_title);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new MenuItemAdapter(this,
                R.layout.menu_list_item, menuItems));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        initializeDrawerToggle();
        initializeFragments();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                int idx = intent.getIntExtra("idx", 0);

                // Tell walk fragment which interesting place it should set
                ((WalkFragment) fragments[0]).setFragment(idx);
            }
        };

        // Start GPSService
        setupConnection();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Utils.INTERESTING_PLACE_BROADCAST));

        // Bind to GPS service
        if (Utils.GPSOn(this)) {
            Intent i = new Intent(this, GPSService.class);
            bindService(i, connection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        unbindService(connection);
        if (gpsService != null)
            gpsService.stopThread();


        super.onPause();
    }

    private void initializeMenuItems() {
        String[] menuItemNames = getResources().getStringArray(R.array.menu_item_names);
        int[] menuItemIcons = {
                R.drawable.ic_directions_walk_black_24dp,
                R.drawable.ic_favorite_black_24dp,
                R.drawable.ic_schedule_black_24dp,
                R.drawable.ic_navigation_black_24dp,
                R.drawable.ic_info_black_24dp
        };

        menuItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            menuItems.add(new MyMenuItem(menuItemNames[i], menuItemIcons[i]));
        }

    }

    private void initializeFragments() {
        fragments = new Fragment[]{
                new WalkFragment(),
                new InterestingPlacesFragment(),
                new HistoryFragment(),
                new NavFragment(),
                new AboutUsFragment()
        };

        // Set initial fragment (history)
        selectMenuItem(2);
    }

    private void initializeDrawerToggle() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mTitle);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set initial menu item name (History)
        setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // This to ensure the navigation icon is displayed as
        // burger instead of arrow.
        // Call syncState() from your Activity's onPostCreate
        // to synchronize the indicator with the state of the
        // linked DrawerLayout after onRestoreInstanceState
        // has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // This method should always be called by your Activity's
        // onConfigurationChanged method.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        // Do nothing ;)
    }

    @Override
    public void onMssgReceived(String mssg, String arg) {
        if (mssg.equals(MENU_ITEM_CHOSEN)) {
            // User has chosen an item and we have to set new fragment
            selectMenuItem(Integer.parseInt(arg));
        } else if (mssg.equals(INTERESTING_PLACE_CHOSEN)) {
            // User has chosen an interesting place and we have to set new flyweight fragment
            ((InterestingPlacesFragment) fragments[1]).selectItem(Integer.parseInt(arg));
        } else if (mssg.equals(GO_BACK_INTERESTING_PLACES)) {
            // Go back from flyweight fragment to interesting places fragment
            selectMenuItem(1);
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectMenuItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectMenuItem(int position) {
        // Use GPSThread which sends broadcast about being in range of interesting
        // places only when we're in WalkFragment
        if (position == 0 && gpsService != null)
            gpsService.startThread();
        else if (gpsService != null)
            gpsService.stopThread();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragments[position])
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = menuItems.get(position).getName();
        setTitle(mTitle);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbarTitleTV.setText(title);
    }

    /**
     * Starts navigation system with the destination set for Lanckorona
     *
     * @param v Button 'navigation'
     */
    public void startNavSystem(View v) {
        ((NavFragment) fragments[3]).startNavSystem();
    }

    /**
     * Start navigation system with the destination set for
     * the beginning of the walk
     *
     * @param v Button 'navigation'
     */
    public void navigateToStart(View v) {
        ((WalkFragment) fragments[0]).navigateToStart();
    }

    private void setupConnection() {
        // If there is no GPS, prompt user for turning it on
        if (!Utils.GPSOn(this)) {
            Utils.showGPSDialog(this);
            return;
        }

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GPSService.MyBinder serviceBinder = (GPSService.MyBinder) service;
                gpsService = serviceBinder.getService();

                // Set data and start thread sending broadcasts about
                // going in range of any interesting places
                List<Fragment> flyweightFragmets = ((WalkFragment) fragments[0]).getFragments();
                List<InterestingPlace> interestingPlaces = Utils.getInterestingPlaces(MainActivity.this);
                gpsService.setFragments(flyweightFragmets);
                gpsService.setInterestingPlaces(interestingPlaces);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                gpsService = null;
            }
        };

    }


}