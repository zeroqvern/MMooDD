package com.example.vern.mmoodd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Datasource.ImgPathDataSource;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Fragments.SelectMoodFragment;
import com.example.vern.mmoodd.R;

public class CreateNewEntryActivity extends AppCompatActivity {

    MoodDataSource moodDataSource;
    EntryDataSource entryDataSource;
    ImgPathDataSource imgPathDataSource;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_entry_master);

        //set date value to bundle
        String dateSelected = getIntent().getStringExtra("date");
        Bundle bundle=new Bundle();
        bundle.putString("date", dateSelected);

        //set bundle to fragment
        SelectMoodFragment moodFragment = new SelectMoodFragment();
        moodFragment.setArguments(bundle);

        //datasources
        entryDataSource = new EntryDataSource(this);
        moodDataSource = new MoodDataSource(this);
        imgPathDataSource = new ImgPathDataSource(this);

        loadFragment(moodFragment);
    }


    protected Fragment loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.newEntryContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return fragment;
    }

    protected void onResume() {
        moodDataSource.open();
        entryDataSource.open();
        imgPathDataSource.open();
        super.onResume();
    }

    protected void onPause() {
        moodDataSource.close();
        entryDataSource.close();
        imgPathDataSource.close();
        super.onPause();
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to stop creating entry", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
