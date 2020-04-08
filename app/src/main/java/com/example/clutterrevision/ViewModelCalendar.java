package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    private ViewModelActivity viewModelActivity;
    DayHelper dayHelper = DayHelper.getInstance();
    MutableLiveData<Boolean> isCurrentMonth = new MutableLiveData<>();

    public ViewModelCalendar(@NonNull Application application) {
        super(application);
        repositoryCalendar = new RepositoryCalendar(application);
        this.viewModelActivity = viewModelActivity;
    }

    public void setData(AdapterCalendar adapterCalendar){
        if(repositoryCalendar.month == dayHelper.getMonthAsInt()){
            isCurrentMonth.postValue(true);
        }else{
            isCurrentMonth.postValue(false);
        }
        adapterCalendar.setData(repositoryCalendar.pojoMonth.getDaysOfMonth());
        adapterCalendar.resetList();
    }

    public void next(Boolean left){
        repositoryCalendar.nextMonth(left);
        repositoryCalendar.CheckDays();

    }

    public void reset(){
        repositoryCalendar.reset();
    }

    public void check(){
        repositoryCalendar.CheckDays();
    }

    public void setHeader(TextView textView){
        String month = Constants.monthsOfYear.get(repositoryCalendar.pojoMonth.getMonth());
        String year = String.valueOf(repositoryCalendar.pojoMonth.getYear());
        textView.setText(month+", "+year);
    }

    public ViewModelActivity getViewModelActivity(){
        return this.viewModelActivity;
    }
    public void setViewModelActivity(ViewModelActivity viewModelActivity){
        this.viewModelActivity = viewModelActivity;
    }






}
