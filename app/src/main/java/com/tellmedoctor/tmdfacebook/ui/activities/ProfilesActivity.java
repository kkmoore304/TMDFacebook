package com.tellmedoctor.tmdfacebook.ui.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.model.profile;
import com.tellmedoctor.tmdfacebook.ui.fragments.DatePickerFragment;
import com.tellmedoctor.tmdfacebook.utils.HttpClientK;
import com.tellmedoctor.tmdfacebook.utils.LogUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfilesActivity extends ActionBarActivity {

    private EditText fname;
    private EditText lname;
    private EditText email;
    private EditText pword;
    private EditText phone;
    private Spinner gender;
    private Spinner language;
    private Spinner role;
    private ImageButton fab_profile;
    private TextView dob;

    private DateFormat sdf = new SimpleDateFormat("hh:mm a");
    private SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd yyyy ");
    private SimpleDateFormat df_time = new SimpleDateFormat("hh.mm a ");
    private Date selectedTime;
    private Date selectedDate;

    private Date currentdate;
    private Date currenttime;
    /**
     * This handles the message send from TimePickerFragment on setting Time
     */
    Handler mDateHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            /** Creating a bundle object to pass currently set Time to the fragment */
            Bundle b = m.getData();

            /** Getting the Hour of year from bundle */
            int mYear = b.getInt("set_year") - 1900;

            /** Getting the Minute of the month from bundle */
            int mMonth = b.getInt("set_month");

            /** Getting the Minute of the day from bundle */
            int mDay = b.getInt("set_day");


            /** Displaying a short time message containing time set by Time picker dialog fragment */
            //  Toast.makeText(getActivity(), b.getString("set_date"), Toast.LENGTH_LONG).show();

            setDate(mYear, mMonth, mDay);

        }
    };
    private profile profileItem ;
    private int mDateStackLevel;

    private void setDate(int y, int m, int d) {

        Calendar rightNow = Calendar.getInstance();
        if (currenttime == null)
            currenttime = rightNow.getTime();

        int hour = currenttime.getHours();
        int minutes = currenttime.getMinutes();
        int seconds = currenttime.getSeconds();

        currenttime = new Date(y, m, d, hour, minutes, seconds);

        dob.setText(df.format(currenttime));
        dob.setTextColor(getResources().getColor(R.color.primaryAccentColor));
        dateTime(currenttime);
    }

    private void dateTime(Date rightNow) {
        currentdate = rightNow;
        currenttime = rightNow;
        profileItem.setDOB(String.valueOf(rightNow.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        if (profileItem  == null)
            profileItem = new profile();

        Toolbar toolbar = (Toolbar) findViewById(R.id.new_monitor_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Profile");
            Toast.makeText(this, "Should have title", Toast.LENGTH_SHORT).show();
        }

        fname = (EditText) findViewById(R.id.fname);
        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (profileItem  == null)
                    profileItem = new profile();

                if (s.toString().length() == 0)
                    profileItem.setFirstname(fname.getText().toString());
                else
                    profileItem.setFirstname(fname.getText().toString());
                //initializeScreen();
                //notes.setTextColor(getResources().getColor(R.color.primaryAccentColor));
            }
        });

        lname = (EditText) findViewById(R.id.lnmae);
        email = (EditText) findViewById(R.id.email);
        pword = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        gender = (Spinner) findViewById(R.id.gender_spinner);
        language = (Spinner) findViewById(R.id.lang_spinner);
        role = (Spinner) findViewById(R.id.role_spinner);

        fab_profile = (ImageButton) findViewById(R.id.fab_profile);
        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //push to profile object
                profileItem.setEmail(email.getText().toString());
                profileItem.setFirstname(fname.getText().toString());
                profileItem.setLastname(lname.getText().toString());
                profileItem.setPhonenumber(phone.getText().toString());
                profileItem.setGender(gender.getSelectedItem().toString());
                profileItem.setLang(language.getSelectedItem().toString());
                profileItem.setRole(role.getSelectedItem().toString());

                try {
                    sendtoserver(profileItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dob = (TextView) findViewById(R.id.dob);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        //  getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendtoserver(profile profileItem) throws JSONException {
        Gson gson = new Gson();
        String json = gson.toJson(profileItem);

        JSONObject obj = new JSONObject();
        obj.put("Email", profileItem.getEmail().toString());
        obj.put("Firstname",  profileItem.getFirstname());
        obj.put("Lastname", profileItem.getLastname());
        obj.put("Gender", profileItem.getGender());
        obj.put("Email", profileItem.getEmail());
        obj.put("Phonenumber", profileItem.getPhonenumber());
        obj.put("Lang", profileItem.getLang());
        obj.put("Role", profileItem.getRole());
        obj.put("DOB", profileItem.getDOB());


        JSONObject res = HttpClientK.SendHttpPost("http://smilebyibis.com/hack1", obj);

        if(res!=null)
        {
            LogUtils.LOGD("JSON->","test");
        }
        ///  PrefsUtils.setReadings(_context, json);
        finish();
    }

    public void showDatePickerDialog(View v) {


        mDateStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("datepicker");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DatePickerFragment newDateFragment = DatePickerFragment.newInstance(mDateStackLevel, mDateHandler);


        int m = newDateFragment.show(ft, "datepicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
