package com.fleenmobile.spacerpolanckoronie;

import android.app.Fragment;
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

    /**
     * Checks whether the GPS is turned on
     * @return True if it's on and False otherwise
     */
    public static boolean GPSOn(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Shows a dialog asking the user to turn the GPS
     * @param context Context in which the dialog is shown
     */
    public static void showGPSDialog(Fragment context) {
        GPSDialog dialog = GPSDialog.newInstance(context);
        dialog.show(context.getFragmentManager(), "GPSDialog");
    }

    /**
     * Shows the user settings page in order to turn on GPS
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
                        new GPSPoint(0.00, 0.00),
                        new GPSRange(new GPSPoint(0.00,0.00), new GPSPoint(0.00,0.00))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place2_name),
                        resources.getString(R.string.place2_description),
                        R.drawable.place2,
                        R.raw.place2,
                        new GPSPoint(0.00, 0.00),
                        new GPSRange(new GPSPoint(0.00,0.00), new GPSPoint(0.00,0.00))));
        result.add(
                new InterestingPlace(
                        resources.getString(R.string.place3_name),
                        resources.getString(R.string.place3_description),
                        R.drawable.place3,
                        R.raw.place3,
                        new GPSPoint(0.00, 0.00),
                        new GPSRange(new GPSPoint(0.00,0.00), new GPSPoint(0.00,0.00))));

        return result;
    }
}
