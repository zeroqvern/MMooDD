package com.example.vern.mmoodd.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vern.mmoodd.Datasource.EntryDataSource;
import com.example.vern.mmoodd.Model.Entry;
import com.example.vern.mmoodd.R;

import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    //Logcat tag
    private static final String LOG = "DatabaseHelper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "MoodTracker.db";

    //Table names
    public static final String TABLE_ENTRY = "entry";
    public static final String TABLE_MOOD ="mood";
    public static final String TABLE_ENTRY_IMAGE = "entry_image";
//    public static final String TABLE_ICON = "icon";

    //ID column names
    public static final String KEY_ENTRYID = "entryID";
    public static final String KEY_MOODID = "moodID";
//    public static final String KEY_ICONID = "iconID";


    //Table: ENTRY column names
    //entryID
    public static final String KEY_DATETIME = "datetime";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    //moodID

    //Table: MOOD column names
    //moodID
    public static final String KEY_MOODNAME = "moodName";
    public static final String KEY_MOODICONRESID = "moodIconResID";
    public static final String KEY_MOODICONNEGID = "moodIconNegID";
    public static final String KEY_COLORRESID = "colorResID";
    //iconID

    //Table: ACTIVITY column names
    //activityID
//    public static final String KEY_ACTIVITYNAME = "activityName";
    //iconID

    //Table: MOOD_ACTIVITY column names
    //moodID
    //    //activityID

    //Table: ENTRY_ACTIVITY column names
    //entryID
    //activityID


    //Table: ENTRY_IMAGE
    //entryID
    public static final String KEY_IMGPATH = "imgPath";

    //Table: ICON
    //iconID
//    public static final String KEY_ICONRESID= "iconResID";
//    public static final String KEY_ICONNEGATIVE = "iconNegative";


    //Table Create Statements
    //ENTRY CREATE
    private static final String CREATE_TABLE_ENTRY = "CREATE TABLE "
            + TABLE_ENTRY + "("
            + KEY_ENTRYID + " INTEGER PRIMARY KEY,"
            + KEY_DATETIME + " TEXT,"
            + KEY_TITLE + " TEXT,"
            + KEY_CONTENT + " TEXT,"
            + KEY_MOODID + " INTEGER,"
            + " FOREIGN KEY ("+KEY_MOODID+") REFERENCES "+TABLE_MOOD+"("+KEY_MOODID+"));";

    private static final String CREATE_TABLE_MOOD = "CREATE TABLE "
            + TABLE_MOOD + "("
            + KEY_MOODID + " INTEGER PRIMARY KEY,"
            + KEY_MOODNAME + " TEXT,"
            + KEY_MOODICONRESID + " INTEGER,"
            + KEY_MOODICONNEGID + " INTEGER,"
            + KEY_COLORRESID + " INTEGER);";

    private static final String CREATE_TABLE_ENTRY_IMAGE = "CREATE TABLE "
            + TABLE_ENTRY_IMAGE + "("
            + KEY_ENTRYID + " INTEGER,"
            + KEY_IMGPATH + " TEXT,"
            + "FOREIGN KEY ("+KEY_ENTRYID+") REFERENCES "+TABLE_ENTRY+"("+KEY_ENTRYID+")); ";


//    private static final String CREATE_TABLE_ICON = "CREATE TABLE "
//            + TABLE_ICON + "("
//            + KEY_ICONID + " INTEGER PRIMARY KEY,"
//            + KEY_ICONRESID + " INTEGER,"
//            + KEY_ICONNEGATIVE + " TEXT);";


    //insert moods into database
    private static final String INSERT_MOOD_HAPPY = "INSERT INTO "
            + TABLE_MOOD + "("
            + KEY_MOODNAME + ","
            + KEY_MOODICONRESID + ","
            + KEY_MOODICONNEGID + ","
            + KEY_COLORRESID + ")"
            + "VALUES ('HAPPY', "
            + R.drawable.ic_mood_happy + ","
            + R.drawable.ic_mood_happy_negative + ","
            + R.color.orange + " );";

    private static final String INSERT_MOOD_NORMAL = "INSERT INTO "
            + TABLE_MOOD + "("
            + KEY_MOODNAME + ","
            + KEY_MOODICONRESID + ","
            + KEY_MOODICONNEGID + ","
            + KEY_COLORRESID + ")"
            + "VALUES ('NORMAL', "
            + R.drawable.ic_mood_normal + ","
            + R.drawable.ic_mood_normal_negative + ","
            + R.color.green + " );";

    private static final String INSERT_MOOD_NUMB = "INSERT INTO "
            + TABLE_MOOD + "("
            + KEY_MOODNAME + ","
            + KEY_MOODICONRESID + ","
            + KEY_MOODICONNEGID + ","
            + KEY_COLORRESID + ")"
            + "VALUES ('NUMB', "
            + R.drawable.ic_mood_numb + ","
            + R.drawable.ic_mood_numb_negative + ","
            + R.color.purple + " );";

    private static final String INSERT_MOOD_SAD = "INSERT INTO "
            + TABLE_MOOD + "("
            + KEY_MOODNAME + ","
            + KEY_MOODICONRESID + ","
            + KEY_MOODICONNEGID + ","
            + KEY_COLORRESID + ")"
            + "VALUES ('SAD', "
            + R.drawable.ic_mood_sad + ","
            + R.drawable.ic_mood_sad_negative + ","
            + R.color.blue+ " );";

    private static final String INSERT_MOOD_DEAD = "INSERT INTO "
            + TABLE_MOOD + "("
            + KEY_MOODNAME + ","
            + KEY_MOODICONRESID + ","
            + KEY_MOODICONNEGID + ","
            + KEY_COLORRESID + ")"
            + "VALUES ('DEAD', "
            + R.drawable.ic_mood_dead + ","
            + R.drawable.ic_mood_dead_negative + ","
            + R.color.coolGrey + " );";


    public SQLiteHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate (SQLiteDatabase database){
        database.execSQL(CREATE_TABLE_ENTRY);
        database.execSQL(CREATE_TABLE_MOOD);
        database.execSQL(CREATE_TABLE_ENTRY_IMAGE);
        database.execSQL(INSERT_MOOD_HAPPY);
        database.execSQL(INSERT_MOOD_NORMAL);
        database.execSQL(INSERT_MOOD_NUMB);
        database.execSQL(INSERT_MOOD_SAD);
        database.execSQL(INSERT_MOOD_DEAD);

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName() ,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", old data preserved");
    }
}
