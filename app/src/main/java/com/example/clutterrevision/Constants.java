package com.example.clutterrevision;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String DATABASE_NAME = "database";

    public static final String TABLE_NAME_CHECKLIST = "checklistTable";
    public static final String TABLE_NAME_NOTE = "noteTable";
    public static final String TABLE_NAME_DAY = "dayTable";
    public static final String SP_DAYS = "datePreferences";

    public static final int TYPE_NOTE = 0;
    public static final int TYPE_REFERENCE = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_CHECKLIST = 3;
    public static final int TYPE_GOOGLE_BOOKS = 4;
    public static final int MY_PERMISSIONS_REQUESTS = 0;


    public static final Map<Integer, String> daysOfWeek;

    static {
        daysOfWeek = new HashMap<>();
        daysOfWeek.put(1, "Mon");
        daysOfWeek.put(2, "Tues");
        daysOfWeek.put(3, "Weds");
        daysOfWeek.put(4, "Thur");
        daysOfWeek.put(5, "Fri");
        daysOfWeek.put(6, "Sat");
        daysOfWeek.put(7, "Sun");

    }

    public static final Map<Integer, String> monthsOfYear;

    static {
        monthsOfYear = new HashMap<>();
        monthsOfYear.put(1, "Jan");
        monthsOfYear.put(2, "Feb");
        monthsOfYear.put(3, "Mar");
        monthsOfYear.put(4, "Apr");
        monthsOfYear.put(5, "May");
        monthsOfYear.put(6, "Jun");
        monthsOfYear.put(7, "Jul");
        monthsOfYear.put(8, "Aug");
        monthsOfYear.put(9, "Sep");
        monthsOfYear.put(10, "Oct");
        monthsOfYear.put(11, "Nov");
        monthsOfYear.put(12, "Dec");
    }

}
