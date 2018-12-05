package com.example.vern.mmoodd.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEntryFragment extends Fragment {

    private Entry entry;
    private Mood mood;

    private int selectedMoodPosition = -1;
    private static final int PICK_FROM_GALLERY = 1;

    private TextView dateHeader;
    private EditText titleText;
    private EditText contentText;
    private List<Mood> moodList;
    private ImageButton moodBtn;
    private TextView moodText;
    private TextView timeText;

    private ImageButton galleryBtn;
    private TextView numAttach;
    private Button viewImageBtn;

    private Button doneBtn;
    private Button cancelBtn;

    private Date date;

    private EntryDataSource entryDataSource;
    private MoodDataSource moodDataSource;
    private ImgPathDataSource imgPathDataSource;

    private List<Uri> uriList = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();

    private DateManager dM;

    private AlertDialog.Builder builder;
    private MoodGridViewAdapter adapter;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_entry,container,false);


        builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),R.style.DialogTheme));

        entryDataSource = new EntryDataSource(getContext());
        moodDataSource = new MoodDataSource(getContext());
        imgPathDataSource = new ImgPathDataSource(getContext());

        //set dateTime view
        dM = new DateManager();
        dateHeader = view.findViewById(R.id.dateTextEntry);
        final String dateSelected = getArguments().getString("date");

        date = dM.getDatefromStr(dateSelected);
        dateHeader.setText(dM.getDateStr(date));

        timeText = view.findViewById(R.id.timeText);
        timeText.setText(dM.getTime(date));

        //set text view
        titleText = view.findViewById(R.id.entryTitleInput);
        contentText = view.findViewById(R.id.entryContentInput);

        //set gallery view
        galleryBtn = view.findViewById(R.id.gallery_btn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PICK_FROM_GALLERY);
                    }
                    else
                    {
                        openGallery();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        numAttach = view.findViewById(R.id.numAttach);
        String num = uriList.size() + "";
        numAttach.setText(num);

        //set mood view
        moodText = view.findViewById(R.id.mood_text);
        moodBtn = view.findViewById(R.id.mood_btn);
        int moodID = getArguments().getInt("moodPosition");

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


        //set view images button
        viewImageBtn = view.findViewById(R.id.viewImage_Btn);
        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> imageArrList = new ArrayList<>();
                imageArrList.addAll(imagePaths);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("uriList", imageArrList);

                GalleryFragment fragment = new GalleryFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);

            }
        });

        //set done button
        doneBtn = view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry = new Entry();
                entry.setTitle(titleText.getText().toString());
                entry.setContent(contentText.getText().toString());
                try {
                    entry.setDatetimeStr(dateSelected);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //get selected mood position
                if(getSelectedMoodPosition() != -1)
                {
                    entry.setMoodID(getSelectedMoodPosition() + 1);
                }
                else
                {
                    int moodID = getArguments().getInt("moodPosition");
                    entry.setMoodID(moodID);
                }

                //check if title is filled before creating entry
                if(entry.getTitle() == null || entry.getTitle().isEmpty()
                        || entry.getTitle().trim().length() == 0)
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
                    int id = entryDataSource.createEntry(entry);
                    entryDataSource.close();

                    entry.setEntryID(id);

                    if(imagePaths.size() != 0)
                    {
                        entry.setImgPath(imagePaths);
                        imgPathDataSource.open();
                        imgPathDataSource.insertImgPaths(entry);
                        imgPathDataSource.close();
                    }

                    Toast.makeText(getContext(),"Entry successfully added!", Toast.LENGTH_SHORT)
                            .show();

                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

        //set cancel button
        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Are you sure you want to stop writing your entry?";
                builder.setTitle("Cancel")
                        .setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                final AlertDialog alertDialog = builder.create();
            }
        });

        return view;
    }

    //set up gallery view
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_FROM_GALLERY);
    }

    //get selected image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        View v = getActivity().findViewById(R.id.createEntryParent);

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK && requestCode == PICK_FROM_GALLERY)
        {
            Uri imageUri = data.getData();

            imagePaths.add(imageUri.toString());
            uriList.add(imageUri);

            numAttach = v.findViewById(R.id.numAttach);
            String num = uriList.size() + "";
            numAttach.setText(num);
        }
    }


    //getter setter of selected mood position
    private int getSelectedMoodPosition () { return selectedMoodPosition;}

    private void setSelectedMoodPosition (int position)
    {
        selectedMoodPosition = position;
    }


    //set up mood selection pop up
    private void showMoodDialog() {

        final PopupWindow popup;

        final View popUpView = getLayoutInflater().inflate(R.layout.mood_only_grid_view, null,false);

        setUpMoodAdapter();

        GridView gridView = popUpView.findViewById(R.id.gridViewMood);
        adapter = new MoodGridViewAdapter(moodList, getContext());
        gridView.setAdapter(adapter);
        popup = new PopupWindow(); // here 400 and 500 is the height and width of layout

        popup.setContentView(popUpView);
        popup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

        ViewGroup parentLayout = ((ViewGroup) getView().getParent());
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
                moodBtn.setBackgroundTintList(getContext().getResources().getColorStateList(mood.getColorResID()));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                View view = getActivity().findViewById(R.id.createEntryParent);
                numAttach = view.findViewById(R.id.numAttach);

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();

                    numAttach.setText(uriList.size() + "");
                }
                break;
        }
    }

    protected Fragment loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.newEntryContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return fragment;
    }

}
