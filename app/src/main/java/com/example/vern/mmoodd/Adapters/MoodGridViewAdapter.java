package com.example.vern.mmoodd.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.ArrayList;
import java.util.List;

public class MoodGridViewAdapter extends BaseAdapter{

    List<Mood> moodList;
    Context context;
    private int selectedPosition = -1;
    private View gridView;


    public MoodGridViewAdapter(List<Mood> moods, Context mContext)
    {
        this.moodList = moods;
        this.context = mContext;
    }

    @Override
    public int getCount() {
        return moodList.size();
    }

    @Override
    public Object getItem(int position) {
        return moodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedPosition() {return selectedPosition;}

    public void setSelectedPosition(int position)
    {
        selectedPosition=position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        final Mood selectedMood = moodList.get(position);
        gridView = new View (context);
        gridView = inflater.inflate(R.layout.single_mood, null);

        LinearLayout moodLayout =  gridView.findViewById(R.id.moodLayout);

        ImageView moodButton = gridView.findViewById(R.id.moodIcon);
        moodButton.setBackgroundResource(selectedMood.getMoodResID());
        moodButton.setBackgroundTintList
                (context.getResources().getColorStateList(selectedMood.getColorResID()));

        TextView moodText = gridView.findViewById(R.id.moodName);
        moodText.setText(selectedMood.getMoodName());
        moodText.setTextColor(ContextCompat.getColorStateList
                (context, selectedMood.getColorResID()));

        if(position == selectedPosition)
        {
            gridView.setSelected(true);
            moodButton.setBackgroundResource(selectedMood.getMoodNegID());
        }
        else
        {
            gridView.setSelected(false);
            moodButton.setBackgroundResource(selectedMood.getMoodResID());
        }

        return moodLayout;
    }

}
