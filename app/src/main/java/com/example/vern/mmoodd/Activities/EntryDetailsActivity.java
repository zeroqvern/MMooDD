package com.example.vern.mmoodd.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Datasource.ImgPathDataSource;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Fragments.EntryEditModeFragment;
import com.example.vern.mmoodd.Fragments.GalleryFragment;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.ArrayList;
import java.util.List;

public class EntryDetailsActivity extends AppCompatActivity {

    private Entry entry;
    private Mood mood;

    private TextView dateHeader;
    private TextView timeText;

    private DateManager dM;
    private TextView titleText;
    private TextView contentText;

    private ImageButton backBtn;

    private Button viewImageBtn;
    private TextView numAttach;
    private List<String> imagePaths;

    private ImageButton moodBtn;
    private TextView moodText;

    private EntryDataSource entryDataSource;
    private MoodDataSource moodDataSource;
    private ImgPathDataSource imgPathDataSource;

    private AlertDialog.Builder builder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_details);

        //datasource
        entryDataSource = new EntryDataSource(getApplicationContext());
        imgPathDataSource = new ImgPathDataSource(getApplicationContext());

        entryDataSource.open();
        int entryID = getIntent().getIntExtra("selectedEntryID", 0);
        setSelectedEntry(entryDataSource.getEntryByID(entryID));
        entry = getSelectedEntry();
        entryDataSource.close();

        imgPathDataSource.open();
        imagePaths = new ArrayList<>();
        imagePaths = imgPathDataSource.getImgPathsByEntry(entryID);
        entry.setImgPath(imagePaths);
        imgPathDataSource.close();


        //set up dateTime view
        dM = new DateManager();

        dateHeader = findViewById(R.id.dateTextEntry);
        String date = dM.getDateStrGiven(entry.getDatetime());
        dateHeader.setText(date);

        timeText = findViewById(R.id.timeText);
        String time = dM.getTime(entry.getDatetime());
        timeText.setText(time);

        //set up texts view
        titleText = findViewById(R.id.entryTitleInput);
        contentText = findViewById(R.id.entryContentInput);

        titleText.setText(entry.getTitle());
        contentText.setText(entry.getContent());

        //set up mood view
        moodBtn = findViewById(R.id.mood_btn);
        moodText = findViewById(R.id.mood_text);

        moodDataSource = new MoodDataSource(this);
        moodDataSource.open();

        mood = moodDataSource.getMoodByID(entry.getMoodID());
        moodText.setText(mood.getMoodName());
        moodText.setTextColor(ContextCompat.getColor(this, mood.getColorResID()));
        moodBtn.setBackgroundTintList(this.getResources().getColorStateList(mood.getColorResID()));

        moodDataSource.close();

        //set up gallery view
        numAttach = findViewById(R.id.numAttach);
        String num = entry.getImgPaths().size() + "";
        numAttach.setText(num);
        viewImageBtn = findViewById(R.id.viewImage_Btn);
        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePaths = new ArrayList<>();
                imagePaths = entry.getImgPaths();
                ArrayList<String> imageArrList = new ArrayList<>();
                imageArrList.addAll(imagePaths);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("uriList", imageArrList);

                GalleryFragment fragment = new GalleryFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);

            }
        });

        //set up back btn
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBacktoHome();
            }
        });

    }

    //getter setter of selectedEntry
    private Entry getSelectedEntry ()
    {
        return entry;
    }

    private void setSelectedEntry (Entry e)
    {
        entry = e;
    }

    //setup menu selection pop up
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.edit_entry_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.toString().toUpperCase()) {
                    case "EDIT":
                        Bundle bundle = new Bundle();
                        int id = getSelectedEntry().getEntryID();
                        bundle.putInt("entryID", id);
                        EntryEditModeFragment fragment = new EntryEditModeFragment();
                        fragment.setArguments(bundle);
                        loadFragment(fragment);

                        break;
                    case "DELETE":
                        runDeleteMode();
                        break;
                }
                return false;
            }
        });
    }


    //setup delete entry
    private void runDeleteMode()
    {
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogTheme));
        String message = "Are you sure you want to delete this entry?";
        builder.setTitle("Delete?")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //positive response
                        entryDataSource = new EntryDataSource(getApplicationContext());
                        entryDataSource.open();
                        Entry e = getSelectedEntry();
                        entryDataSource.deleteEntry(e.getEntryID());
                        entryDataSource.close();

                        if(e.getImgPaths() != null) {
                            if(e.getImgPaths().size() != 0)
                            {
                                imgPathDataSource = new ImgPathDataSource(getApplicationContext());
                                imgPathDataSource.open();
                                imgPathDataSource.deleteImgPath(e);
                                imgPathDataSource.close();
                            }
                        }

                        Toast.makeText(getApplicationContext(), "Entry successfully deleted!", Toast.LENGTH_SHORT).show();

                        goBacktoHome();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        final AlertDialog alertDialog = builder.create();
    }

    protected Fragment loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = EntryDetailsActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in_full_alpha, R.anim.fade_out_full_alpha);
        transaction.replace(R.id.relLayoutEntry, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return fragment;
    }

    protected void onResume() {
        entryDataSource.open();
        moodDataSource.open();
        imgPathDataSource.open();
        super.onResume();
    }

    protected void onPause() {
        entryDataSource.close();
        moodDataSource.close();
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
        Toast.makeText(this, "Please click BACK again to stop editing entry", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void goBacktoHome()
    {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

