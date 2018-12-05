package com.example.vern.mmoodd_ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vern.mmoodd.Activities.EntryDetailsActivity;
import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.Date;
import java.util.List;

public class EntryListFragment extends Fragment {
    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mAdapter;

    private EntryDataSource entryDataSource;
    private MoodDataSource moodDataSource;

    private boolean emptyEntries = false;

    private DateManager dM;
    private Date dateNow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_list_recycler_view, container, false);

        entryDataSource = new EntryDataSource(getContext());
        moodDataSource = new MoodDataSource((getContext()));

        entryDataSource.open();
        moodDataSource.open();

        mEntryRecyclerView = view.findViewById(R.id.entry_recycler_view);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyEntries = updateUI();
        if(emptyEntries)
        {
            view = inflater.inflate(R.layout.no_entry_view, container, false);
        }

        entryDataSource.close();
        moodDataSource.close();

        return view;
    }

    private boolean updateUI() {

        dM = new DateManager();
        String monthYear = getArguments().getString("date");

        List<Entry> entryList = entryDataSource.getEntriesByMonthYear(monthYear, "DSC");
        if(entryList.size() == 0)
        {
            return true;
        }
        else
        {
            mAdapter = new EntryAdapter(entryList);
            mEntryRecyclerView.setAdapter(mAdapter);
            return false;
        }
    }


    private class EntryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mood;
        private TextView moodText;
        private TextView moodTitle;
        private TextView dayDate, monthDate, timeDate;
        private Entry entry;
        private RelativeLayout entryItem;
        private DateManager dM = new DateManager();

        public EntryHolder (View itemView) {
            super (itemView);
            itemView.setOnClickListener(this);

            mood = itemView.findViewById(R.id.entry_mood);
            moodText = itemView.findViewById(R.id.mood_text);
            moodTitle = itemView.findViewById(R.id.entryList_title);
            dayDate = itemView.findViewById(R.id.dayDate);
            monthDate = itemView.findViewById(R.id.monthDate);
            timeDate = itemView.findViewById(R.id.timeDate);
            entryItem = itemView.findViewById(R.id.entryItemLayout);
        }

        public void bindEntry (Entry entry) {
            this.entry = entry;

            moodTitle.setText(entry.getTitle());
            String month = dM.getMonth(entry.getDatetime()).toUpperCase();

            monthDate.setText(month);
            String day = dM.getDay(entry.getDatetime());

            dayDate.setText(day);
            String time = dM.getTime(entry.getDatetime());

            timeDate.setText(time);

            int moodNum = entry.getMoodID();

            moodDataSource.open();
            Mood selectedMood = moodDataSource.getMoodByID(moodNum);
            moodDataSource.close();

            int colorID = selectedMood.getColorResID();

            moodText.setText(selectedMood.getMoodName());
            moodText.setTextColor(getResources().getColor(colorID));
            mood.setBackgroundResource(selectedMood.getMoodResID());
            mood.setBackgroundTintList(getActivity().getResources().getColorStateList(colorID));
            entryItem.setBackgroundTintList(getActivity().getResources().getColorStateList(colorID));

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(),EntryDetailsActivity.class);
            intent.putExtra("selectedEntryID", entry.getEntryID());
            startActivity(intent);
        }
    }


    private class EntryAdapter extends  RecyclerView.Adapter<EntryHolder> {
        private List<Entry> mEntries;

        public  EntryAdapter(List<Entry> Entries) {
            mEntries = Entries;
        }

        @Override
        public EntryHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.single_entry_view, parent, false);
            return new EntryHolder(view);
        }

        @Override
        public void onBindViewHolder (EntryHolder holder, int position) {
            Entry entry = mEntries.get(position);
            holder.bindEntry(entry);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }
    }

    @Override
    public void onPause()
    {
        entryDataSource.close();
        moodDataSource.close();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        entryDataSource.open();
        moodDataSource.open();
        super.onResume();
    }

}