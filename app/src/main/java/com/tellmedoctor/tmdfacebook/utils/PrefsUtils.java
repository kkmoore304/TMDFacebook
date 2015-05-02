package com.tellmedoctor.tmdfacebook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.tellmedoctor.tmdfacebook.model.questionItem;
import com.tellmedoctor.tmdfacebook.ui.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmoore on 5/2/15.
 */
public class PrefsUtils {
    private static final String TAG = PrefsUtils.class.getSimpleName();

    /* determines if the application finished all initialization functions*/
    private static final String PREF_SETUP_DONE = "pref_setup_done";
    /* The current version of the App */
    private static final String PREF_APP_VERSION = "pref_app_version";
    /* remembers the state of the Nav drawer */
    private static final String PREF_USER_LEARNED_DRAWER = "pref_user_learned_drawer";
    /* Boolean indicating whether the debug build warning was already shown */
    private static final String PREF_DEBUG_BUILD_WARNING_SHOWN = "pref_debug_build_warning_shown";
    /* Push question */
    private static final String PREF_QUESTIONS = "pref_questions";

    /**
     * enabled when the EULA is Accepted
     *
     * @param context
     * @return
     */
    public static boolean getEulaAccepted(final Context context) {
        // write to shared preferences
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);

        // Check what version we're configured for
        int appVersion = sp.getInt(PREF_APP_VERSION, 0);
        if (appVersion != Config.APP_VERSION) {
            // Application is configured for a different version. Reset
            // preferences and re-run setup.
            sp.edit().clear().putInt(PREF_APP_VERSION, Config.APP_VERSION)
                    .commit();
        }

        return sp.getBoolean(PREF_SETUP_DONE, false);
    }

    public static void setEulaAccepted(Context c, boolean b) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(c);
        sp.edit().putBoolean(PREF_SETUP_DONE, b).commit();

    }
    /**
     * Drawer managemernt
     *
     * @param activity
     * @param aTrue
     */
    public static void setUserLearnedDrawer(Context activity, Boolean aTrue) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(activity);
        sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, aTrue).commit();

    }

    public static boolean getLearnedDrawer(Context activity) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(activity);

        return sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
    }

    /**
     * indicating whether the debug build warning was already shown
     *
     * @param context
     * @return
     */
    public static boolean wasDebugWarningShown(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_DEBUG_BUILD_WARNING_SHOWN, false);
    }


    public static void markDebugWarningShown(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_DEBUG_BUILD_WARNING_SHOWN, true).commit();
    }


    public static void setQuestions(Context context, String json) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_QUESTIONS, json).commit();

    }

    public static List<questionItem> getQuestions(Context _context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(_context);
        String json = sp.getString(PREF_QUESTIONS, "");
       // questionItem[] q = gson.fromJson(json, questionItem[].class);

        ArrayList<questionItem> q = gson.fromJson(json, new TypeToken<ArrayList<questionItem>>() {
        }.getType());
        return q;
    }


}
