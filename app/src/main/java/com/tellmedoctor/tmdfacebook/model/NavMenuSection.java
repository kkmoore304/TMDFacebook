package com.tellmedoctor.tmdfacebook.model;

import com.tellmedoctor.tmdfacebook.ui.views.NavDrawerItem;

/**
 * Created by kmoore on 5/2/15.
 */
public class NavMenuSection implements NavDrawerItem {

    private final int id;
    private final String label;
    private final int icon;
    private final boolean updateActionBarTitle;
    private final int isSection;


    public NavMenuSection(int id, String label) {
        this.id = id;
        this.label = label;
        icon = 0;
        updateActionBarTitle = false;
        isSection = 1;
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
}