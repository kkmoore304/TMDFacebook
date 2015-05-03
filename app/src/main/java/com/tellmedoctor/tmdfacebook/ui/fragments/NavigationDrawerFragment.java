package com.tellmedoctor.tmdfacebook.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.adapters.NavArrayAdapter;
import com.tellmedoctor.tmdfacebook.model.NavMenuItem;
import com.tellmedoctor.tmdfacebook.model.NavMenuSection;
import com.tellmedoctor.tmdfacebook.model.profile;
import com.tellmedoctor.tmdfacebook.ui.activities.MainActivity;
import com.tellmedoctor.tmdfacebook.ui.views.NavDrawerItem;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private static AccessToken tokenz;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private Context context;
    private String[] mNavTitles;
    private CallbackManager callbackManager;

    ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
    private View rootView;
    private LoginButton loginButton;

    List<String> permissionNeeds = Arrays.asList("user_photos", "friends_photos", "email", "user_birthday", "user_friends");
    private AccessTokenTracker accessTokenTracker;
    private String token;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mUserLearnedDrawer = PrefsUtils.getLearnedDrawer(getActivity());
        context = getActivity();
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        //  mNavTitles = getResources().getStringArray(R.array.navtitles_array);

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        FacebookSdk.sdkInitialize(context);
        rootView = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        mDrawerListView = (ListView) rootView.findViewById(R.id.navlist);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        final TextView name = (TextView) rootView.findViewById(R.id.name);
        final TextView email = (TextView) rootView.findViewById(R.id.email);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();//logInWithReadPermissions(getActivity(), permissionNeeds);

                PrefsUtils.setFBAuth(getActivity(), false);
                Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                //String keyIdentifier_data_entry = "EulaActivity";
                //EulaIntent.putExtra("fragment", keyIdentifier_data_entry);
                startActivity(MainIntent);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // App code
            }
        };
        AccessToken newString;
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                token = null;
            } else {
                token = (String) extras.get("token");
            }
        } else {
            newString = null;//(AccessToken) savedInstanceState.getSerializable("token");
        }

        if (PrefsUtils.getAccessToken(getActivity()) != null &&
                PrefsUtils.getAccessTokenDate(getActivity()) != 0) {

            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
            String date = formatter.format(PrefsUtils.getAccessTokenDate(getActivity()));
            tokenz = AccessToken.getCurrentAccessToken();
        }
        final profile fbUser = new profile();
        GraphRequest request = GraphRequest.newMeRequest(
                tokenz,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject user,
                            GraphResponse response) {
                        if (user != null) {
                            fbUser.setEmail(user.optString("email"));
                            fbUser.setFirstname(user.optString("name"));
                            fbUser.setId(user.optString("id"));
                            if (name != null)
                                name.setText(user.optString("name"));
                            if (email != null)
                                email.setText(user.optString("email"));
                            ImageView userpicture = (ImageView) rootView.findViewById(R.id.image);
                            RetrieveImageTask task = new RetrieveImageTask(userpicture);
                            task.execute(user.optString("id"));

                        }
                    }
                });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        items.add(new NavMenuItem(0, getString(R.string.questions), "ic_questions", true, getActivity(), 0));
        items.add(new NavMenuItem(1, getString(R.string.answers), "ic_answer", true, getActivity(), 0));
        items.add(new NavMenuItem(2, getString(R.string.schedules), "ic_schedules", true, getActivity(), 0));
        items.add(new NavMenuSection(100, getString(R.string.settings)));
        items.add(new NavMenuItem(3, getString(R.string.patient), "ic_profiles", true, getActivity(), 0));


        NavArrayAdapter adapter = new NavArrayAdapter(getActivity(), items);

       /* mDrawerListView.setAdapter(new NavArrayAdapter(getActionBar()
                .getThemedContext(), R.layout.navdrawer_item, mNavTitles));*/

        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return rootView;//mDrawerListView;
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener


       /* ActionBar actionBar = getActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);*/


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                   /* SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();*/

                    PrefsUtils.setUserLearnedDrawer(getActivity(), true);
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //  actionBar.setTitle(R.string.app_name);
        actionBar.setLogo(R.drawable.ic_tellme_doc);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void setup(int navigation_drawer, DrawerLayout viewById, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(navigation_drawer);
        mDrawerLayout = viewById;
        //mDrawerLayout.setStatusBarBackgroundColor(
        //       getResources().getColor(R.color.primaryColor_500));
        toolbar.setNavigationIcon(R.drawable.ic_navigation_drawer);
       /* mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    *//* host Activity *//*
                mDrawerLayout,                    *//* DrawerLayout object *//*
                R.drawable.ic_drawer,             *//* nav drawer image to replace 'Up' caret *//*
                R.string.navigation_drawer_open,  *//* "open drawer" description for accessibility *//*
                R.string.navigation_drawer_close  *//* "close drawer" description for accessibility */
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    PrefsUtils.setUserLearnedDrawer(getActivity(), true);
                    //  saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public Bitmap getPhotoFacebook(final String id) {

        Bitmap bitmap = null;
        final String nomimg = "https://graph.facebook.com/" + id + "/picture?type=large";
        URL imageURL = null;

        try {
            imageURL = new URL(nomimg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            //img_value.openConnection().setInstanceFollowRedirects(true).getInputStream()
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;

    }


    private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;
        private WeakReference<ImageView> imageViewReference;
        private String data;

        public RetrieveImageTask(ImageView userpicture) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(userpicture);
        }


        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return getPhotoFacebook(data);
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
