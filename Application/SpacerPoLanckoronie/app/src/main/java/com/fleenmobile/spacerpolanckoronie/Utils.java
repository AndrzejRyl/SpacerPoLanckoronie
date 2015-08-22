package com.fleenmobile.spacerpolanckoronie;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.fleenmobile.spacerpolanckoronie.dialogs.GPSDialog;

/**
 * @author FleenMobile at 2015-08-22
 */
public class Utils {

    /**
     * Checks whether the GPS is turned on
     * @return True if it's on and False otherwise
     */
    public static boolean GPSOn(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
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
}
