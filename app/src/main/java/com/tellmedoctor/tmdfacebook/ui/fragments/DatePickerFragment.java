package com.tellmedoctor.tmdfacebook.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by kmoore on 5/2/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    static Handler mHandler ;
    private Activity mActivity;
    private TimePickerDialog.OnTimeSetListener mListener;
    private int month;
    private int day;
    private int year;

   /* public DatePickerFragment(Handler h) {
        this.mHandler=h;
    }*/

    public DatePickerFragment() {
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static DatePickerFragment newInstance(int num,Handler h ) {
        DatePickerFragment f = new DatePickerFragment();//h);
        mHandler=h;
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /***
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param month       The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param day         The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        this.year =year;
        this.month = month;
        this.day=day;

        /** Creating a bundle object to pass currently set time to the fragment */
        Bundle b = new Bundle();

        /** Adding currently set month to bundle object */
        b.putInt("set_month", month);

        /** Adding currently set day to bundle object */
        b.putInt("set_day", day);

        /** Adding currently set year to bundle object */
        b.putInt("set_year", year);

        /** Adding Current time in a string to bundle object */
        b.putString("set_date", "Set Date : " + Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year));

        /** Creating an instance of Message */
        Message m = new Message();

        /** Setting bundle object on the message object m */
        m.setData(b);

        /** Message m is sending using the message handler instantiated in MainActivity class */
        mHandler.sendMessage(m);
    }


}
