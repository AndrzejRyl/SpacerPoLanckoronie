package com.fleenmobile.spacerpolanckoronie.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSPoint;
import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSRange;
import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSService;
import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;

/**
 * This is an activity which shows a map with user's current location
 * and location of the place he's going to
 *
 * @author FleenMobile at 2015-09-09
 */
public class MapActivity extends ActionBarActivity {

    private static GPSService mService;

    private ImageView mark, map;
    private int mapWidth, mapHeight, markWidth, markHeight;
    private int marginLeft, marginTop;
    private GPSRange mapRange = new GPSRange(new GPSPoint(49.842297, 19.711905), new GPSPoint(49.851165, 19.719651));
    private final boolean[] onMap = {true};

    private BroadcastReceiver mMessageReceiver;

    private Thread GPSThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Find views
        map = (ImageView) findViewById(R.id.map);
        mark = (ImageView) findViewById(R.id.map_position);

        // Get size of map and mark
        getMarkSize();
        getMapSize();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Start MainActivity and set it to walk module
                Intent i = new Intent(MapActivity.this, MainActivity.class);
                i.putExtra(Utils.MAP_ACTIVITY, "");
                i.putExtra(Utils.INTERESTING_PLACE_BROADCAST, intent.getIntExtra(Utils.INTERESTING_PLACE_BROADCAST, 0));
                startActivity(i);

                finish();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Utils.INTERESTING_PLACE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        if (GPSThread != null)
            GPSThread.interrupt();
    }

    private void getMarkSize() {
        ViewTreeObserver vto = mark.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mark.getViewTreeObserver().removeOnPreDrawListener(this);
                markHeight = mark.getMeasuredHeight();
                markWidth = mark.getMeasuredWidth();

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

    private void startUpdatingGPS() {
        GPSThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    setLocation();

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
                    calculateMargins();

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

    private void calculateMargins() {
        Location location = mService.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double topographicWidth = mapRange.getRightTop().getLongitude() - mapRange.getLeftBottom().getLongitude();
        double topographicHeight = mapRange.getRightTop().getLatitude() - mapRange.getLeftBottom().getLatitude();

        double topoghrapicLongitudeDelta = longitude - mapRange.getLeftBottom().getLongitude();
        double topoghrapicLatitudeDelta = latitude - mapRange.getLeftBottom().getLatitude();

        double longitudePercentage = topoghrapicLongitudeDelta / topographicWidth;
        double latitudePercentage = topoghrapicLatitudeDelta / topographicHeight;

        marginLeft = (int) (longitudePercentage * mapWidth) - markWidth;
        marginTop = mapHeight - (int) (latitudePercentage * mapHeight) - markHeight;
    }


    public static void setService(GPSService service) {
        mService = service;
    }
}