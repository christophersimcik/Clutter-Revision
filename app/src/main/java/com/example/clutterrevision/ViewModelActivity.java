package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelActivity extends AndroidViewModel implements ObserverDates{

    Fragment savedFragment;
    PojoDay pojoDay;
    PojoDay today;
    private int myPosition;
    private String currentDay;
    private FragmentManager fragmentManager;
    private Context context;
    private RepositoryDay repositoryDay = new RepositoryDay(getApplication());
    public MutableLiveData<List<PojoDay>> datesLiveData = new MutableLiveData<>();

    public ViewModelActivity(@NonNull Application application) {
        super(application);
        repositoryDay.register(this);
        context = application.getApplicationContext();
        myPosition = initPosition();
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager(){
        return this.fragmentManager;
    }

    public void setPosition(int position){
        this.myPosition = position;
    }

    public int getPosition(){
        return this.myPosition;
    }

     private int initPosition(){
         String nameSP = Constants.SP_DAYS;
         String key = context.getResources().getString(R.string.number_of_days);
         SharedPreferences sharedPreferences = context.getSharedPreferences(nameSP, Context.MODE_PRIVATE);
         int size = 0;
         if(sharedPreferences != null && sharedPreferences.contains(key)){
             size = sharedPreferences.getInt(key,0);
         }

         return size;
     }

     public void deleteAllEmptyDates(){
        repositoryDay.deleteEmpty();
     }

     private void retrieveTodaysDate(){
        repositoryDay.checkForDay(DayHelper.getInstance().getDateAsString());
     }

     private void createTodaysDate() {
         String dayID = DayHelper.getInstance().getDateAsString();
         String dayOfWeek = DayHelper.getInstance().getDayOfWeek();
         String monthOfYear = DayHelper.getInstance().getMonthOfYear();
         String dayOfMonth = DayHelper.getInstance().getDayOfMonth();
         String year = DayHelper.getInstance().getYear();
         int numberOfNotes = 0;
         int numberOfNoteNotes = 0;
         int numberOfAudioNotes = 0;
         int numberOfReferNotes = 0;
         int numberOfBookNotes = 0 ;
         int numberOfListNotes = 0;
         PojoDay pojoDay = new PojoDay(
                 dayID,
                 dayOfWeek,
                 monthOfYear,
                 dayOfMonth,
                 year,
                 numberOfNotes,
                 numberOfNoteNotes,
                 numberOfAudioNotes,
                 numberOfReferNotes,
                 numberOfBookNotes,
                 numberOfListNotes
         );
         today = pojoDay;
         repositoryDay.insert(pojoDay);
     }

    // retrieve and set the saved fragment
    public void getSavedFragment(Bundle bundle) {
        String savedKey;
        if (bundle != null) {
            savedKey = bundle.getString("key", "");
            this.savedFragment = fragmentManager.getFragment(bundle, savedKey);
        } else {
            this.savedFragment = null;
        }
    }
    //
    private FragmentNotes getFragmentNotes() {
        FragmentNotes fragmentNotes = (FragmentNotes) fragmentManager.findFragmentByTag("notes");
        if (fragmentNotes == null) {
            fragmentNotes = new FragmentNotes();
        }
        return fragmentNotes;
    }

    public void dayFragmentManager() {

        Fragment main = fragmentManager.findFragmentById(R.id.fragment_main);
        Fragment date = fragmentManager.findFragmentById(R.id.fragment_dates);

        if (main instanceof FragmentNotes && !(date instanceof FragmentDates)) {
            fragmentManager.beginTransaction().add(R.id.fragment_dates, new FragmentDates(), null)
            .commit();
        }

        if (!(main instanceof FragmentNotes) && date instanceof FragmentDates) {
            fragmentManager.beginTransaction().remove(date).commit();
        }
    }

    public void jumpToThisDay(String dayID){
        List<PojoDay> pojoDays = datesLiveData.getValue();
        for(int i = 0; i < pojoDays.size(); i ++){
            if (pojoDays.get(i).getDay_id().equals(dayID)) {
                setPosition(i);
            }
        }
    }

    public void inititiateFragments(){
        if(fragmentManager.findFragmentById(R.id.fragment_main) == null) {
            if (savedFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.fragment_main, savedFragment, savedFragment.getTag())
                        .commit();
            } else {
                FragmentNotes fragmentNotes = getFragmentNotes();
                fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentNotes, "notes")
                        .addToBackStack("notes")
                        .commit();
            }
        }
    }

    @Override
    public void onEmptyDateDeleted() {
        retrieveTodaysDate();
        System.out.println("view model activity : dates emptied");
    }



    @Override
    public void onTodaysDateChecked(PojoDay pojoDay) {
        if(pojoDay == null){
            createTodaysDate();
        }else{
            repositoryDay.getAllDays();
        }
    }

    @Override
    public void onDateInserted() {
        repositoryDay.getAllDays();
        System.out.println("view model activity : date inserted");
    }

    @Override
    public void onDatesRetrieved(List<PojoDay> pojoDays) {
        datesLiveData.postValue(pojoDays);
    }

    public void setCurrentDay(String currentDay){
        this.currentDay = currentDay;
    }

    public String getCurrentDay(){
        return this.currentDay;
    }

    interface datesCallback{
        public void onDatesLiveDataInitialized();
    }


}
