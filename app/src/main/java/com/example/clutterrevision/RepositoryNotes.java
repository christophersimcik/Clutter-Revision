package com.example.clutterrevision;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryNotes implements SubjectNotes, SubjectNote, SubjectNoteAdded {
    private Application application;
    private DatabaseClutter databaseClutter;
    private DaoNote daoNote;
    List<ObserverNotes> observers;
    List<ObserverNote> observersNote;
    List<ObserverNoteAdded> observerNoteAddedList;

    public RepositoryNotes(Application application) {
        this.application = application;
        init();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoNote = databaseClutter.getDaoNote();
        observers = new ArrayList<>();
        observerNoteAddedList = new ArrayList<>();
        observersNote = new ArrayList<>();
    }

    public void insert(final PojoNote pojoNote) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println(
                        "*** note : note id = " +
                                pojoNote.getNote_id() + " type = " +
                                pojoNote.getType() + " image = " +
                                pojoNote.getImage() + " day = " +
                                pojoNote.getNote_day()
                );
                daoNote.insert(pojoNote);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getThisDay(pojoNote.getNote_day());
                notifyNoteAdded();
                notifyNoteInserted();
            }
        }.execute();
    }

    public void delete(final PojoNote pojoNote) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoNote.delete(pojoNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getThisDay(pojoNote.getNote_day());
            }
        }.execute();
    }
    public void deleteAll(final PojoNote... pojoNotes) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoNote.deleteAll(pojoNotes);
                return null;
            }
        };
        asyncTask.execute();
    }

    public void update(final PojoNote pojoNote) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoNote.update(pojoNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getThisDay(pojoNote.getNote_day());
                notifyNoteUpdated();
            }
        }.execute();
    }

    public void getThisDay(final String dayID) {
        new AsyncTask<Void, Void, List<PojoNote>>() {
            @Override
            protected List<PojoNote> doInBackground(Void... voids) {
                return daoNote.getDaysNotes(dayID);
            }

            @Override
            protected void onPostExecute(List<PojoNote> pojoNotes) {
                super.onPostExecute(pojoNotes);
                notifyNotesRetrieved(pojoNotes);
            }
        }.execute();
    }

    public LiveData<PojoNote> getThisNote(final String noteID){
        return daoNote.getNote(noteID);
    }

    @Override
    public void register(ObserverNotes observerNotes) {
        observers.add(observerNotes);
    }

    @Override
    public void unregister(ObserverNotes observerNotes) {
        observers.remove(observerNotes);
    }

    @Override
    public void notifyNotesRetrieved(List<PojoNote> listOfPojoNotes) {
        for(ObserverNotes observerNotes : observers){
            observerNotes.onDayRetrieved(listOfPojoNotes);
        }
    }

    @Override
    public void notifyNoteInserted() {
        for(ObserverNote observerNote : observersNote){
            observerNote.onNoteInserted();
        }
    }

    @Override
    public void notifyNoteUpdated() {
        for(ObserverNote observerNote : observersNote){
            observerNote.onNoteUpdated();
        }
    }


    @Override
    public void register(ObserverNote observerNote) {
        observersNote.add(observerNote);
    }

    @Override
    public void unregister(ObserverNote observerNote) {

    }

    @Override
    public void notifyObservers(PojoNote pojoNote) {
        for(ObserverNote observerNote : observersNote){

            observerNote.onNoteRetrieved(pojoNote);
        }
    }

    @Override
    public void register(ObserverNoteAdded observerNoteAdded) {
       observerNoteAddedList.add(observerNoteAdded);
    }

    @Override
    public void unregister(ObserverNoteAdded observerNoteAdded) {

    }

    @Override
    public void notifyNoteAdded() {
        for(ObserverNoteAdded observerNoteAdded : observerNoteAddedList){
            observerNoteAdded.onNoteRetrieved();
        }

    }
}
