package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by xzp on 2017-07-30.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.time";

    private static final String ARG_DATE = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Date getDate(Intent intent) {
        return (Date) intent.getSerializableExtra(EXTRA_DATE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
            .inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        } else {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
            .setView(v)
            .setTitle(R.string.time_picker_title)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int hour;
                    int minute;

                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        hour= mTimePicker.getCurrentHour();
                        minute = mTimePicker.getCurrentMinute();
                    } else {
                        hour= mTimePicker.getHour();
                        minute = mTimePicker.getMinute();
                    }

                    Date date = new GregorianCalendar(0, 0, 0, hour, minute).getTime();
                    sendResult(Activity.RESULT_OK, date);
                }
            })
            .create();
    }

    private void sendResult(int resultCode, Date date) {
        if(getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
