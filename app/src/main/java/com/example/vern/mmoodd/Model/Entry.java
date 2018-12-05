package com.example.vern.mmoodd.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Entry {
    private int entryID;
    private Date entryDateTime;
    private String title;
    private String content;
    private int moodID;
    private List<String> imgPaths;

    public Entry (){}

    //entryID
    public int getEntryID() {
        return entryID;
    }

    public void setEntryID (int id) {
        this.entryID = id;
    }

    //date time
    public Date getDatetime() {
        return entryDateTime;
    }

    public String getDateString() {
        SimpleDateFormat ft = new SimpleDateFormat("dd MM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return ft.format(getDatetime());
    }

    public int getDayofMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(getDatetime());
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public String getTimeString() {
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return ft.format(getDatetime());
    }

    public void setDatetimeStr(String d) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        this.entryDateTime = ft.parse(d);
    }

    public void setDateTime(Date d) {
        entryDateTime = d;
    }

    //title
    public String getTitle() {
        return title;
    }

    public void setTitle (String t) {
        this.title = t;
    }

    //content
    public String getContent() {
        return content;
    }

    public void setContent (String c) {
        this.content = c;
    }

    //moodID
    public int getMoodID() {
        return moodID;
    }

    public void setMoodID (int id) {
        this.moodID = id;
    }

    //image paths
    public List<String> getImgPaths () {
        return imgPaths;
    }

    public void setImgPath (List <String> paths) {
        this.imgPaths = paths;
    }
}
