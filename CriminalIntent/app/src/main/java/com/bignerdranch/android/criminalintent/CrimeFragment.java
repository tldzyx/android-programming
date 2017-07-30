package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by xzp on 2017-07-17.
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_TIME = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                    .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = view.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }

    public static UUID getCrimeId(Intent data) {
        return (UUID)data.getSerializableExtra(ARG_CRIME_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DATE: {
                switch (resultCode){
                    case Activity.RESULT_OK: {
                        Date newDate = DatePickerFragment.getDate(data);
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.setTime(newDate);

                        Date oldDate = mCrime.getDate();
                        Calendar oldCalendar = Calendar.getInstance();
                        oldCalendar.setTime(oldDate);

                        int year = newCalendar.get(Calendar.YEAR);
                        int month = newCalendar.get(Calendar.MONTH);
                        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
                        int hour = oldCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = oldCalendar.get(Calendar.MINUTE);
                        Date date = new GregorianCalendar(year, month,day, hour, minute).getTime();

                        mCrime.setDate(date);
                        updateDate();
                    } break;
                }
            } break;
            case REQUEST_TIME: {
                switch (resultCode){
                    case Activity.RESULT_OK: {
                        Date newTime = TimePickerFragment.getDate(data);
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.setTime(newTime);

                        Date oldTime = mCrime.getDate();
                        Calendar oldCalendar = Calendar.getInstance();
                        oldCalendar.setTime(oldTime);

                        int year = oldCalendar.get(Calendar.YEAR);
                        int month = oldCalendar.get(Calendar.MONTH);
                        int day = oldCalendar.get(Calendar.DAY_OF_MONTH);
                        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = newCalendar.get(Calendar.MINUTE);
                        Date date = new GregorianCalendar(year, month,day, hour, minute).getTime();

                        mCrime.setDate(date);
                        updateTime();
                    } break;
                }
            }
        }

    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = mCrime.getDate();
        String formatDate = sdf.format(date);
        mDateButton.setText(formatDate);
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date time = mCrime.getDate();
        String formatTime = sdf.format(time);
        mTimeButton.setText(formatTime);
    }
}
