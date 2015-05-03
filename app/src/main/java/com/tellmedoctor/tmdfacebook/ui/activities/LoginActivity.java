package com.tellmedoctor.tmdfacebook.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/*import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;*/
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tellmedoctor.tmdfacebook.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.facebook.login.widget.ProfilePictureView;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ImageView userpicture;

    List<String> permissionNeeds=Arrays.asList("user_photos","friends_photos", "email", "user_birthday", "user_friends");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tellmedoctor.tmdfacebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);


        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissionNeeds);
            }
        });


        
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.toString());
                PrefsUtils.setFBAuth(LoginActivity.this, true);
                PrefsUtils.setAccessToken(LoginActivity.this, loginResult.getAccessToken().toString());
                PrefsUtils.setAccessTokenDate(LoginActivity.this, loginResult.getAccessToken().getExpires().getTime());
                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                String keyIdentifier_data_entry = loginResult.getAccessToken().getToken();
                MainIntent.putExtra("token", keyIdentifier_data_entry);
                startActivity(MainIntent);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "CANCEL");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, e.getMessage());
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




}

