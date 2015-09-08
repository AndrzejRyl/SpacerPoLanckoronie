package com.fleenmobile.spacerpolanckoronie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FleenMobile at 2015-08-22
 */
public class InterestingPlacesFragment extends Fragment {

    private IFragmentCommunication mActivity;

    private ListView list;

    private List<InterestingPlace> interestingPlaces;
    private List<InterestingPlaceFlyweightFragment> fragments;

    /**
     * Required empty constructor
     */
    public InterestingPlacesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        interestingPlaces = Utils.getInterestingPlaces(this.getActivity());
        initializeFragments();
    }

    private void initializeFragments() {
        InterestingPlaceFlyweightFragment fragment;
        fragments = new ArrayList<>();

        for (InterestingPlace place : interestingPlaces) {
            fragment = new InterestingPlaceFlyweightFragment();
            fragment.setPlace(place);
            fragments.add(fragment);
        }
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
        View rootView = inflater.inflate(R.layout.fragment_interesting_places, container, false);

        // Find list view and set the adapter containing cards to it
        list = (ListView) rootView.findViewById(R.id.interesting_places_listview);
        list.setAdapter(new InterestingPlacesAdapter(this.getActivity(),
                R.layout.interesting_place_list_item, interestingPlaces));

        return rootView;
    }

    /**
     * Sets a flyweight fragment with interesting place
     * (it changes hamburger icon into an arrow going back to interesting places)
     *
     * @param position Position of card chosen
     */
    public void selectItem(int position) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragments.get(position))
                .commit();

    }

}
