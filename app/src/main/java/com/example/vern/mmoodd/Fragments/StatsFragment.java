package com.example.vern.mmoodd.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.vern.mmoodd.Adapters.MonthInPixelsAdapter;
import com.example.vern.mmoodd.Adapters.MonthInPixelsDaysAdapter;
import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.R;
import java.util.List;

public class StatsFragment extends Fragment {

    private EntryDataSource entryDataSource;
    private List<Entry> entryList;

    private MonthInPixelsAdapter adapter;
    private MonthInPixelsDaysAdapter adapterDays;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_fragment, container, false);

//        GRAPH CODES
//        String monthYear = getArguments().getString("date");
//
//        moodDataSource = new MoodDataSource(getContext());
//        entryDataSource = new EntryDataSource(getContext());
//
//        moodDataSource.open();
//        moodList = moodDataSource.getAllMoods();
//        moodDataSource.close();
//
//        entryDataSource.open();
//
//        String moodNamesList[] = new String[moodList.size()];
//        List <Entry> entries = entryDataSource.getEntriesByMonthYear(monthYear, "ASC");
//
//        entryDataSource.close();
//
//        List<DataPoint> dataPointList = new ArrayList<>();
//
//
//        for(Entry e : entries)
//        {
//            dataPointList.add(new DataPoint(e.getDayofMonth(), e.getMoodID() + 1));
//        }
//
//
//        DataPoint dataPoint[] = new DataPoint[dataPointList.size()];
//        for (int j = 0; j < dataPointList.size(); j++)
//        {
//            dataPoint[j] = dataPointList.get(j);
//        }
//
//        GraphView graph = view.findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint> (dataPoint);
//
//        graph.getGridLabelRenderer().setNumVerticalLabels(moodList.size());
//
//        graph.getViewport().setMinX(1);
//        graph.getViewport().setMaxX(31);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//
//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
////        staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});
//        staticLabelsFormatter.setVer
//        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//
//        graph.addSeries(series);

        final String dateSelected = getArguments().getString("date");

        //set up month in pixels view
        setUpAdapter();

        GridView gridViewCal = view.findViewById(R.id.gridViewPixel);
        adapter = new MonthInPixelsAdapter(dateSelected, entryList, getContext());
        gridViewCal.setAdapter(adapter);

        GridView gridViewWeekdays = view.findViewById(R.id.gridViewWeekNames);
        adapterDays = new MonthInPixelsDaysAdapter(getContext());
        gridViewWeekdays.setAdapter(adapterDays);

        return view;
    }

    private void setUpAdapter() {
        entryDataSource = new EntryDataSource(getContext());
        entryDataSource.open();
        final String dateSelected = getArguments().getString("date");
        entryList = entryDataSource.getEntriesByMonthYear(dateSelected, "ASC");
        entryDataSource.close();
    }


}
