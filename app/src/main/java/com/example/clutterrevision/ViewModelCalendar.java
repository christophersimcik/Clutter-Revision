package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class ViewModelCalendar extends AndroidViewModel{
    RepositoryCalendar repositoryCalendar;

    public ViewModelCalendar(@NonNull Application application) {
        super(application);
        repositoryCalendar = new RepositoryCalendar(application);
    }

    public void setData(AdapterCalendar adapterCalendar){
        adapterCalendar.setData(repositoryCalendar.pojoMonth.getDaysOfMonth());
    }

    public void next(Boolean left){
        repositoryCalendar.nextMonth(left);

    }

    public void setHeader(TextView textView){
        String month = Constants.monthsOfYear.get(repositoryCalendar.pojoMonth.getMonth());
        String year = String.valueOf(repositoryCalendar.pojoMonth.getYear());
        textView.setText(month+", "+year);
    }






}
