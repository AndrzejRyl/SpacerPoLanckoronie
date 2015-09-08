package com.fleenmobile.spacerpolanckoronie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;

/**
 * @author FleenMobile at 2015-08-22
 */
public class NavFragment extends Fragment{

    private IFragmentCommunication mActivity;
    private TextView testTV;

    /**
     * Required empty constructor
     */
    public NavFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Get a holder to the host activity
        mActivity = (IFragmentCommunication) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav, container, false);

        return rootView;
    }

    /**
     * Starts navigation system with the destination set for Lanckorona
     */
    public void startNavSystem() {
        if (!Utils.GPSOn(this.getActivity())) {
            Utils.showGPSDialog(this);
            return;
        }

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=49.844951,19.715290"));
        startActivity(intent);
    }


}
