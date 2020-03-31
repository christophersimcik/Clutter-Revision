package com.example.clutterrevision;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryDay implements SubjectDates{
    private Application application;
    private DatabaseClutter databaseClutter;
    private DaoDay daoDay;
    private List<ObserverDates> observerDates = new ArrayList<>();
    String name;

    public RepositoryDay(Application application) {
        this.application = application;
        this.name = name;
        init();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoDay = databaseClutter.getDaoDay();
    }

    public void insert(final PojoDay pojoDay) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println(
                        "*** note : day id = " +
                                pojoDay.getDay_id() + " mnth = " +
                                pojoDay.getDayOfMonth() + " week = " +
                                pojoDay.getDayOfWeek() + " year = " +
                                pojoDay.getYear()
                );
                daoDay.insert(pojoDay);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                notifyDateInserted();
            }
        };
        asyncTask.execute();
    }

    public void delete(final PojoDay pojoDay) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoDay.delete(pojoDay);
                System.out.println("day ");
                return null;
            }
        };
        asyncTask.execute();
    }
    public void deleteAll(final PojoDay... pojoDays) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoDay.deleteAll(pojoDays);
                return null;
            }
        };
        asyncTask.execute();
    }

    public void deleteEmpty(){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                daoDay.deleteEmpties(0);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                System.out.println("*** main_activity dates deleted");
                notifyEmptiesDeleted();
            }
        }.execute();
    }

    public void update(final PojoDay pojoDay) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoDay.update(pojoDay);
                System.out.println("updating ok ");
                return null;
            }
        }.execute();
    }

    public void getAllDays() {
        new AsyncTask<Void, List<PojoDay>, List<PojoDay>>(){
            @Override
            protected List<PojoDay> doInBackground(Void... voids) {
               return daoDay.getAllDays();
            }

            @Override
            protected void onPostExecute(List<PojoDay> list) {
                super.onPostExecute(list);
                notifyDatesRetrieved(list);

            }
        }.execute();
    }

    public void checkForDay(final String id){
        new AsyncTask<Void,Void,PojoDay>(){
            @Override
            protected PojoDay doInBackground(Void... voids) {
                System.out.println("*** " + "check for day");
                return daoDay.getThisDay(id);
            }

            @Override
            protected void onPostExecute(PojoDay pojoDay) {
                super.onPostExecute(pojoDay);
                notifyDateChecked(pojoDay);
            }
        }.execute();
    }

    @Override
    public void register(ObserverDates observerDates) {
        this.observerDates.add(observerDates);
        System.out.println("*** repository_day " + this.observerDates.size());
    }

    @Override
    public void unregister(ObserverDates observerDates) {

    }

    @Override
    public void notifyEmptiesDeleted() {
        for(ObserverDates observerDates : this.observerDates){
            observerDates.onEmptyDateDeleted();
        }
    }

    @Override
    public void notifyDateChecked(PojoDay pojoDay) {
        for(ObserverDates observerDates : this.observerDates){
            observerDates.onTodaysDateChecked(pojoDay);
        }
    }

    @Override
    public void notifyDateInserted() {
        for(ObserverDates observerDates : this.observerDates){
            observerDates.onDateInserted();
        }
    }

    @Override
    public void notifyDatesRetrieved(List<PojoDay> listPojoDays) {
        for(ObserverDates observerDates : this.observerDates){
            observerDates.onDatesRetrieved(listPojoDays);
        }
    }
}
