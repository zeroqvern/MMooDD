package com.example.vern.mmoodd.Datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vern.mmoodd.Database.SQLiteHelper;
import com.example.vern.mmoodd.Model.Entry;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntryDataSource {
    //Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.KEY_ENTRYID,
            SQLiteHelper.KEY_DATETIME,
            SQLiteHelper.KEY_TITLE,
            SQLiteHelper.KEY_CONTENT,
            SQLiteHelper.KEY_MOODID,
    };
    private Context mContext;

    public EntryDataSource (Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close () {
        dbHelper.close();
    }

    //create entry
    public int createEntry (Entry entry) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_DATETIME, entry.getDatetime().toString());
        values.put(SQLiteHelper.KEY_TITLE, entry.getTitle());
        values.put(SQLiteHelper.KEY_CONTENT, entry.getContent());
        values.put(SQLiteHelper.KEY_MOODID, entry.getMoodID());

        long id = database.insert(SQLiteHelper.TABLE_ENTRY,null, values);

        //write mood
//        MoodActivityDataSource writeActivitesMood = new MoodActivityDataSource(mContext);
//        writeActivitesMood.insertMoodActivities(entry.getMoodID(), entry.getActivitiesIDs());

        //write activities
//        EntryActivityDataSource writeActivitiesEntry = new EntryActivityDataSource(mContext);
//        writeActivitiesEntry.insertEntryActivities(entry.getEntryID(), entry.getActivitiesIDs());

//        write img paths
//        ImgPathDataSource writeImgPaths = new ImgPathDataSource(mContext);
//        writeImgPaths.open();
//        writeImgPaths.insertImgPaths(entry);
//        writeImgPaths.close();

        return  (int)id;
    }

    //delete entry
    public void deleteEntry (int entryID) {

        //delete activities from Entry_Activities table
//        EntryActivityDataSource deleteEntryActivity = new EntryActivityDataSource (mContext);
//        deleteEntryActivity.deleteEntry_Activity(entryID);

        //delete entry from Entry table

        String whereQuery = SQLiteHelper.KEY_ENTRYID + "=" + entryID;
        database.delete(SQLiteHelper.TABLE_ENTRY, whereQuery, null);
    }

    //get all entries
    public List<Entry> getAllEntries () {
        List<Entry> entryList = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY,
                allColumns, null, null, null,null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = cursorToEntry (cursor);

            //add entry to list
            entryList.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        sortList(entryList, "DSC");

        return entryList;

    }

    //get entries by mood
    public List<Entry> getEntriesByMood (int moodID) {
        List <Entry> entryList = new ArrayList<>();

        String selection = SQLiteHelper.KEY_MOODID + "=" + moodID;
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY, allColumns, selection, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = cursorToEntry (cursor);

            entryList.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        sortList(entryList, "ASC");
        return entryList;
    }

    //get entries by id
    public Entry getEntryByID (int id)
    {
        Entry entry = new Entry();

        String selection = SQLiteHelper.KEY_ENTRYID + "=" + id;
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY,
                allColumns, selection, null, null,null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry (cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return entry;
    }

    //get mood by entry
    public int getMoodByEntry (int entryID) {
        String selection = SQLiteHelper.KEY_ENTRYID + "=" + entryID;
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY, allColumns, selection, null,
                null, null, null );

        int value = 0;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int index;
            index = cursor.getColumnIndexOrThrow("moodID");
            value = cursor.getInt(index);
            cursor.moveToNext();
        }

        cursor.close();

        return value;
    }

    //get entry by month_Year
    public List<Entry> getEntriesByMonthYear (String monthYear, String order) {

        String splitDateStr[]= monthYear.split(" ");
        String condition = "%" + splitDateStr[0] + "%" + splitDateStr[1];
        List<Entry> entryList = new ArrayList<>();

        String selection = SQLiteHelper.KEY_DATETIME + " LIKE '" + condition + "'";
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY, allColumns, selection, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = cursorToEntry(cursor);

            //add entry to list
            entryList.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        sortList(entryList, order);
        return entryList;
    }

    //get selected entries by mood
    private List <Entry> getFilteredEntriesByMood (int moodID, List<Entry> entryList)
    {
        List <Entry> filteredEntries = new ArrayList<>();

        for (Entry e : entryList)
        {
            if(e.getMoodID() == moodID)
            {
                filteredEntries.add(e);
            }
        }

        sortList(filteredEntries, "ASC");

        return filteredEntries;
    }

    //edit entry
    public void editEntry (Entry entry) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_DATETIME, entry.getDatetime().toString());
        values.put(SQLiteHelper.KEY_TITLE, entry.getTitle());
        values.put(SQLiteHelper.KEY_CONTENT, entry.getContent());

        String whereQuery = SQLiteHelper.KEY_ENTRYID + "=" + entry.getEntryID();
        database.update(SQLiteHelper.TABLE_ENTRY, values, whereQuery,null );

        //get oldMood
        int oldMood = getMoodByEntry(entry.getEntryID());
        int newMood = entry.getMoodID();

        //edit mood
        editMoodInEntry(entry.getEntryID(), newMood, oldMood);

        //edit activities
