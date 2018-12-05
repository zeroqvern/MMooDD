package com.example.vern.mmoodd.Model;

public class Mood {
    private int moodID;
    private String moodName;
    private int moodResID;
    private int moodNegID;
    private int colorResID;

    //moodID
    public int getMoodID() {
        return moodID;
    }

    public void setMoodID (int id){
        this.moodID = id;
    }

    //mood name
    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String s) {
        this.moodName = s;
    }

    //mood resource ID
    public int getMoodResID() { return moodResID;}

    public void setMoodResID(int id) {
        this.moodResID= id;
    }

    //mood negative resource ID
    public int getMoodNegID () { return moodNegID;}

    public void setMoodNegID (int id)
    {
        this.moodNegID = id;
    }

    //color resource ID
    public int getColorResID () {return colorResID;}

    public void setColorResID (int id) {
        this.colorResID = id;
    }

}
