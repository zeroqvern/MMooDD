package com.example.vern.mmoodd.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vern.mmoodd.R;

public class MoreOptFragment extends Fragment {
    private static final String Tag = "EntryFrag";

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more_options_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.moreOptFrag);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick (View view){
                Toast.makeText(getActivity(), "Opt clicked!", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        return view;
    }

}
