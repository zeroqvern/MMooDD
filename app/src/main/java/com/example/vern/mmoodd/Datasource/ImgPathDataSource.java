package com.example.vern.mmoodd.Datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vern.mmoodd.Database.SQLiteHelper;
import com.example.vern.mmoodd.Model.Entry;

import java.util.ArrayList;
import java.util.List;


public class ImgPathDataSource {
    //Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.KEY_ENTRYID,
            SQLiteHelper.KEY_IMGPATH
    };
    private Context mContext;

    public ImgPathDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //insert image paths
    public Entry insertImgPaths (Entry entry){
        //insert all image paths
        List<String> imgPathList = entry.getImgPaths();
        for (String path : imgPathList) {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.KEY_ENTRYID, entry.getEntryID());
            values.put(SQLiteHelper.KEY_IMGPATH, path);

            database.insert(SQLiteHelper.TABLE_ENTRY_IMAGE, null, values);
        }

        //retrieve all image paths
        List<String> dbImgPathList = new ArrayList<String>();

        String selection = SQLiteHelper.KEY_ENTRYID + "=" + entry.getEntryID();
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY_IMAGE, allColumns, selection,
                null, null, null,null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int index;
            index = cursor.getColumnIndexOrThrow("imgPath");
            String value = cursor.getString(index);

            dbImgPathList.add(value);
        }
        cursor.close();

        entry.setImgPath(dbImgPathList);

        return entry;
    }

    //delete image paths
    public void deleteImgPath (Entry entry) {
        int entryID = entry.getEntryID();

        String whereQuery = SQLiteHelper.KEY_ENTRYID + "=" + entryID;
        database.delete(SQLiteHelper.TABLE_ENTRY_IMAGE, whereQuery, null);
    }

    //get image paths by entry id
    public List <String> getImgPathsByEntry (int entryID) {
        List <String> imgPathsList = new ArrayList<String>();

        String selection = SQLiteHelper.KEY_ENTRYID + "=" + entryID;
        Cursor cursor = database.query(SQLiteHelper.TABLE_ENTRY_IMAGE, allColumns, selection,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int index;
            index = cursor.getColumnIndexOrThrow("imgPath");
            String value = cursor.getString(index);

            imgPathsList.add(value);
            cursor.moveToNext();
        }
        cursor.close();

        return  imgPathsList;
    }
}
