package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelDay extends AndroidViewModel implements ObserverNotes, ObserverDates {

    DayHelper dayHelper;
    LiveData<List<PojoDay>> listOfDays;
    RepositoryDay repositoryDay;
    RepositoryNotes repositoryNote;
    MutableLiveData<List<PojoNote>> listOfNotes;
    public FragmentManager fragmentManager;
    String dayID, dayOfWeek, monthOfYear, dayOfMonth, year;
    String currentDay;

    private PojoDay pojoDay;

    public ViewModelDay(@NonNull Application application) {
        super(application);
        this.repositoryDay = new RepositoryDay(application);
        repositoryDay.register(this);
        this.repositoryNote = new RepositoryNotes(application);
        this.repositoryNote.register(this);
        this.dayHelper = DayHelper.getInstance();
        this.dayID = dayHelper.getDateAsString();
        this.dayOfWeek = dayHelper.getDayOfWeek();
        this.monthOfYear = dayHelper.getMonthOfYear();
        this.dayOfMonth = dayHelper.getDayOfMonth();
        this.year = dayHelper.getYear();
        this.listOfNotes = new MutableLiveData<>();
    }

    public void refreshNotes() {
        if( currentDay == null || currentDay.isEmpty()) {
        currentDay = dayHelper.getDateAsString();
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
        fileAudioImage.inputAudioImageData(s);
        fileAudioImage.register(view);
    }

    public void setPojoDay(PojoDay pojoDay) {
        this.pojoDay = pojoDay;
    }

    public PojoDay getPojoDay() {
        return this.pojoDay;
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

}
