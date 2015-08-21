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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;

/**
 * This is a main activity containing navigation drawer
 * and allowing user to switch between nav drawer items by
 * swapping fragments
 *
 * @author Fleen Mobile
 */
public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private TextView mToolbarTitleTV;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private String[] menuItemNames;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get strings:
         * all menu item names
         * initial menu item name (History)
         * toolbar title for opening nav drawer
        */
        menuItemNames = getResources().getStringArray(R.array.menu_item_names);
        mTitle = menuItemNames[2];
        mDrawerTitle = getResources().getString(R.string.menu_title);

        mDrawerList = (ListView) findViewById(R.id.nav_list);
        mToolbarTitleTV = (TextView) findViewById(R.id.toolbar_title);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_item, menuItemNames));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        initializeDrawerToggle();
        initializeFragments();

    }

    private void initializeFragments() {
        //TODO:

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
        setTitle(menuItemNames[2]);
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
                .replace(R.id.nav_top, fragments[position])
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = menuItemNames[position];
        setTitle(menuItemNames[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbarTitleTV.setText(title);
    }

}