package com.example.vern.mmoodd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vern.mmoodd.R;

import java.text.DateFormatSymbols;

public class MonthInPixelsDaysAdapter extends BaseAdapter {

    Context context;
    private View gridView;

    private String[] weekDays;


    public MonthInPixelsDaysAdapter(Context c)
    {
        context = c;

        //get names of days of week
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] days = dfs.getShortWeekdays();

        weekDays = new String[7];

        for(int i = 0; i < days.length; i++) {
            if (i != 0) {
                weekDays[i-1] = days[i];
            }
        }
    }

    @Override
    public int getCount() {
        return weekDays.length;
    }

    @Override
    public Object getItem(int position) {
        return weekDays[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout daysLayout;

        gridView = new View (context);
        gridView = inflater.inflate(R.layout.single_day, null);

        daysLayout =  gridView.findViewById(R.id.daysLayout);
        TextView dayText = gridView.findViewById(R.id.weekDay);

        dayText.setText(weekDays[position]);
        return daysLayout;
    }
}
