package com.example.clutterrevision;

public class PojoDayOfMonth {

    int dayOfMonth;
    int dayOfWeek;
    String dayAsString;
    int[] typesOfNotes = new int[4];

    public PojoDayOfMonth(int dayOfMonth, int dayOfWeek, String dayAsString) {
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.dayAsString = dayAsString;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void addTypesOfNotes(int type) {
        typesOfNotes[type]++;
    }
}
