package com.example.clutterrevision;

import android.view.View;

import java.time.DayOfWeek;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class PojoDay {

    @PrimaryKey
    @NonNull
    private String day_id;

    @ColumnInfo(name = "day_of_week")
    private String dayOfWeek;

    @ColumnInfo(name = "month_of_year")
    private String monthOfYear;

    @ColumnInfo(name = "day_of_month")
    private String dayOfMonth;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "number_of_notes")
    private int numberOfNotes;

    @ColumnInfo(name = "number_of_audio_notes")
    private int numberOfAudioNotes;

    @ColumnInfo(name = "number_of_note_notes")
    private int numberOfNoteNotes;

    @ColumnInfo(name = "number_of_ref_notes")
    private int numberOfReferNotes;

    @ColumnInfo(name = "number_of_book_notes")
    private int numberOfBookNotes;

    @ColumnInfo(name = "number_of_list_notes")
    private int numberOfListNotes;

    public PojoDay(
            String day_id,
            String dayOfWeek,
            String monthOfYear,
            String dayOfMonth,
            String year,
            int numberOfNotes,
            int numberOfAudioNotes,
            int numberOfNoteNotes,
            int numberOfReferNotes,
            int numberOfBookNotes,
            int numberOfListNotes){
        this.day_id = day_id;
        this.dayOfWeek = dayOfWeek;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.year = year;
        this.numberOfNotes = numberOfNotes;
        this.numberOfAudioNotes = numberOfAudioNotes;
        this.numberOfNoteNotes = numberOfNoteNotes;
        this.numberOfReferNotes = numberOfReferNotes;
        this.numberOfBookNotes = numberOfBookNotes;
        this.numberOfListNotes = numberOfListNotes;

    }

    public void setDay_id(String dayID){
        this.day_id = dayID;
    }

    public String getDay_id(){
        return this.day_id;
    }

    public void setDayOfWeek(String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek(){
        return this.dayOfWeek;
    }

    public void setMonthOfYear(String monthOfYear){
        this.monthOfYear = monthOfYear;
    }

    public String getMonthOfYear(){
        return this.monthOfYear;
    }

    public void setDayOfMonth(String dayOfMonth){
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfMonth(){
     return this.dayOfMonth;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getYear(){
        return this.year;
    }

    public void setNumberOfNotes(int numberOfNotes){
        this.numberOfNotes = numberOfNotes;
    }
    public int getNumberOfNotes(){
        return numberOfNotes;
    }

    // number of note
    public void setNumberOfNoteNotes(int numberOfNoteNotes){
        this.numberOfNoteNotes = numberOfNoteNotes;
    }
    public int getNumberOfNoteNotes(){
        return numberOfNoteNotes;
    }

    //number of audio
    public void setNumberOfAudioNotes(int numberOfAudioNotes){
        this.numberOfAudioNotes = numberOfAudioNotes;
    }
    public int getNumberOfAudioNotes(){
        return numberOfAudioNotes;
    }

    //number of reference
    public void setNumberOfReferNotes(int numberOfReferNotes){
        this.numberOfReferNotes = numberOfReferNotes;
    }
    public int getNumberOfReferNotes(){
        return numberOfReferNotes;
    }

    //number of book
    public void setNumberOfBookNotes(int numberOfBookNotes){
        this.numberOfBookNotes = numberOfBookNotes;
    }
    public int getNumberOfBookNotes(){
        return numberOfBookNotes;
    }

    //number of lists
    public void setNumberOfListNotes(int numberOfListNotes){
        this.numberOfListNotes = numberOfListNotes;
    }
    public int getNumberOfListNotes(){
        return numberOfListNotes;
    }

}


