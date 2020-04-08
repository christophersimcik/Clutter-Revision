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

    public void addNote(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.addNote(strings[0]);
                System.out.println("*** a d d i n g ");
                return null;
            }
        }.execute(string);
    }

    public void addAudio(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.addAudio(strings[0]);
                System.out.println("*** a d d i n g");
                return null;
            }
        }.execute(string);
    }

    public void addReference(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.addReference(strings[0]);
                System.out.println("*** a d d i n g ");
                return null;
            }
        }.execute(string);
    }

    public void addList(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.addList(strings[0]);
                System.out.println("*** a d d i n g ");
                return null;
            }
        }.execute(string);
    }

    public void addBook(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.addBook(strings[0]);
                System.out.println("*** a d d i n g ");
                return null;
            }
        }.execute(string);
    }

    public void deleteNote(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.deleteNote(strings[0]);
                System.out.println("*** d e l e t i n g");
                return null;
            }
        }.execute(string);
    }

    public void deleteAudio(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.deleteAudio(strings[0]);
                System.out.println("*** d e l e t i n g");
                return null;
            }
        }.execute(string);
    }

    public void deleteReference(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.deleteReference(strings[0]);
                System.out.println("*** d e l e t i n g");
                return null;
            }
        }.execute(string);
    }

    public void deleteList(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.deleteList(strings[0]);
                System.out.println("*** d e l e t i n g");
                return null;
            }
        }.execute(string);
    }

    public void deleteBook(String string){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                daoDay.deleteBook(strings[0]);
                System.out.println("*** d e l e t i n g b o o k");
                return null;
            }
        }.execute(string);
    }



    public RepositoryDay(Application application) {
        this.application = application;
        this.name = name;
        init();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoDay = databaseClutter.getDaoDay();
    }

    public LiveData<List<PojoDay>> getAllDaysAsLiveData(){
        return daoDay.getAllDaysAsLiveData();
    }

    public void insert(final PojoDay pojoDay) {
        AsyncTask<PojoDay, Void, Void> asyncTask = new AsyncTask<PojoDay, Void, Void>() {
            @Override
            protected Void doInBackground(PojoDay... pojoDays) {
                daoDay.insert(pojoDays[0]);
                System.out.println("*** i n s e r t i n g ");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                notifyDateInserted();
            }
        };
        asyncTask.execute(pojoDay);
    }

    public void delete(final PojoDay pojoDay) {
        AsyncTask<PojoDay, Void, Void> asyncTask = new AsyncTask<PojoDay, Void, Void>() {
            @Override
            protected Void doInBackground(PojoDay... pojoDays) {
                daoDay.delete(pojoDays[0]);
                return null;
            }
        };
        asyncTask.execute(pojoDay);
    }

    public void deleteAll(final PojoDay... pojoDays) {
        AsyncTask<PojoDay[], Void, Void> asyncTask = new AsyncTask<PojoDay[], Void, Void>() {
            @Override
            protected Void doInBackground(PojoDay[]... pojoDays) {
                daoDay.deleteAll(pojoDays[0]);
                return null;
            }
        };
        asyncTask.execute(pojoDays);
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
                notifyEmptiesDeleted();
            }
        }.execute();
    }

    public void update(final PojoDay pojoDay) {
        new AsyncTask<PojoDay, Void, Void>() {
            @Override
            protected Void doInBackground(PojoDay... pojoDays) {
                daoDay.update(pojoDays[0]);
                return null;
            }
        }.execute(pojoDay);
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
                System.out.println("ooo = " + list.get(list.size()-1).getNumberOfNotes());

            }
        }.execute();
    }

    public void checkForDay(String id){
        new AsyncTask<String,Void,PojoDay>(){
            @Override
            protected PojoDay doInBackground(String... strings) {
                return daoDay.getThisDay(strings[0]);
            }

            @Override
            protected void onPostExecute(PojoDay pojoDay) {
                super.onPostExecute(pojoDay);
                notifyDateChecked(pojoDay);
            }
        }.execute(id);
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
