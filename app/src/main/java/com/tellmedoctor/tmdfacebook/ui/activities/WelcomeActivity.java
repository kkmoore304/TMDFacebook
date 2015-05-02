package com.tellmedoctor.tmdfacebook.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.ui.Config;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;
import android.view.View;

public class WelcomeActivity extends ActionBarActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_welcome);

            findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefsUtils.setEulaAccepted(WelcomeActivity.this, true);
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.button_decline).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefsUtils.setEulaAccepted(WelcomeActivity.this,false);
                    finish();
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();

            // Shows the debug warning, if this is a debug build and the warning has not been shown yet
            if (Config.IS_DOGFOOD_BUILD && !PrefsUtils.wasDebugWarningShown(this)) {
                new AlertDialog.Builder(this)
                        .setTitle(Config.DOGFOOD_BUILD_WARNING_TITLE)
                        .setMessage(Config.DOGFOOD_BUILD_WARNING_TEXT)
                        .setPositiveButton(android.R.string.ok, null).show();
                PrefsUtils.markDebugWarningShown(this);
            }
        }
    }

