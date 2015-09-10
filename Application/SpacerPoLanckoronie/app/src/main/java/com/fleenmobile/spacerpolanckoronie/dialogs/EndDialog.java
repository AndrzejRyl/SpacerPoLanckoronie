package com.fleenmobile.spacerpolanckoronie.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.fonts.RobotoTextView;

/**
 * This dialog will inform the user that this is the
 * end of the walk. He will be also informed that
 * we can navigate him to our local beekeeper
 *
 * @author FleenMobile at 2015-09-10
 */
public class EndDialog extends DialogFragment {
    private static IFragmentCommunication creatorInstance;

    private ImageView imageView;
    private RobotoTextView nameTV;
    private RobotoTextView buttonTV;

    public static EndDialog newInstance(IFragmentCommunication instance) {

        // Start a dialog
        EndDialog dialog = new EndDialog();

        // Get a holder to host activity
        creatorInstance = instance;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_end_walk_dialog, null,
                false);

        // Make sure this dialog has no title (we've got a cutom one)
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Find views
        buttonTV = (RobotoTextView) view.findViewById(R.id.end_walk_button);

        setOnClickListener();

        return view;
    }

    private void setOnClickListener() {
        buttonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndDialog.this.dismiss();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=49.850217,19.726382"));
                startActivity(intent);
            }
        });
    }

}