//        editActivitiesList(entry);

        //editImgPaths
//        editImgPaths(entry);
    }

    //edit Mood in Entry
    public void editMoodInEntry (int entryID, int newMoodID, int oldMoodID) {
//        EntryActivityDataSource activityEntry = new EntryActivityDataSource(mContext);
//        MoodActivityDataSource moodActivity = new MoodActivityDataSource(mContext);

//        List<Integer> activityIDsList = activityEntry.getActivitiesByEntry(entryID);

        //get all activityIDs related to mood and delete them
//        for (int activityID : activityIDsList) {
//            moodActivity.deleteMoodANDActivity(oldMoodID, activityID);
//        }

        //update new moodID in Entry
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_MOODID, newMoodID);

        String updateWhereQuery = SQLiteHelper.KEY_ENTRYID + "=" + entryID;
        database.update(SQLiteHelper.TABLE_ENTRY,values, updateWhereQuery, null);

        //update new rows of mood and activities
//        moodActivity.insertMoodActivities(newMoodID, activityIDsList);
    }

    //set all values to entry
    private Entry cursorToEntry (Cursor cursor) {
        Entry entry = new Entry();
        entry.setEntryID(cursor.getInt(0));
        try {
            entry.setDatetimeStr(cursor.getString(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entry.setTitle(cursor.getString(2));
        entry.setContent(cursor.getString(3));
        entry.setMoodID(cursor.getInt(4));

        return entry;
    }

    //get img paths by entry
    private Entry getInvolvedImgPaths (Entry entry) {
        //get involved image paths
        ImgPathDataSource getImage = new ImgPathDataSource(mContext);

        getImage.open();
        entry.setImgPath(getImage.getImgPathsByEntry(entry.getEntryID()));
        getImage.close();

        return entry;
    }

    //sort entryList
    private List <Entry> sortList (List<Entry> unsortedList, String order) {

        //ascending order
        if(order.equals("ASC"))
        {
            Collections.sort(unsortedList, new Comparator<Entry>() {
                @Override
                public int compare(Entry lhs, Entry rhs) {
                    Date lhsDate = lhs.getDatetime();
                    Date rhsDate = rhs.getDatetime();
                    return lhsDate.compareTo(rhsDate);
                }
            });

        }
        //descending order
        else
        {
            Collections.sort(unsortedList, new Comparator<Entry>() {
                @Override
                public int compare(Entry lhs, Entry rhs) {
                    Date lhsDate = lhs.getDatetime();
                    Date rhsDate = rhs.getDatetime();

                    return rhsDate.compareTo(lhsDate);
                }
            });
        }

        return unsortedList;
    }

}
