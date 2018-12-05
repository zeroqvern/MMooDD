package com.example.vern.mmoodd.Fragments;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vern.mmoodd.Adapters.GalleryAdapter;
import com.example.vern.mmoodd.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private List<Uri> uriList = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();
    private ArrayList<String> imageArrList = new ArrayList<>();

    private GridView galleryGrid;
    private ImageButton backBtn;

    private GalleryAdapter galleryAdapter;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gallery_grid_view_list, container, false);

        imageArrList = getArguments().getStringArrayList("uriList");
        imagePaths.addAll(imageArrList);

        galleryGrid = view.findViewById(R.id.gridviewGallery);


        if(imagePaths.size() != 0)
        {
            for(String s : imagePaths)
            {
                Uri uri = Uri.parse(s);
                uriList.add(uri);
            }

            galleryAdapter = new GalleryAdapter(uriList, getContext());
            galleryGrid.setAdapter(galleryAdapter);
        }

        //set up gallery item view
        galleryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });

        //set up back button
        backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
