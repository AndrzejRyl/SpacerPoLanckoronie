package com.fleenmobile.spacerpolanckoronie.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.adapters.InterestingPlace;
import com.fleenmobile.spacerpolanckoronie.fonts.RobotoTextView;

/**
 * This dialog will inform the user which place is next
 * in the walk. We have a special order in which interesting
 * places are visited.
 *
 * It will also allow the user to see a map with this place marked
 *
 * @author FleenMobile at 2015-09-09
 */
public class NextPlaceDialog extends DialogFragment{
    private static IFragmentCommunication creatorInstance;
    private static InterestingPlace mPlace;

    private ImageView imageView;
    private RobotoTextView nameTV;
    private RobotoTextView buttonTV;

    public static NextPlaceDialog newInstance(IFragmentCommunication instance, InterestingPlace place) {

        // Start a dialog
        NextPlaceDialog dialog = new NextPlaceDialog();

        // Get a holder to host activity
        creatorInstance = instance;

        // This is a place that the user is going to
        mPlace = place;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_next_place_dialog, null,
                false);

        // Make sure this dialog has no title (we've got a cutom one)
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Find views
        nameTV = (RobotoTextView) view.findViewById(R.id.next_place_name);
        buttonTV = (RobotoTextView) view.findViewById(R.id.next_place_button);
        imageView = (ImageView) view.findViewById(R.id.next_place_image);

        // Set data (name of the interesting place and it's picture as well as onClickListener)
        setData();


        return view;
    }

    private void setData() {
        nameTV.setText(mPlace.getName());
        imageView.setImageResource(mPlace.getImage());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        buttonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Start MapActivity with this places GPS position
            }
        });
    }

}
