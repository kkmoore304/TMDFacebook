package com.tellmedoctor.tmdfacebook.model;

import android.content.Context;
import com.tellmedoctor.tmdfacebook.ui.views.NavDrawerItem;

/**
 * Created by kmoore on 5/2/15.
 */
public class NavMenuItem implements NavDrawerItem {
    private final int id ;
    private final String label ;
    private final int icon ;
    private final boolean updateActionBarTitle ;
    private final int isSection;

    //test
    public NavMenuItem(int id, String label, String icon, boolean updateActionBarTitle, Context context, int isSection) {
        this.id = id;
        this.label = label;
        this.icon = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        this.updateActionBarTitle = updateActionBarTitle;
        this.isSection = isSection;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int getType() {
        return this.isSection;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }


    public int getIcon() {
        return icon;
    }
}
