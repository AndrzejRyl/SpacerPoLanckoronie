package com.fleenmobile.spacerpolanckoronie.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fleenmobile.spacerpolanckoronie.R;
import com.fleenmobile.spacerpolanckoronie.activities.IFragmentCommunication;
import com.fleenmobile.spacerpolanckoronie.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter contains interesting places and displays
 * them in cards
 *
 * @author FleenMobile at 2015-08-22
 */
public class InterestingPlacesAdapter extends ArrayAdapter<InterestingPlace>{

    private List<InterestingPlace> objects;
    private Context context;
    private int layout;
    private IFragmentCommunication mActivity;


    public InterestingPlacesAdapter(Context context, int resource, List<InterestingPlace> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layout = resource;
        this.objects = new ArrayList<InterestingPlace>();
        this.objects.addAll(objects);
        this.mActivity = (IFragmentCommunication)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        InterestingPlaceHolder holder = null;

        // Get a holder (existing or a new one)
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);

            holder = new InterestingPlaceHolder();
            holder.image = (ImageView) row.findViewById(R.id.card_interesting_place_image);
            holder.name = (TextView) row.findViewById(R.id.card_interesting_place_name);
            holder.description = (TextView) row.findViewById(R.id.card_interesting_place_description);

            row.setTag(holder);
        } else {
            holder = (InterestingPlaceHolder) row.getTag();
        }

        // Set holder according to interesting place data
        final InterestingPlace interestingPlace = objects.get(position);
        holder.image.setImageResource(interestingPlace.getSmallImage());
        holder.name.setText(interestingPlace.getName());
        holder.description.setText(interestingPlace.getShortDesc());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Let MainActivity know which menu item was chosen
                mActivity.onMssgReceived(MainActivity.INTERESTING_PLACE_CHOSEN, String.valueOf(position));
            }
        });

        return row;
    }

    static class InterestingPlaceHolder {
        ImageView image;
        TextView name;
        TextView description;
    }
}
