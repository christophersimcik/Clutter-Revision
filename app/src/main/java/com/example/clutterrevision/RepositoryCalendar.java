package com.example.clutterrevision;

import android.app.Application;

public class RepositoryCalendar {
    DayHelper dayHelper;
    RepositoryDay repositoryDay;
    PojoMonth pojoMonth;
    int year, month;

    public RepositoryCalendar(Application application) {
        dayHelper = DayHelper.getInstance();
        year = dayHelper.getYearAsInt();
        month = dayHelper.getMonthAsInt();
        pojoMonth = dayHelper.getCalendarMonth(year, month);
        repositoryDay = new RepositoryDay(application);
    }

    public PojoMonth nextMonth(Boolean left){
        if(left){
            if(month == 1){
                month = 12;
                year --;
            }else{
                month--;
            }
        }else{
            if(month == 12){
                month = 1;
                year ++;
            }else{
                month++;
            }
        }
        pojoMonth = dayHelper.getCalendarMonth(year, month);
        return pojoMonth;
    }

    public void CheckDays(){
        for(PojoDayOfMonth pojoDayOfMonth : pojoMonth.getDaysOfMonth()){
            repositoryDay.checkForDay(pojoDayOfMonth.dayAsString);
        }
    }






}
