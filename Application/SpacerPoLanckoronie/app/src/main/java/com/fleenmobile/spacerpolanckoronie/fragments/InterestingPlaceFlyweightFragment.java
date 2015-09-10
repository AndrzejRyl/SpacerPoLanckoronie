package com.fleenmobile.spacerpolanckoronie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSService;
import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.Utils;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.activities.MainActivity;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.dialogs.EndDialog;
import com.fleenmobile.spacerpolanckoronie.dialogs.NextPlaceDialog;
import com.fleenmobile.spacerpolanckoronie.fonts.RobotoTextView;

/**
 * @author FleenMobile at 2015-08-23
 */
public class InterestingPlaceFlyweightFragment extends Fragment {

    @NonNull
    private InterestingPlace place;
    private IFragmentCommunication mActivity;
    private GPSService service;

    private RobotoTextView name, content;
    private ImageView image;
    private ImageView arrow;

    private MediaPlayer mp;

    // Walk module additions
    private InterestingPlace nextPlace;
    private ImageButton FAB;
    private String mode = "";
    private NextPlaceDialog dialog;
    private EndDialog endDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Get a holder to the host activity
        mActivity = (IFragmentCommunication) activity;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Stop playing the sound
        if (mp != null && mp.isPlaying())
            mp.pause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_flyweight_interesting_places, container, false);

        // Find views
        name = (RobotoTextView) rootView.findViewById(R.id.flyweight_title);
        content = (RobotoTextView) rootView.findViewById(R.id.flyweight_content);
        content.setMovementMethod(new ScrollingMovementMethod());
        image = (ImageView) rootView.findViewById(R.id.flyweight_image);
        arrow = (ImageView) rootView.findViewById(R.id.flyweight_arrow);
        FAB = (ImageButton) rootView.findViewById(R.id.flyweight_FAB);

        // Set data to those views based on interesting place
        name.setText(place.getName());
        content.setText(place.getDescription());
        image.setImageResource(place.getImage());

        // Play the sound connected to the given interesting place
        playSound();

        // Set visibility on arrow and FAB buttons
        if (mode.equals(Utils.INTERESTING_PLACE_MODE)) {
            setArrow();
        } else if (mode.equals(Utils.WALK_MODE)) {
            setFAB();
        }

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (dialog != null && dialog.isVisible())
            dialog.dismiss();

        if (endDialog != null && endDialog.isVisible())
            endDialog.dismiss();
    }

    private void playSound() {
        mp = MediaPlayer.create(this.getActivity(), place.getAudio());

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                InterestingPlaceFlyweightFragment.this.mp = null;
            }
        });

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void setPlace(InterestingPlace place) {
        this.place = place;
    }

    public void setNextPlace(InterestingPlace place) {
        this.nextPlace = place;
    }

    public void setService(GPSService service) {
        this.service = service;
    }

    /**
     * Set arrow to take the user back to "Interesting places" card
     */
    public void setArrow() {
        arrow.setVisibility(View.VISIBLE);
        FAB.setVisibility(View.INVISIBLE);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop playing the sound
                if (mp != null && mp.isPlaying())
                    mp.pause();
                mActivity.onMssgReceived(MainActivity.GO_BACK_INTERESTING_PLACES, "");
            }
        });
    }

    /**
     * Set FAB to take the user next place
     */
    public void setFAB() {
        FAB.setVisibility(View.VISIBLE);
        arrow.setVisibility(View.INVISIBLE);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop playing the sound
                if (mp != null && mp.isPlaying())
                    mp.pause();

                goToNextPlace();
            }
        });
    }

    // Show the user dialog displaying which place is next and allow
    // him to see the map with this place marked
    private void goToNextPlace() {
        if (nextPlace != null) {
            dialog = NextPlaceDialog.newInstance(mActivity, nextPlace, service);
            dialog.show(((Activity) mActivity).getFragmentManager(), "Next place dialog");
        } else {
            endDialog = EndDialog.newInstance(mActivity);
            endDialog.show(((Activity) mActivity).getFragmentManager(), "End dialog");
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
