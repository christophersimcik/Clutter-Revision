package com.example.clutterrevision;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

public class DayHelper {
    private static final DayHelper dayHelper = new DayHelper();
    private DateTime dt;
    private int dayOfWeek;
    private int monthOfYear;
    private int dayOfMonth;
    private int year;
    private String[] dateAsArray;

    public static DayHelper getInstance() {
        return dayHelper;
    }

    private DayHelper() {
        this.dt = getDateTime();
        this.dayOfWeek = dt.getDayOfWeek();
        this.monthOfYear = dt.getMonthOfYear();
        this.dayOfMonth = dt.getDayOfMonth();
        this.year = dt.getYear();
        this.dateAsArray = convertToArray(dt);
        System.out.println("*** and *** " + getDateAsString());
    }

    private String[] convertToArray(DateTime dt) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM'/'dd'/'yyyy");
        return dtf.print(dt).split("/");
    }

    private DateTime getDateTime() {
        DateTime dt = new DateTime();
        return dt.withZone(DateTimeZone.forID(TimeZone.getDefault().getID()));
    }

    public String getDateAsString() {
        return dateAsArray[0] + dateAsArray[1] + dateAsArray[2];
    }

    public String[] getDateArray() {
        return dateAsArray;
    }

    public String getDayOfWeek() {
        return Constants.daysOfWeek.get(dayOfWeek);
    }

    public String getMonthOfYear() {
        return Constants.monthsOfYear.get(monthOfYear);
    }

    public String getDayOfMonth() {
        return String.valueOf(dayOfMonth);
    }

    public String getYear() {
        return String.valueOf(year);
    }
    public int getYearAsInt(){
        return year;
    }

    public int getMonthAsInt(){
        return monthOfYear;
    }

    public PojoMonth getCalendarMonth(int year, int month) {
        int numOfDays;
        int firstDayOfMonth;
        dt = new DateTime(year, month, 1, 0, 0);
        numOfDays = dt.dayOfMonth().getMaximumValue();
        firstDayOfMonth = dt.getDayOfWeek();
        return new PojoMonth(year, month, numOfDays, firstDayOfMonth);
    }

}
