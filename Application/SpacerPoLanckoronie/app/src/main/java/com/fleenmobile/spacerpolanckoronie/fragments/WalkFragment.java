package com.fleenmobile.spacerpolanckoronie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is responsible for the most important module in this app
 * which is a walk throughout Lanckorona. It uses GPS to track user's
 * position and supplies information about interesting places
 * that the user visits.
 *
 * @author FleenMobile at 2015-08-22
 */
public class WalkFragment extends Fragment {

    private IFragmentCommunication mActivity;

    private List<InterestingPlace> interestingPlaces;
    private List<Fragment> fragments;

    /**
     * Required empty constructor
     */
    public WalkFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Get a holder to the host activity
        mActivity = (IFragmentCommunication) activity;

        // Get interesting places in their right order
        interestingPlaces = Utils.getInterestingPlaces(((Activity) mActivity));

        prepareFragments();

    }

    @Override
    public void onResume() {
        super.onResume();

        // If there is no GPS, prompt user for turning it on
        if (!Utils.GPSOn(this.getActivity())) {
            Utils.showGPSDialog(this.getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_walk, container, false);

        return rootView;
    }

    /**
     * Starts navigation system with the destination set to beginning of
     * the walk
     */
    public void navigateToStart() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=49.845310,19.718493"));
        startActivity(intent);
    }

    private void prepareFragments() {
        InterestingPlaceFlyweightFragment fragment;
        fragments = new ArrayList<>();
        int i = 0;

        for (i = 0; i + 1 < interestingPlaces.size(); i++) {
            fragment = new InterestingPlaceFlyweightFragment();
            fragment.setPlace(interestingPlaces.get(i));
            fragment.setNextPlace(interestingPlaces.get(i + 1));

            // Set the mode in which FAB is visible but arrow
            // taking the user back to this fragment isn't
            fragment.setMode(Utils.WALK_MODE);
            fragments.add(fragment);
        }

        // Add last fragment without next place (this is the end of the walk)
        fragment = new InterestingPlaceFlyweightFragment();
        fragment.setPlace(interestingPlaces.get(i));
        fragment.setNextPlace(null);

        // Set the mode in which FAB is visible but arrow
        // taking the user back to this fragment isn't
        fragment.setMode(Utils.WALK_MODE);
        fragments.add(fragment);

    }

    public void setFragment(int idx) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = ((Activity) mActivity).getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragments.get(idx))
                .commit();
    }

    public List<Fragment> getFragments() {
        return fragments;
    }
}
