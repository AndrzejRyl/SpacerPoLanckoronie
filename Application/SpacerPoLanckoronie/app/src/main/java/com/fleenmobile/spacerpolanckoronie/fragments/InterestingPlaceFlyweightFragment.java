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
import android.widget.ImageView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.activities.MainActivity;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.fonts.RobotoTextView;

/**
 * @author FleenMobile at 2015-08-23
 */
public class InterestingPlaceFlyweightFragment extends Fragment {

    @NonNull
    private InterestingPlace place;
    private IFragmentCommunication mActivity;

    private RobotoTextView name, content;
    private ImageView image;
    private ImageView arrow;

    private MediaPlayer mp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Get a holder to the host activity
        mActivity = (IFragmentCommunication) activity;
    }


    /**
     * Set arrow to take the user back to "Interesting places" card
     */
    private void setArrow() {
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

        // Set data to those views based on interesting place
        name.setText(place.getName());
        content.setText(place.getDescription());
        image.setImageResource(place.getImage());

        // Set arrow's onClickListener to get the user back to 'interesting places'
        setArrow();

        // Play the sound connected to the given interesting place
        playSound();

        return rootView;
    }

    private void playSound() {
        mp = MediaPlayer.create(this.getActivity(), place.getAudio());

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                InterestingPlaceFlyweightFragment.this.mp=null;
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
}
