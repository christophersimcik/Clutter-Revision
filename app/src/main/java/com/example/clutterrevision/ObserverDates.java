package com.example.clutterrevision;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface ObserverDates {
    void onEmptyDateDeleted();
    void onTodaysDateChecked(PojoDay pojoDay);
    void onDateInserted();
    void onDatesRetrieved(List<PojoDay> pojoDays);
}
