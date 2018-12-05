package com.example.vern.mmoodd.Model;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class DateManager {

    public DateManager(){}

    //get date from date picked dialog
    public String getPickedDatefromDialog(int month, int year)
    {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        String monthName = months[month-1];

        String dateFullStr = monthName + " " + year;

        return dateFullStr;
    }

    //get date string now
    public String getDateStrNow(){
        Date dateNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dateString = ft.format(dateNow);

        return dateString;
    }

    //get date from string
    public Date getDatefromStr(String dateStr)
    {
        SimpleDateFormat ft = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        Date dateGiven = new Date();

        try {
            dateGiven = ft.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateGiven;

    }

    //get full date string from date
    public String getFullStrfromDate (Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

        String dateString = ft.format(date);
        return dateString;

    }

    //get date string from date given
    public String getDateStr (Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dateString = ft.format(date);
        return dateString;
    }

    //get month and year now
    public String getMonthYearNow()
    {
        Date dateNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MMM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dateString = ft.format(dateNow);

        return dateString;
    }

    //get month and year from date given
    public String getMonthYear (Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("MMM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dateString = ft.format(date);

        return dateString;
    }

    //get previous month from given month and year string
    public String getMonthYearPrevious (String monthYear)
    {
        String dateMembers[] = monthYear.split(" ");
        int monthNum = getMonthNum(dateMembers[0]);

        String dateStr = getMonthName(monthNum - 1) + " " + dateMembers[1];
        return dateStr;
    }

    //get later month from given month and year string
    public String getMonthYearAfter (String monthYear)
    {
        String dateMembers[] = monthYear.split(" ");
        int monthNum = getMonthNum(dateMembers[0]);
        if(monthNum == 12)
        {
            monthNum = 0;
        }
        String dateStr = getMonthName(monthNum + 1) + " " + dateMembers[1];
        return dateStr;
    }

    //get date string from date given
    public String getDateStrGiven(Date dateGiven)
    {
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dateString = ft.format(dateGiven);
        return dateString;
    }

    //get month name
    public String getMonthName (int month)
    {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();

        return months[month-1];
    }

    //get month number
    public int getMonthNum (String month) {

        int position = -1;

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();

        for(String m : months) {
            if (m.equals(month)) {
                position = Arrays.asList(months).indexOf(m) + 1;
                return position;
            }
        }
        return position;
    }

    //get day from date
    public String getDay(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("dd");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String day = ft.format(date);

        return day;
    }

    //get day name from date
    public String getDayofWeek (Date date)
    {

        SimpleDateFormat ft = new SimpleDateFormat("E");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String dayName = ft.format(date);

        return dayName;
    }

    //get month from date
    public String getMonth(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("MMM");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String month = ft.format(date);

        return month;
    }

    //get year from date
    public String getYear(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String year = ft.format(date);

        return year;
    }

    //get time from date
    public String getTime(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String time = ft.format(date);

        return time;
    }

}
