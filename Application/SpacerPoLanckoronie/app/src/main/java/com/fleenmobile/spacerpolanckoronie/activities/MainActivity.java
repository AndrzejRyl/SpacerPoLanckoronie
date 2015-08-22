package com.fleenmobile.spacerpolanckoronie.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.adapters.MenuItemAdapter;
import com.fleenmobile.spacerpolanckoronie.adapters.MyMenuItem;
import com.fleenmobile.spacerpolanckoronie.fragments.AboutUsFragment;
import com.fleenmobile.spacerpolanckoronie.fragments.HistoryFragment;
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
public class MainActivity extends ActionBarActivity implements IFragmentCommunication{
    public static final String MENU_ITEM_CHOSEN = "item chosen";
    public static final String INTERESTING_PLACE_CHOSEN = "interesting place chosen";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private TextView mToolbarTitleTV;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Fragment[] fragments;
    private List<MyMenuItem> menuItems;

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
        for (int i=0; i < 5; i++) {
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
        selectItem(2);
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
    public void onMssgReceived(String mssg, String arg) {
        if (mssg.equals(MENU_ITEM_CHOSEN)) {
            // User has chosen an item and we have to set new fragment
            selectItem(Integer.parseInt(arg));
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
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
     * @param v Button 'navigation'
     */
    public void startNavSystem(View v) {
        ((NavFragment)fragments[3]).startNavSystem();
    }

}