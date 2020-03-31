package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelActivity extends AndroidViewModel implements ObserverDates{

    DayHelper dayHelper;
    Fragment savedFragment;
    PojoDay pojoDay;
    private int myPosition;
    private String currentDay;
    private FragmentManager fragmentManager;
    private Context context;
    private RepositoryDay repositoryDay = new RepositoryDay(getApplication());
    public MutableLiveData<List<PojoDay>> datesLiveData = new MutableLiveData<>();

    public ViewModelActivity(@NonNull Application application) {
        super(application);
        dayHelper = DayHelper.getInstance();
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
        repositoryDay.checkForDay(dayHelper.getDateAsString());
     }

     private void createTodaysDate() {
         String dayID = dayHelper.getDateAsString();
         String dayOfWeek = dayHelper.getDayOfWeek();
         String monthOfYear = dayHelper.getMonthOfYear();
         String dayOfMonth = dayHelper.getDayOfMonth();
         String year = dayHelper.getYear();
         int numberOfNotes = 0;
         PojoDay pojoDay = new PojoDay(
                 dayID,
                 dayOfWeek,
                 monthOfYear,
                 dayOfMonth,
                 year,
                 numberOfNotes
         );
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

    public void inititiateFragments(){

        if (savedFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_main, savedFragment, savedFragment.getTag())
                    .addToBackStack(savedFragment.getTag())
                    .commit();
        } else {
            FragmentNotes fragmentNotes = getFragmentNotes();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentNotes, "notes")
                    .addToBackStack("notes")
                    .commit();
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
        System.out.println("view model activity : dates retrieved");
    }

    public void setCurrentDay(String currentDay){
        this.currentDay = currentDay;
    }

    public String getCurrentDay(){
        return this.currentDay;
    }

}
