package com.example.vern.mmoodd.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vern.mmoodd.Activities.HomeActivity;
import com.example.vern.mmoodd.Adapters.MoodGridViewAdapter;
import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Datasource.ImgPathDataSource;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntryEditModeFragment extends Fragment{

    private Entry entry;
    private Mood mood;

    private EditText titleText;
    private EditText contentText;

    private List<Mood> moodList;
    private ImageButton moodBtn;
    private TextView moodText;
    private MoodGridViewAdapter adapter;
    private int selectedMoodPosition = -1;

    private DateManager dM;
    private Date date;
    private TextView timeText;

    private TextView numAttach;
    private Button viewImageBtn;

    private Button doneBtn;
    private Button cancelBtn;

    private EntryDataSource entryDataSource;
    private MoodDataSource moodDataSource;
    private ImgPathDataSource imgPathDataSource;

    private List<Uri> uriList = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();

    private AlertDialog.Builder builder;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.entry_edit_mode,container,false);


        builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),R.style.DialogTheme));
        entryDataSource = new EntryDataSource(getContext());
        moodDataSource = new MoodDataSource(getContext());
        imgPathDataSource = new ImgPathDataSource(getContext());
        dM = new DateManager();

        //get entry by ID
        entryDataSource.open();
        int id = getArguments().getInt("entryID");
        Entry e = entryDataSource.getEntryByID(id);
        setSelectedEntry(e);
        entryDataSource.close();

        //setup text view
        date = e.getDatetime();
        timeText = view.findViewById(R.id.timeText);
        timeText.setText(dM.getTime(date));

        titleText = view.findViewById(R.id.entryTitleInput);
        contentText = view.findViewById(R.id.entryContentInput);

        titleText.setText(e.getTitle());
        contentText.setText(e.getContent());

        //set up mood view
        moodText = view.findViewById(R.id.mood_text);
        moodBtn = view.findViewById(R.id.mood_btn);

        int moodID = e.getMoodID();

        moodDataSource.open();
        mood = moodDataSource.getMoodByID(moodID);
        moodText.setText(mood.getMoodName());
        moodText.setTextColor(ContextCompat.getColor(getContext(), mood.getColorResID()));
        moodBtn.setBackgroundTintList(getContext().getResources().getColorStateList(mood.getColorResID()));
        moodDataSource.close();

        moodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoodDialog();
            }
        });

        imgPathDataSource.open();
        imagePaths = new ArrayList<>();
        imagePaths = imgPathDataSource.getImgPathsByEntry(id);
        entry.setImgPath(imagePaths);
        imgPathDataSource.close();

        //setting up gallery view
        numAttach = view.findViewById(R.id.numAttach);
        imagePaths = e.getImgPaths();
        if(imagePaths != null)
        {
            for (String s : imagePaths)
            {
                Uri uri = Uri.parse(s);
                uriList.add(uri);
            }
        }

        String num = e.getImgPaths().size() + "";
        numAttach.setText(num);

        //set up view images view
        viewImageBtn = view.findViewById(R.id.viewImage_Btn);
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

        //set up done button
        doneBtn = view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry e = getSelectedEntry();

                e.setTitle(titleText.getText().toString());
                e.setContent(contentText.getText().toString());

                if(getSelectedMoodPosition() != -1)
                {
                    e.setMoodID(getSelectedMoodPosition() + 1);
                }

                if(e.getTitle() == null || e.getTitle().isEmpty() || e.getTitle().trim().length() == 0)
                {
                    String message = "Please enter a title.";
                    builder.setTitle("Error!")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show();

                    final AlertDialog alertDialog = builder.create();
                }

                else
                {

                    entryDataSource.open();
                    entryDataSource.editEntry(e);
                    entryDataSource.close();


                    Toast.makeText(getContext(),"Entry successfully edited!", Toast.LENGTH_SHORT)
                            .show();

                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

        //set up cancel button
        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Are you sure you want to stop editing your entry?";
                builder.setTitle("Cancel")
                        .setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                final AlertDialog alertDialog = builder.create();
            }
        });

        return view;
    }


    //getter setter of selectedMoodPosition
    private int getSelectedMoodPosition () { return selectedMoodPosition;}

    private void setSelectedMoodPosition (int position)
    {
        selectedMoodPosition = position;
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


    //set up mood selection dialog
    private void showMoodDialog() {

        final PopupWindow popup;

        final View popUpView = getLayoutInflater().inflate(R.layout.mood_only_grid_view,
                null,false);

        setUpMoodAdapter();

        GridView gridView = popUpView.findViewById(R.id.gridViewMood);
        adapter = new MoodGridViewAdapter(moodList, getContext());
        gridView.setAdapter(adapter);
        popup = new PopupWindow();

        popup.setContentView(popUpView);
        popup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

        View parentLayout = getActivity().findViewById(R.id.entryEditModeParent);
        moodText =  parentLayout.findViewById(R.id.mood_text);
        moodBtn = parentLayout.findViewById(R.id.mood_btn);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.setSelectedPosition(position);
                setSelectedMoodPosition(position);
                adapter.notifyDataSetChanged();

                moodDataSource = new MoodDataSource(getContext());

                moodDataSource.open();
                mood = moodDataSource.getMoodByID(position + 1);
                moodDataSource.close();

                moodText.setText(mood.getMoodName());
                moodText.setTextColor(ContextCompat.getColor(getContext(), mood.getColorResID()));
                moodBtn.setBackgroundTintList(getContext().getResources()
                        .getColorStateList(mood.getColorResID()));

                popup.dismiss();
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
        return;
    }

    private void setUpMoodAdapter() {
        moodDataSource = new MoodDataSource(getContext());
        moodDataSource.open();
        moodList = moodDataSource.getAllMoods();
        moodDataSource.close();
    }


    protected Fragment loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in_full_alpha, R.anim.fade_out_full_alpha);
        transaction.replace(R.id.relLayoutEntry, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return fragment;
    }

}
