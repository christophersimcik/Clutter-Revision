package com.example.clutterrevision;

import android.service.autofill.FillEventHistory;

import java.util.ArrayList;
import java.util.List;

public class PojoMonth {

    private int month;
    private int year;
    private int days;
    private int firstDayOfWeek;
    private List<PojoDayOfMonth> daysOfMonth;

    public PojoMonth(int year, int month, int days, int firstDayOfWeek){

        this.year = year;
        this.month = month;
        this.days = days;
        this.firstDayOfWeek = firstDayOfWeek;
        this.daysOfMonth = createListOfDays();
        System.out.println("day of week = " + firstDayOfWeek);
    }

    public int getMonth(){
        return this.month;
    }
    public int getYear(){
        return this.year;
    }
    public int getDays(){
        return this.days;
    }
    public int getFirstDayOfWeek(){
        return this.firstDayOfWeek;
    }
    public void setFirstDayOfWeek(int firstDayOfWeek){
        this.firstDayOfWeek = firstDayOfWeek;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void setDays(int days){
        this.days = days;
    }
    public List<PojoDayOfMonth> getDaysOfMonth(){
        return this.daysOfMonth;
    }

     public List<PojoDayOfMonth> createListOfDays(){
         List<PojoDayOfMonth> listOfDaysinMonth = new ArrayList<>();
         int dayOfWeek = this.firstDayOfWeek;
         int counter = 1;
         while( counter < dayOfWeek){
             System.out.println("counting = " + counter);
             listOfDaysinMonth.add(null);
             counter ++;
         }
         for(int i = 1 ; i <= days; i ++){
             String asString = createString(month,year,i);
             listOfDaysinMonth.add(new PojoDayOfMonth(i,dayOfWeek,asString));
             dayOfWeek = dayOfWeekTicker(dayOfWeek);
             counter++;
         }
         while(counter <= 42){
             listOfDaysinMonth.add(null);
             counter++;
         }
        return listOfDaysinMonth;
     }

    private String createString(int month, int year, int day){
        String monthFormatted = String.format("%02d",month);
        String dayFormatted = String.format("%02d",day);
        String formattedString = monthFormatted+dayFormatted+year;
        return formattedString  ;
    }

     private int dayOfWeekTicker(int i){
         if(i<7){
             i++;
         }else{
             i=1;
         }
         return i;
     }
}
