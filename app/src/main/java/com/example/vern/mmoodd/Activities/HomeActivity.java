package com.example.vern.mmoodd.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Fragments.EntryListFragment;
import com.example.vern.mmoodd.Fragments.MoreOptFragment;
import com.example.vern.mmoodd.Fragments.StatsFragment;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.MonthYearPickerDialog;
import com.example.vern.mmoodd.R;

import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private TextView mDateHeader;
    private ImageButton mArrow_Back;
    private ImageButton mArrow_Forward;

    private ActionBar toolbar;

    private RelativeLayout relFab;
    private RelativeLayout relDarkOut;
    private RelativeLayout relMask;

    private FloatingActionButton fab_add, fab_today, fab_yesterday, fab_otherDay;
    private Animation fabOpen, fabClose, fabClockwise, fabAntiClockwise, fadeIn, fadeOut;
    private TextView todayText, yesterdayText, otherDayText;
    boolean isOpen = false;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private DateManager dM = new DateManager();
    private AlertDialog.Builder builder;
    private DatePickerDialog datePickerDialog;

    private Bundle bundle;

    private BottomNavigationView bottomNavigationView;

    MoodDataSource moodDataSource;
    EntryDataSource entryDataSource;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar);

        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogTheme));

        mDateHeader = findViewById(R.id.dateText);
        mDateHeader.setText(dM.getMonthYearNow());

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet (DatePicker view, int year, int month, int useless) {
                String realMonthYear = dM.getMonthYearNow();
                String realAfterDate = dM.getMonthYearAfter(realMonthYear);

                String pickedDate = dM.getPickedDatefromDialog(month,year);

                if (pickedDate.equals(realAfterDate)) {
                    String message = pickedDate + " is in the future\n（・□・；）"
                            + "\n\nWe can't view into the future, right? ᕕ( ᐛ )ᕗ"
                            +"\n\nPlease choose other dates.";
                    builder.setTitle("Oh no...")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show();


                    final AlertDialog alertDialog = builder.create();
                }
                else {
                    mDateHeader.setText(pickedDate);

                    if(pickedDate.equals(realMonthYear))
                    {
                        mArrow_Forward.setBackgroundTintList(getApplication().getResources().getColorStateList(R.color.purpleGrey));
                    }
                    else
                    {
                        mArrow_Forward.setBackgroundTintList(getApplication().getResources().getColorStateList(R.color.darkIndigo));
                    }

                    runFragment();
                }
            }

        };

        mDateHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                String dateMembers[] = mDateHeader.getText().toString().split(" ");
                int month = dM.getMonthNum(dateMembers[0]);
                int year = Integer.parseInt(dateMembers[1]);
                pd.setMonthYearValue(month, year);

                pd.setListener(dateSetListener);
                pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");

            }
        });

        //set up header view
        mArrow_Forward = findViewById(R.id.arrow_forward);
        mArrow_Back = findViewById(R.id.arrow_back);

        mArrow_Forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String date = mDateHeader.getText().toString();
                String currentAfterDate = dM.getMonthYearAfter(date);

                String realMonthYear = dM.getMonthYearNow();

                if(!date.equals(realMonthYear))
                {
                    if(realMonthYear.equals(currentAfterDate))
                    {
                        mArrow_Forward.setBackgroundTintList(getApplication().getResources().getColorStateList(R.color.purpleGrey));
                    }
                    mDateHeader.setText(currentAfterDate);

                    runFragment();
                }
                else
                {
                    mArrow_Forward.setBackgroundTintList(getApplication().getResources().getColorStateList(R.color.purpleGrey));
                }
            }
        });

        mArrow_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String date = mDateHeader.getText().toString();

                String previousDate  = dM.getMonthYearPrevious(date);
                Bundle bundle = new Bundle();
                bundle.putString("date", previousDate);

                mDateHeader.setText(previousDate);

                String currentMonthYear = dM.getMonthYearNow();

                if(!mDateHeader.getText().toString().equals(currentMonthYear))
                {
                    mArrow_Forward.setBackgroundTintList(getApplication().getResources().getColorStateList(R.color.darkIndigo));

                }

                runFragment();
            }
        });

        //set up bottom navigation view
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.bottomNavView_Bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        runFragment();

        //set up floating action button view
        relFab = findViewById(R.id.relFab);
        relDarkOut = findViewById(R.id.relDarkOut);
        relMask = findViewById(R.id.masking);

        createFabView();

        relDarkOut.bringToFront();
        relFab.bringToFront();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
            int selectedItemId = bottomNavigationView.getSelectedItemId();

            switch (item.getItemId()) {
                case R.id.btm_nav_entry:
                    if(selectedItemId != item.getItemId())
                    {
                        fragment = new EntryListFragment();

                        bundle = new Bundle();
                        bundle.putString("date", mDateHeader.getText().toString());
                        fragment.setArguments(bundle);

                        loadFragment(fragment);
                    }

                    return true;

                case R.id.btm_nav_stats:
                    if(selectedItemId != item.getItemId()) {
                        fragment = new StatsFragment();

                        bundle = new Bundle();
                        bundle.putString("date", mDateHeader.getText().toString());
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                    }
                    return true;

                case R.id.btm_nav_moreOpt:

                    if(selectedItemId != item.getItemId()) {
                        fragment = new MoreOptFragment();

                        loadFragment(fragment);
                        return true;
                    }
            }

            return false;
        }
    };


    private void runFragment()
    {
        mDateHeader = findViewById(R.id.dateText);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        bundle = new Bundle();
        bundle.putString("date", mDateHeader.getText().toString());

        int selectedItemId = bottomNavigationView.getSelectedItemId();

        switch (selectedItemId)
        {
            case R.id.btm_nav_entry:
                EntryListFragment previousEntries = new EntryListFragment();
                previousEntries.setArguments(bundle);
                loadFragment(previousEntries);

                break;

            case R.id.btm_nav_stats:
                StatsFragment statsFragment = new StatsFragment();
                statsFragment.setArguments(bundle);
                loadFragment(statsFragment);

                break;

            case R.id.btm_nav_moreOpt:
                MoreOptFragment moreOptFragment = new MoreOptFragment();
                moreOptFragment.setArguments(bundle);
                loadFragment(moreOptFragment);
        }
    }

    private void loadFragment (Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createFabView() {
        final Context context = this;

        relDarkOut = findViewById(R.id.relDarkOut);
        relMask = findViewById(R.id.masking);

        fab_add = findViewById(R.id.addEntryFab);
        fab_today = findViewById(R.id.todayFab);
        fab_yesterday = findViewById(R.id.yesterdayFab);
        fab_otherDay = findViewById(R.id.otherDayFab);

        todayText = findViewById(R.id.today_text);
        yesterdayText = findViewById(R.id.yesterday_text);
        otherDayText = findViewById(R.id.other_day_text);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);

        //Animation
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_clockwise);
        fabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_anticlockwise);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        relDarkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_today.startAnimation(fabClose);
                todayText.startAnimation(fabClose);

                fab_yesterday.startAnimation(fabClose);
                yesterdayText.startAnimation(fabClose);

                fab_otherDay.startAnimation(fabClose);
                otherDayText.startAnimation(fabClose);

                fab_add.startAnimation(fabAntiClockwise);

                fab_today.setClickable(false);
                fab_yesterday.setClickable(false);
                fab_otherDay.setClickable(false);

                relDarkOut.startAnimation(fadeOut);
                relDarkOut.setVisibility(View.INVISIBLE);
                relDarkOut.setClickable(false);

                relMask.setVisibility(View.GONE);

                isOpen = false;
            }


        });

        fab_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                if(isOpen){
                    fab_today.startAnimation(fabClose);
                    todayText.startAnimation(fabClose);

                    fab_yesterday.startAnimation(fabClose);
                    yesterdayText.startAnimation(fabClose);

                    fab_otherDay.startAnimation(fabClose);
                    otherDayText.startAnimation(fabClose);

                    fab_add.startAnimation(fabAntiClockwise);

                    fab_today.setClickable(false);
                    fab_yesterday.setClickable(false);
                    fab_otherDay.setClickable(false);

                    relDarkOut.startAnimation(fadeOut);
                    relDarkOut.setVisibility(View.INVISIBLE);
                    relDarkOut.setClickable(false);

                    relMask.setVisibility(View.GONE);
                    relMask.setClickable(true);
                    isOpen = false;

                }
                else {
                    fab_today.startAnimation(fabOpen);
                    todayText.startAnimation(fabOpen);

                    fab_yesterday.startAnimation(fabOpen);
                    yesterdayText.startAnimation(fabOpen);

                    fab_otherDay.startAnimation(fabOpen);
                    otherDayText.startAnimation(fabOpen);

                    fab_add.startAnimation(fabClockwise);

                    fab_today.setClickable(true);
                    fab_yesterday.setClickable(true);
                    fab_otherDay.setClickable(true);

                    relDarkOut.setAnimation(fadeIn);
                    relDarkOut.setVisibility(View.VISIBLE);
                    relDarkOut.setClickable(true);

                    relMask.setVisibility(View.VISIBLE);
                    relMask.setClickable(false);

                    isOpen = true;
                }
            }
        });

        fab_today.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, CreateNewEntryActivity.class);
                Date date = new Date();
                intent.putExtra("date", date.toString());
                startActivity(intent);

            }
        });

        fab_yesterday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {

                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -1);
                Date date = c.getTime();

                Intent intent = new Intent(context, CreateNewEntryActivity.class);
                intent.putExtra("date", date.toString());
                startActivity(intent);

            }
        });

        fab_otherDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();

            }
        });


    }

    private void setDateTimeField() {
        final Calendar c = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                R.style.DialogTheme, new DatePickerDialog.OnDateSetListener(){

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

                Date date = c.getTime();
                Intent intent = new Intent(getApplicationContext(), CreateNewEntryActivity.class);
                intent.putExtra("date", date.toString());
                startActivity(intent);
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    protected void onResume() {
        if(moodDataSource != null)
        {
            moodDataSource.open();
        }

        if(entryDataSource != null)
        {
            entryDataSource.open();
        }

        super.onResume();
    }

    protected void onPause() {
        if(moodDataSource != null)
        {
            moodDataSource.close();
        }

        if(entryDataSource != null)
        {
            entryDataSource.close();
        }
        super.onPause();
    }

}
