package com.fleenmobile.spacerpolanckoronie.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSRange;
import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSService;
import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;

/**
 * This is an activity which shows a map with user's current location
 * and location of the place he's going to
 *
 * @author FleenMobile at 2015-09-09
 */
public class MapActivity extends ActionBarActivity {

    @NonNull
    private static GPSService mService;
    @NonNull
    private static InterestingPlace mDestination;

    private static final int DESTINATION_MARK = 0;
    private static final int USER_MARK = 1;

    private ImageView mark, destinationMark, map;
    private int mapWidth, mapHeight, markWidth, markHeight;
    private int marginLeft, marginTop, destinationMarginLeft, destinationMarginTop;
    private GPSRange mapRange = Utils.getMapRange();
    private final boolean[] onMap = {true};

    private BroadcastReceiver mMessageReceiver;

    private Thread GPSThread;

    private boolean beginningFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Find views
        map = (ImageView) findViewById(R.id.map);
        mark = (ImageView) findViewById(R.id.map_position);
        destinationMark = (ImageView) findViewById(R.id.map_destination_position);

        // Get size of map and mark
        getMarkSize();
        getMapSize();

        // Check if this a beginning of a walk
        if (getIntent().getStringExtra(Utils.BEGINNING_FLAG) != null)
            beginningFlag = true;

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // If this is the beginning of a walk MainActivity is started
                // only when the user reaches first interesting place
                if (beginningFlag && intent.getIntExtra(Utils.INTERESTING_PLACE_BROADCAST, 0) != 0)
                    return;

                // There is a problem with small proximity between Tadeusz Villa and the Quarry.
                // Whenever the user just visited the quarry and is going to Castle Villa
                // we have to react only on going in range of Castle Villa (so as not to provoke
                // going in circle between Tadeusz Villa and the Quarry)
                // TODO: Make sure that Castle Villa is in fact fourth in the order
                if (mDestination.getName().equals(MapActivity.this.getResources().getString(R.string.place5_name))
                    && intent.getIntExtra(Utils.INTERESTING_PLACE_BROADCAST, 0) != 3)
                    return;


                // Start MainActivity and set it to walk module
                finish();

                Intent i = new Intent(MapActivity.this, MainActivity.class);
                i.putExtra(Utils.MAP_ACTIVITY, "");
                i.putExtra(Utils.INTERESTING_PLACE_BROADCAST, intent.getIntExtra(Utils.INTERESTING_PLACE_BROADCAST, 0));
                startActivity(i);

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mService != null)
            mService.setAppPaused(false);

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Utils.INTERESTING_PLACE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mService != null)
            mService.setAppPaused(true);

        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        if (GPSThread != null)
            GPSThread.interrupt();
    }

    private void getMarkSize() {
        ViewTreeObserver vto = destinationMark.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                destinationMark.getViewTreeObserver().removeOnPreDrawListener(this);
                markHeight = destinationMark.getMeasuredHeight();
                markWidth = destinationMark.getMeasuredWidth();

                return true;
            }
        });
    }

    private void getMapSize() {
        ViewTreeObserver vto = map.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                map.getViewTreeObserver().removeOnPreDrawListener(this);
                mapHeight = map.getMeasuredHeight();
                mapWidth = map.getMeasuredWidth();

                startUpdatingGPS();
                return true;
            }
        });
    }

    // Sets a mark for destination on the map
    private void setDestinationMark() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set location mark visible
                destinationMark.setVisibility(View.VISIBLE);

                // Calculate margins on a basis of size of a map, size of a mark and GPS mark
                calculateMargins(DESTINATION_MARK);

                // Set location mark on a map
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(destinationMarginLeft, destinationMarginTop, 0, 0);
                destinationMark.setLayoutParams(params);
            }
        });
    }

    private void startUpdatingGPS() {
        GPSThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    setLocation();
                    setDestinationMark();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        GPSThread.start();
    }

    private void setLocation() {
        // Show me the size of a map
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // First check if a user is in Lanckorona ;)
                if (mapRange.inRange(mService.getLocation())) {
                    onMap[0] = true;

                    // Set location mark visible
                    mark.setVisibility(View.VISIBLE);

                    // Calculate margins on a basis of size of a map, size of a mark and GPS mark
                    calculateMargins(USER_MARK);

                    // Set location mark on a map
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(marginLeft, marginTop, 0, 0);
                    mark.setLayoutParams(params);
                } else {
                    if (onMap[0]) {
                        // Show this mssg only once
                        mark.setVisibility(View.INVISIBLE);
                        Toast.makeText(MapActivity.this, getResources().getString(R.string.out_of_map), Toast.LENGTH_LONG).show();
                        onMap[0] = false;
                    }

                }
            }

        });


    }

    private void calculateMargins(int mark) {
        // Get longitude, latitude of user or his destination
        double latitude, longitude;
        if (mark == DESTINATION_MARK) {
            latitude = mDestination.getCoordinates().getLatitude();
            longitude = mDestination.getCoordinates().getLongitude();
        } else {
            latitude = mService.getLocation().getLatitude();
            longitude = mService.getLocation().getLongitude();
        }

        double topographicWidth = mapRange.getRightTop().getLongitude() - mapRange.getLeftBottom().getLongitude();
        double topographicHeight = mapRange.getRightTop().getLatitude() - mapRange.getLeftBottom().getLatitude();

        double topoghrapicLongitudeDelta = longitude - mapRange.getLeftBottom().getLongitude();
        double topoghrapicLatitudeDelta = latitude - mapRange.getLeftBottom().getLatitude();

        double longitudePercentage = topoghrapicLongitudeDelta / topographicWidth;
        double latitudePercentage = topoghrapicLatitudeDelta / topographicHeight;

        if (mark == DESTINATION_MARK) {
            destinationMarginLeft = (int) (longitudePercentage * mapWidth) - markWidth/2;
            destinationMarginTop = mapHeight - (int) (latitudePercentage * mapHeight) - markHeight;
        } else {
            marginLeft = (int) (longitudePercentage * mapWidth) - markWidth/2;
            marginTop = mapHeight - (int) (latitudePercentage * mapHeight) - markHeight;
        }
    }


    public static void setService(GPSService service) {
        mService = service;
    }

    public static void setDestination(InterestingPlace destination) {
        mDestination = destination;
    }
}