package com.fleenmobile.spacerpolanckoronie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;

/**
 * @author FleenMobile at 2015-08-22
 */
public class AboutUsFragment extends Fragment{

    private IFragmentCommunication mActivity;
    private TextView testTV;

    /**
     * Required empty constructor
     */
    public AboutUsFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        // Find views
        testTV = (TextView)rootView.findViewById(R.id.about_us_test);
        testTV.setText("About us");

        return rootView;
    }



}
