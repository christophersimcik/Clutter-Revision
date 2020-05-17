package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelDay extends AndroidViewModel implements ObserverNotes, ObserverDates {

    LiveData<List<PojoDay>> listOfDays;
    RepositoryDay repositoryDay;
    RepositoryNotes repositoryNote;
    MutableLiveData<List<PojoNote>> listOfNotes;
    public FragmentManager fragmentManager;
    String currentDay;
    ViewModelActivity viewModelActivity;

    private PojoDay pojoDay;

    public ViewModelDay(@NonNull Application application) {
        super(application);
        this.repositoryDay = new RepositoryDay(application);
        repositoryDay.register(this);
        this.repositoryNote = new RepositoryNotes(application);
        repositoryNote.register(this);
        this.listOfNotes = new MutableLiveData<>();
        this.listOfDays = repositoryDay.getAllDaysAsLiveData();
    }

    public void refreshNotes() {
        if( currentDay == null || currentDay.isEmpty()) {
        currentDay = getDayId();
        }
        getDaysNotes(currentDay);
    }

    public void getDaysNotes(String day) {
        repositoryNote.getThisDay(day);
    }

    public void insertNote(PojoNote pojoNote) {
        repositoryNote.insert(pojoNote);
    }

    @Override
    public void onDayRetrieved(List<PojoNote> pojoNotes) {
        listOfNotes.postValue(pojoNotes);
    }

    public void getAudioImage(String s, VisualAudio view) {
        RepositoryAudioImageData fileAudioImage = new RepositoryAudioImageData();
        fileAudioImage.register(view);
        fileAudioImage.inputAudioImageData(s);
    }

    public void setPojoDay(PojoDay pojoDay) {
        this.pojoDay = pojoDay;
    }

    public PojoDay getPojoDay() {
        return this.pojoDay;
    }

    public void setViewModelActivity(ViewModelActivity viewModelActivity) {
        this.viewModelActivity = viewModelActivity;
    }

    @Override
    public void onEmptyDateDeleted() {

    }

    @Override
    public void onTodaysDateChecked(PojoDay pojoDay) {

    }

    @Override
    public void onDateInserted() {
    }

    @Override
    public void onDatesRetrieved(List<PojoDay> pojoDays) {
    }

    public String getDayId(){
        return DayHelper.getInstance().getDateAsString();
     }

}
