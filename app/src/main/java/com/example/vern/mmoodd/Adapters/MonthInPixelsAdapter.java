package com.example.vern.mmoodd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.vern.mmoodd.Datasource.MoodDataSource;
import com.example.vern.mmoodd.Model.DateManager;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.Model.Mood;
import com.example.vern.mmoodd.R;

import java.util.Date;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class MonthInPixelsAdapter extends BaseAdapter {

    List<Entry> entryList;
    Context context;

    private View gridView;

    private MoodDataSource moodDataSource;

    private String month;
    private int numDays;

    private Entry[] entries;
    private DateManager dM;
    private int addSpace;

    public MonthInPixelsAdapter(String m, List<Entry> entryList, Context mContext)
    {
        month = m;
        String date[] = m.split(" ");

        this.entryList = entryList;
        this.context = mContext;

        //get month number
        dM = new DateManager();
        int month = dM.getMonthNum(date[0]);

        //set date
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(date[1]));
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);

        //get maximum days of the month
        numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        //set date and get day of week
        Date d = c.getTime();
        String dayName = dM.getDayofWeek(d);

        //get names of days of week
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weekdays = dfs.getShortWeekdays();

        //add space in front of the array
        //to determine which day of the week
        //is the first day of the month
        addSpace = 0;
        for(String w : weekdays) {
            if (!w.equals(dayName)) {
                addSpace++;
            }
            else
            {
                addSpace--;
                break;
            }
        }
        entries = new Entry[numDays + addSpace];

        //set entries array
        for (Entry e : entryList)
        {
            int position = e.getDayofMonth() - 1;
            entries[position + addSpace] = e;
        }
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return entries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        gridView = new View (context);
        gridView = inflater.inflate(R.layout.single_pixel, null);

        LinearLayout pixelLayout;
        pixelLayout =  gridView.findViewById(R.id.pixelLayout);
        ImageView pixel = gridView.findViewById(R.id.pixel);

        if(position < addSpace)
        {
            pixel.setBackgroundTintList
                    (context.getResources().getColorStateList(R.color.white));
        }
        else {
            if (entries != null) {
                Entry selectedEntry = entries[position];

                if (selectedEntry == null) {
                    pixel.setBackgroundTintList
                            (context.getResources().getColorStateList(R.color.lightPurpleGrey));
                }
                else {
                    moodDataSource = new MoodDataSource(context);

                    moodDataSource.open();
                    Mood mood = moodDataSource.getMoodByID(selectedEntry.getMoodID());
                    moodDataSource.close();

                    pixel.setBackgroundTintList
                            (context.getResources().getColorStateList(mood.getColorResID()));
                }

            }
            else
            {
                pixel.setBackgroundTintList
                        (context.getResources().getColorStateList(R.color.purpleGrey));
            }
        }
        return pixelLayout;
    }
}
