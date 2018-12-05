package com.example.vern.mmoodd.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.vern.mmoodd.Adapters.MoodGridViewAdapter;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectMoodFragment extends Fragment {

    private MoodDataSource moodDataSource;
    private MoodGridViewAdapter adapter;
    private int selectedPosition = -1;

    List<Mood> moodList = new ArrayList<>();

    private TextView dateText;
    private TextView timeText;
    private ImageButton nextBtn;
    private boolean nextBtnFirst = false;

    private Animation fadeIn;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private DateManager dM;

    private String dateString;
    private int[] dateMembers = new int[3];
    private int[] timeMembers = new int[2];

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_mood_grid_view_page, container, false);


        final String dateSelected = getArguments().getString("date");

        //set up dateTime view
        dM = new DateManager();
        Date date = dM.getDatefromStr(dateSelected);
        String dateOnlyStr = dM.getDateStr(date);
        String timeOnlyStr = dM.getTime(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setDateMembers(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        setTimeMembers(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        dateText = view.findViewById(R.id.selectDate);
        dateText.setText(dateOnlyStr);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dM = new DateManager();

                        String date = dayOfMonth + " " + dM.getMonthName(monthOfYear) + " " + year;

                        dateText.setText(date);
                        setDateMembers(year, monthOfYear, dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        timeText = view.findViewById(R.id.selectTime);
        timeText.setText(timeOnlyStr);

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String hour = hourOfDay + "";
                        String mins = minute + "";

                        if(hourOfDay < 10)
                        {
                            hour = "0" + hourOfDay;
                        }

                        if(minute < 10)
                        {
                            mins = "0" + minute;
                        }

                        timeText.setText(hour + ":" + mins);

                        setTimeMembers(hourOfDay, minute);

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        //set up mood selection view
        setUpAdapter();

        GridView gridView = view.findViewById(R.id.gridViewMood);
        adapter = new MoodGridViewAdapter(moodList, getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.setSelectedPosition(position);
                selectedPosition = position;
                adapter.notifyDataSetChanged();
                if (nextBtnFirst == false) {
                    nextBtn.startAnimation(fadeIn);
                    nextBtn.setAlpha(1.0f);
                    nextBtn.setClickable(true);
                    nextBtnFirst = true;
                }
            }
        });


        //set up next button view
        nextBtn = view.findViewById(R.id.next_btn);
        nextBtn.setClickable(false);
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_full_alpha);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood mood = moodList.get(selectedPosition);

                Bundle bundle = new Bundle();
                bundle.putInt("moodPosition", mood.getMoodID());
                bundle.putString("date", getFullDateString());

                CreateEntryFragment createEntryFragment = new CreateEntryFragment();
                createEntryFragment.setArguments(bundle);
                loadFragment(createEntryFragment);

            }
        });

        return view;
    }

    //getter setter of date members
    private int[] getDateMembers () { return dateMembers;}

    private void setDateMembers (int year, int month, int day)
    {
        dateMembers[0] = year;
        dateMembers[1] = month;
        dateMembers[2] = day;
    }

    //getter setter of time members
    private int[] getTimeMembers () { return timeMembers; }

    private void setTimeMembers (int hour, int min)
    {
        timeMembers[0] = hour;
        timeMembers[1] = min;
    }

    private String getFullDateString ()
    {
        int[] date = getDateMembers();
        int[] time = getTimeMembers();

        int year = date[0];
        int month = date[1];
        int day = date[2];

        int hour = time[0];
        int min = time[1];
        int sec = 00;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, min, sec);
        Date d = c.getTime();

        dM = new DateManager();
        String dateString = dM.getFullStrfromDate(d);
        return dateString;

    }

    protected Fragment loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.newEntryContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return fragment;
    }

    //set up mood selection view
    private void setUpAdapter() {
        moodDataSource = new MoodDataSource(getContext());
        moodDataSource.open();
        moodList = moodDataSource.getAllMoods();
        moodDataSource.close();
    }

}

