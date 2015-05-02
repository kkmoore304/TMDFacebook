package com.tellmedoctor.tmdfacebook.adapters;

import android.content.Context;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.model.NavMenuItem;
import com.tellmedoctor.tmdfacebook.model.NavMenuSection;
import com.tellmedoctor.tmdfacebook.ui.views.NavDrawerItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kmoore on 5/2/15
 */
public class NavArrayAdapter extends ArrayAdapter<NavDrawerItem> {


    private enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    private Context context;
    private ArrayList<NavDrawerItem> items;
    private LayoutInflater vi;

    public NavArrayAdapter(Context context, ArrayList<NavDrawerItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getViewTypeCount() {  //Returns the number of types of Views that will be created by getView(int, View, ViewGroup).
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        //framework calls getItemViewType for row n, the row it is about to display.
        //Get the type of View that will be created by getView(int, View, ViewGroup) for the specified item.
        return getItem(position).getType() == 1 ? 0 : 1; // get position passes (n) and ascertain if it is a header  or not
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isEnabled();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final NavDrawerItem i = items.get(position);

        if (i != null) {
            if (i.getType() == 1) {
                NavMenuSection si = (NavMenuSection) i;

                v = vi.inflate(R.layout.navdrawer_section, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.navmenusection_label);

                sectionView.setText(si.getLabel());
            } else {
                NavMenuItem ei = (NavMenuItem) i;
                v = vi.inflate(R.layout.navdrawer_item, null);
                final TextView title = (TextView) v.findViewById(R.id.navmenuitem_label);
                ImageView imageView = (ImageView) v.findViewById(R.id.navmenuitem_icon);

                if (title != null) {
                    title.setText(ei.getLabel());
                    imageView.setImageResource(ei.getIcon());
                }
                v.setTransitionName(String.valueOf(title.getText()));

            }
        }
        return v;
    }

}