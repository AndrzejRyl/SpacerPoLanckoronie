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
    public static boolean soundOn = true;

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

        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place1_name),
                        resources.getString(R.string.place1_description),
                        R.drawable.place1,
                        R.raw.place1,
                        new GPSPoint(49.845319, 19.718539),
                        new GPSRange(new GPSPoint(49.845114, 19.718229), new GPSPoint(49.845595, 19.718951))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place2_name),
                        resources.getString(R.string.place2_description),
                        R.drawable.place2,
                        R.raw.place2,
                        new GPSPoint(49.850292, 19.716644),
                        new GPSRange(new GPSPoint(49.850111, 19.716409), new GPSPoint(49.850633, 19.717219))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place3_name),
                        resources.getString(R.string.place3_description),
                        R.drawable.place3,
                        R.raw.place3,
                        new GPSPoint(49.850400, 19.716147),
                        new GPSRange(new GPSPoint(49.850227, 19.715931), new GPSPoint(49.850601, 19.716259))));
        /*result.add(
                new InterestingPlace(
                        resources.getString(R.string.place4_name),
                        resources.getString(R.string.place4_description),
                        R.drawable.place4,
                        R.raw.place4,
                        new GPSPoint(49.848454, 19.718972),
                        new GPSRange(new GPSPoint(49.848183, 19.718020), new GPSPoint(49.848765, 19.719274))));
        */result.add(
                new InterestingPlace(
                        resources.getString(R.string.place5_name),
                        resources.getString(R.string.place5_description),
                        R.drawable.place5,
                        R.raw.place5,
                        new GPSPoint(49.848562, 19.717906),
                        new GPSRange(new GPSPoint(49.848386, 19.717400), new GPSPoint(49.848752, 19.718175))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place6_name),
                        resources.getString(R.string.place6_description),
                        R.drawable.place6,
                        R.raw.place6,
                        new GPSPoint(49.847889, 19.716184),
                        new GPSRange(new GPSPoint(49.847713, 19.715976), new GPSPoint(49.848287, 19.716517))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place7_name),
                        resources.getString(R.string.place7_description),
                        R.drawable.place7,
                        R.raw.place7,
                        new GPSPoint(49.847888, 19.712509),
                        new GPSRange(new GPSPoint(49.847489, 19.711997), new GPSPoint(49.848200, 19.713120))));

        return result;
    }
}
