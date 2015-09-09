package com.fleenmobile.spacerpolanckoronie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.provider.Settings;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSPoint;
import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSRange;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.dialogs.GPSDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FleenMobile at 2015-08-22
 */
public class Utils {

    public static final String INTERESTING_PLACE_MODE = "interesting place mode";
    public static final String WALK_MODE = "walk mode";
    public static final String INTERESTING_PLACE_BROADCAST = "interesting place broadcast";
    public static final String MAP_ACTIVITY = "map activity";

    /**
     * Checks whether the GPS is turned on
     *
     * @return True if it's on and False otherwise
     */
    public static boolean GPSOn(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Shows a dialog asking the user to turn the GPS
     *
     * @param context Context in which the dialog is shown
     */
    public static void showGPSDialog(Context context) {
        GPSDialog dialog = GPSDialog.newInstance(((Activity) context));
        dialog.show(((Activity) context).getFragmentManager(), "GPSDialog");
    }

    /**
     * Shows the user settings page in order to turn on GPS
     *
     * @param context The context in which we will show this page
     */
    public static void showSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static List<InterestingPlace> getInterestingPlaces(Context context) {
        List<InterestingPlace> result = new ArrayList<>();
        Resources resources = context.getResources();
        // TODO: Change to real data
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place1_name),
                        resources.getString(R.string.place1_description),
                        R.drawable.place1,
                        R.raw.place1,
                        new GPSPoint(49.848560, 19.717997),
                        new GPSRange(new GPSPoint(49.848437, 19.717663), new GPSPoint(49.848727, 19.718104))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place2_name),
                        resources.getString(R.string.place2_description),
                        R.drawable.place2,
                        R.raw.place2,
                        new GPSPoint(49.848382, 19.718863),
                        new GPSRange(new GPSPoint(49.848311, 19.718031), new GPSPoint(49.848640, 19.718920))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place3_name),
                        resources.getString(R.string.place3_description),
                        R.drawable.place3,
                        R.raw.place3,
                        new GPSPoint(49.850221, 19.716639),
                        new GPSRange(new GPSPoint(49.850185, 19.716579), new GPSPoint(49.850561, 19.716938))));

        return result;
    }
}
