package com.tellmedoctor.tmdfacebook.ui.views;

/**
 * Created by kmoore on 5/2/15.
 */
public interface NavDrawerItem {
    int getId();
    String getLabel();
    int getType();
    boolean isEnabled();
    boolean updateActionBarTitle();
}
