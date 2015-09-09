package com.fleenmobile.spacerpolanckoronie.GPSUtils;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;

import java.util.List;

/**
 * This service checks user's GPS position and
 * displays info about a interesting place when the user is
 * in it's range
 *
 * @author FleenMobile at 2015-09-09
 */
public class GPSService extends Service {

    private LocationManager lm;
    private LocationListener locationListener;
    private Location mLocation = null;

    private MyBinder myBinder = new MyBinder();

    private List<InterestingPlace> interestingPlaces;
    private InterestingPlace interestingPlaceInRange = null;
    private List<Fragment> fragments;
    private InterestingPlacesThread interestingPlacesThread;

    private boolean walkStarted = false;


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize GPS updates system
        if (lm == null) {
            lm = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    // This allows activities to bind to this service and call it's methods
    public class MyBinder extends Binder {
        public GPSService getService() {
            return GPSService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        walkStarted = false;
        // Try not to kill this service and make sure that it has MainActivity alive
        // when it's restarted
        return START_REDELIVER_INTENT;
    }

    public void setInterestingPlaces(List<InterestingPlace> interestingPlaces) {
        this.interestingPlaces = interestingPlaces;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    // Start a thread checking the user's location and sending a broadcast
    // if he's in range of any interesting place
    public void startThread() {
        interestingPlacesThread = new InterestingPlacesThread();
        interestingPlacesThread.start();
    }

    public void stopThread() {
        if (interestingPlacesThread != null)
            interestingPlacesThread.interrupt();
    }

    // This thread checks user's location every 2s and
    // sends a broadcast if the user is in range of any
    // interesting place
    private class InterestingPlacesThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    if (!walkStarted) {
                        // Wait for the user to get into the range of first place
                        // because we're starting the walk
                        InterestingPlace place = interestingPlaces.get(0);
                        if (place != interestingPlaceInRange && mLocation != null && place.getRange().inRange(mLocation)) {
                            sendMyBroadcast(place);
                            interestingPlaceInRange = place;
                            walkStarted = true;
                        }
                    } else {

                        // Check if the user is in range of any interesting place
                        // but don't double broadcasts
                        for (InterestingPlace place : interestingPlaces) {
                            if (place != interestingPlaceInRange && mLocation != null && place.getRange().inRange(mLocation)) {
                                sendMyBroadcast(place);
                                interestingPlaceInRange = place;
                            }
                        }
                    }

                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    // Sends a broadcast informing activities that user has
    // just gone into the range of the given place
    private void sendMyBroadcast(InterestingPlace place) {
        Log.e("Zonk", "sending");
        Intent intent = new Intent(Utils.INTERESTING_PLACE_BROADCAST);

        // Add data
        intent.putExtra("idx", interestingPlaces.indexOf(place));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}