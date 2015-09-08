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
 * This adapter holds menu items on navigation drawer
 *
 * @author FleenMobile at 2015-08-21
 */
public class MenuItemAdapter extends ArrayAdapter<MyMenuItem> {

    private List<MyMenuItem> objects;
    private Context context;
    private int layout;
    private IFragmentCommunication mActivity;


    public MenuItemAdapter(Context context, int resource, List<MyMenuItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layout = resource;
        this.objects = new ArrayList<MyMenuItem>();
        this.objects.addAll(objects);
        this.mActivity = (IFragmentCommunication)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MenuItemHolder holder = null;

        // Get a holder (existing or a new one)
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);

            holder = new MenuItemHolder();
            holder.icon = (ImageView) row.findViewById(R.id.menu_item_icon);
            holder.name = (TextView) row.findViewById(R.id.menu_item_name);

            row.setTag(holder);
        } else {
            holder = (MenuItemHolder) row.getTag();
        }

        // Set holder according to menu item data
        final MyMenuItem menuItem = objects.get(position);
        holder.icon.setImageResource(menuItem.getIcon());
        holder.name.setText(menuItem.getName());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Let MainActivity know which menu item was chosen
                mActivity.onMssgReceived(MainActivity.MENU_ITEM_CHOSEN, String.valueOf(position));
            }
        });

        return row;
    }

    static class MenuItemHolder {
        ImageView icon;
        TextView name;
    }
}
