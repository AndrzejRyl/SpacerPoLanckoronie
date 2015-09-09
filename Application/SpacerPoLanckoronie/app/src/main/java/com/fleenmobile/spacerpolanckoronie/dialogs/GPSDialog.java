package com.fleenmobile.spacerpolanckoronie.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;

/**
 * This dialog informs the user that he needs to turn on the GPS
 * and allows him to do it via settings
 *
 * @author FleenMobile at 2015-08-22
 */
public class GPSDialog extends DialogFragment {
    private static Activity creatorInstance;

    public static GPSDialog newInstance(Activity instance) {

        // Start a dialog
        GPSDialog dialog = new GPSDialog();

        // Get a holder to host activity
        creatorInstance = instance;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gps_dialog, null,
                false);

        // Make sure this dialog has no title (we've got a cutom one)
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Find textview and attach onclick listener to that
        TextView settings = (TextView) view.findViewById(R.id.gps_dialog_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
                dismiss();
            }
        });

        return view;
    }

    /**
     * Starts settings so as to allow user to turn GPS on
     */
    public void showSettings() {
        Utils.showSettings(this.getActivity());
    }
}
