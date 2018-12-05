package com.example.vern.mmoodd.Datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vern.mmoodd.Database.SQLiteHelper;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.Model.Mood;

import java.util.ArrayList;
import java.util.List;

public class MoodDataSource {
    //Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.KEY_MOODID,
            SQLiteHelper.KEY_MOODNAME,
            SQLiteHelper.KEY_MOODICONRESID,
            SQLiteHelper.KEY_MOODICONNEGID,
            SQLiteHelper.KEY_COLORRESID };
    private Context mContext;

    public MoodDataSource (Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close () {
        dbHelper.close();
    }

    //create Mood
//    public void createMood (Mood mood) {
//        ContentValues values = new ContentValues();
//        values.put(SQLiteHelper.KEY_MOODNAME, mood.getMoodName());
//        values.put(SQLiteHelper.KEY_ICONID, mood.getIconID());
//
//        database.insert(SQLiteHelper.TABLE_MOOD,null, values);
//
//    }

    //delete mood
//    public void deleteMood (int moodID) {
//        //edit Moods in Entry to default
//        int defaultMoodID = 1;
//
//        //get a list of entries
//        EntryDataSource editMoodDefault = new EntryDataSource(mContext);
//        List <Entry> entryList = editMoodDefault.getEntriesByMood(moodID);
//
//        //edit the moods in the Entries to default
//        if (!entryList.isEmpty() || entryList.size() != 0){
//            for (Entry item : entryList) {
//                editMoodDefault.editMoodInEntry(item.getEntryID(), defaultMoodID, moodID);
//            }
//        }

    //delete mood and activity relationships in Mood_Activities table
//        MoodActivityDataSource deleteMoodActivity = new MoodActivityDataSource(mContext);
//        deleteMoodActivity.deleteMood_Activity(moodID);

    //delete mood completely
//        String whereQuery = SQLiteHelper.KEY_MOODID + "=" + moodID;
//        database.delete(SQLiteHelper.TABLE_MOOD, whereQuery, null);
//        //toast: "Mood deleted"
//    }

    //get all moods
    public List<Mood> getAllMoods () {
        List<Mood> moodList = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MOOD,
                allColumns, null, null, null,null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mood mood = cursorToMood (cursor);
            moodList.add(mood);
            cursor.moveToNext();
        }
        cursor.close();

        return moodList;
    }

    //get mood by ID
    public Mood getMoodByID (int id)
    {
        Mood selectedMood = new Mood();

        String selection = SQLiteHelper.KEY_MOODID + "=" + id;
        Cursor cursor = database.query(SQLiteHelper.TABLE_MOOD,
                allColumns, selection, null, null,null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            selectedMood = cursorToMood (cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return selectedMood;

    }

    //edit mood
//    public void editMood (Mood mood) {
//        ContentValues values = new ContentValues();
//        values.put(SQLiteHelper.KEY_MOODNAME, mood.getMoodName());
//        values.put(SQLiteHelper.KEY_ICONID, mood.getIconID());
//
//        String whereQuery = SQLiteHelper.KEY_MOODID + "=" + mood.getMoodID();
//
//        database.update(SQLiteHelper.TABLE_MOOD,values, whereQuery,null );
//        //toast: "Successfully edited mood"
//    }

    //set all values to mood
    private Mood cursorToMood (Cursor cursor) {
        Mood mood = new Mood();
        mood.setMoodID(cursor.getInt(0));
        mood.setMoodName(cursor.getString(1));
        mood.setMoodResID(cursor.getInt(2));
        mood.setMoodNegID(cursor.getInt(3));
        mood.setColorResID(cursor.getInt(4));

        return mood;
    }
}
