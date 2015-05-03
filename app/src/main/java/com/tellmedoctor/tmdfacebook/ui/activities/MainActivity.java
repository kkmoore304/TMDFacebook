package com.tellmedoctor.tmdfacebook.ui.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.facebook.FacebookSdk;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.ui.fragments.AnswersFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.AskAQuestionFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.ContactusFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.HomeFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.NavigationDrawerFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.ProfilesFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.QuestionFragment;
import com.tellmedoctor.tmdfacebook.ui.fragments.SchedulesFragment;
import com.tellmedoctor.tmdfacebook.utils.DialogUtils;
import com.tellmedoctor.tmdfacebook.utils.LogUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomeFragment.OnFragmentInteractionListener,
        QuestionFragment.OnFragmentInteractionListener,
        ProfilesFragment.OnFragmentInteractionListener,
        SchedulesFragment.OnFragmentInteractionListener,
        AnswersFragment.OnFragmentInteractionListener,
        ContactusFragment.OnFragmentInteractionListener,
        AskAQuestionFragment.OnFragmentInteractionListener {

    //private static final String TAG = HomeActivity.class.getSimpleName();
    //private static final int HOME = 0;
    private static final int QUESTIONS = 0;
    private static final int ANSWERS = 1;
    private static final int SCHEDULES = 2;
    private static final int PROFILES = 4;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int HELP = 6;
    private static final int CONTACT_US = 7;
    private static final int ASKQUESTION = 8;


    private FragmentManager f;
    private FragmentTransaction ft;
    private LayoutInflater inflater;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    // private CharSequence mTitle;
    private String position;
    protected String newString;
    private Context _context;
    private CharSequence mTitle;
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = getLayoutInflater();
        _context = getApplicationContext();

        FacebookSdk.sdkInitialize(getApplicationContext());
        String action_utilities = "tbd";
        String delete_all_data = "tbd";
        String back_and_rest = "tbd";
        String help = "HelpFragment";
        String contact_us = "ContactFragment";
        String pinLogBook = "tbd";
        String askQuestion = "AskQuestion";
        String Question = "Question";


        // update the main content by replacing with fragments
        // based on the action set. This is the Intent-filter interface
        // please see the manifest for details
        Intent intent = getIntent();

        // Action intent Filters from Utilities
        String action = intent.getAction();
        Bundle extras;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Replace main container with fragment based on "fragment"
        // argument called.
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("fragment");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("fragment");
        }
        if (mNavigationDrawerFragment != null) {
            // Set up the navigation drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }
        // Navigate to right fragment based on the custom intent filter
        // from utilities
        //TODO ALL THE OTHER NAVIGATION POSITIONS
        if (action != null && newString != null) {
            if (action.equalsIgnoreCase(action_utilities)) {


                if (newString.equalsIgnoreCase(delete_all_data))
                    onNavigationDrawerItemSelected(QUESTIONS);


                if (newString.equalsIgnoreCase(back_and_rest))
                    onNavigationDrawerItemSelected(ANSWERS);

                if (newString.equalsIgnoreCase(back_and_rest))
                    onNavigationDrawerItemSelected(SCHEDULES);

                if (newString.equalsIgnoreCase(back_and_rest))
                    onNavigationDrawerItemSelected(PROFILES);
            }
        }

        // navigate to to right fragment based on the extras sent
        if (newString != null) {

            if (newString.equalsIgnoreCase(askQuestion.toLowerCase()))
                onNavigationDrawerItemSelected(ASKQUESTION);

            if (newString.equalsIgnoreCase(help)) {
                onNavigationDrawerItemSelected(HELP);
            }

            if (newString.equalsIgnoreCase(contact_us)) {
                onNavigationDrawerItemSelected(CONTACT_US);
            }

            // pin the logbook
            if (newString.equalsIgnoreCase(pinLogBook)) {
                //add it to the HomeItemList
                onNavigationDrawerItemSelected(QUESTIONS);
            }

            // pin the logbook
            if (newString.equalsIgnoreCase(Question.toLowerCase())) {
                //add it to the HomeItemList
                onNavigationDrawerItemSelected(QUESTIONS);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * response from the logbook fragment
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != 1) {
            return;
        }
        if (resultCode == RESULT_OK) {
            //String result= data.getStringExtra("result");
            newString = "tbd";
            onNavigationDrawerItemSelected(0);
        }
        if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
            LogUtils.LOGD(TAG, "Decided not to...");
        }
    }//onActivityResult


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        f = getFragmentManager();
        ft = f.beginTransaction();
        this.position = String.valueOf(position);

        switch (position) {
           /* case HOME:
                //  mTitle = getString(R.string.home);
                HomeFragment hh = HomeFragment.newInstance("HomeFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + hh.getTag());
                ft = ft.replace(R.id.container, hh, "HomeFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                break;*/
            case QUESTIONS:
                QuestionFragment qq = QuestionFragment.newInstance("QuestionFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + qq.getTag());
                ft = ft.replace(R.id.container, qq, "QuestionFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                break;

            case ANSWERS:
                AnswersFragment aa = AnswersFragment.newInstance("AnswerFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + aa.getTag());
                ft = ft.replace(R.id.container, aa, "AnswerFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                break;

            case SCHEDULES:
                SchedulesFragment ss = SchedulesFragment.newInstance("ScheduleFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + ss.getTag());
                ft = ft.replace(R.id.container, ss, "ScheduleFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                break;

            case PROFILES:
               /* ProfilesFragment pp = ProfilesFragment.newInstance("ProfileFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + pp.getTag());
                ft = ft.replace(R.id.container, pp, "ProfileFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();*/

                Intent ProfileIntent = new Intent(this, ProfilesActivity.class);
                String keyIdentifier_data_entry = "profilesFragment";
                ProfileIntent.putExtra("fragment", keyIdentifier_data_entry);
                startActivity(ProfileIntent);
                break;
            case ASKQUESTION:
                AskAQuestionFragment aq = AskAQuestionFragment.newInstance("AskAQuestionFragment", newString);
                LogUtils.LOGD(TAG, "tag Name:" + aq.getTag());
                ft = ft.replace(R.id.container, aq, "AskAQuestionFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                break;

            default:
                break;
        }
    }


   /* */

    /**
     * Reset the action header with the app name and icon
     *//*
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu menu1 = menu;
        //  if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        f = getFragmentManager();
        ft = f.beginTransaction();

        if (id == R.id.action_about) {
            try {

                DialogUtils.createAboutDialog(this, inflater).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (id == R.id.action_contact) {
            ft = ft.replace(R.id.container, ContactusFragment.newInstance("Contact Us Fragment ", "Main"));
            ft.addToBackStack(this.position);
            ft.commit();//AllowingStateLoss();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Reset the action header with the app name and icon
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        // actionBar.setTitle(mTitle);

        // actionBar.setNavigationIcon(R.drawable.ic_launcher);
        // actionBar.setTitle("Title");
        // actionBar.setSubtitle("Sub");
        actionBar.setLogo(R.drawable.ic_tellme_doc);
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu menu1 = menu;
        //  if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        f = getFragmentManager();
        ft = f.beginTransaction();

        if (id == R.id.action_about) {
            try {

                DialogUtils.createAboutDialog(this, inflater).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (id == R.id.action_contact) {
            ft = ft.replace(R.id.container, ContactUsFragment.newInstance("Contact Us Fragment ", "Main"));
            ft.addToBackStack(this.position);
            ft.commit();//AllowingStateLoss();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Fragment managing the behaviors, interactions and presentation of the navigation drawer.*/
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getTitle();
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //   layout_drawer = (LinearLayout) findViewById(R.id.layout_drawer);
        mNavigationDrawerFragment.setup(R.id.navigation_drawer, drawerLayout, toolbar);

    }

}
