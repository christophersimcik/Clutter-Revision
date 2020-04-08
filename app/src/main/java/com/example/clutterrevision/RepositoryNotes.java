package com.example.clutterrevision;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryNotes implements SubjectNotes, SubjectNote, SubjectNoteAdded {

    private Application application;
    private DatabaseClutter databaseClutter;
    private DaoNote daoNote;
    private DaoDay daoDay;
    List<ObserverNotes> observers;
    List<ObserverNote> observersNote;
    List<ObserverNoteAdded> observerNoteAddedList;
    RepositoryDay repositoryDay;

    public RepositoryNotes(Application application) {
        this.application = application;
        repositoryDay = new RepositoryDay(application);
        init();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoNote = databaseClutter.getDaoNote();
        daoDay = databaseClutter.getDaoDay();
        observers = new ArrayList<>();
        observerNoteAddedList = new ArrayList<>();
        observersNote = new ArrayList<>();
    }

    public void insert(final PojoNote pojoNote) {
        new AsyncTask<PojoNote, Void,String>() {
            @Override
            protected String doInBackground(PojoNote... pojoNotes) {
                daoNote.insert(pojoNotes[0]);
                String dayId = pojoNotes[0].getNote_day();
                switch(pojoNotes[0].getType()){
                    case Constants.TYPE_NOTE:
                        repositoryDay.addNote(dayId);
                        break;
                    case Constants.TYPE_AUDIO:
                        repositoryDay.addAudio(dayId);
                        break;
                    case Constants.TYPE_REFERENCE:
                        repositoryDay.addReference(dayId);
                        break;
                    case Constants.TYPE_CHECKLIST:
                        repositoryDay.addList(dayId);
                        break;
                    case Constants.TYPE_GOOGLE_BOOKS:
                        repositoryDay.addBook(dayId);
                        break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getThisDay(s);
                notifyNoteAdded();
                notifyNoteInserted();
            }
        }.execute(pojoNote);
    }

    public void delete(final PojoNote pojoNote) {
        new AsyncTask<PojoNote, Void, String >() {
            @Override
            protected String doInBackground(PojoNote... pojoNotes) {
                daoNote.delete(pojoNotes[0]);
                String dayId = pojoNote.getNote_day();
                switch(pojoNote.getType()){
                    case Constants.TYPE_NOTE:
                        repositoryDay.deleteNote(dayId);
                        break;
                    case Constants.TYPE_AUDIO:
                        repositoryDay.deleteAudio(dayId);
                        deleteFiles(pojoNotes[0].getContent(),pojoNotes[0].getImage());
                        break;
                    case Constants.TYPE_REFERENCE:
                        repositoryDay.deleteReference(dayId);
                        break;
                    case Constants.TYPE_CHECKLIST:
                        repositoryDay.deleteList(dayId);
                        break;
                    case Constants.TYPE_GOOGLE_BOOKS:
                        repositoryDay.deleteBook(dayId);
                        break;
                }
                return dayId;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getThisDay(s);
            }
        }.execute(pojoNote);
    }

    public void deleteAll(final PojoNote... pojoNotes) {
        AsyncTask<PojoNote[], Void, Void> asyncTask = new AsyncTask<PojoNote[], Void, Void>() {
            @Override
            protected Void doInBackground(PojoNote[]... pojoNotes) {
                daoNote.deleteAll(pojoNotes[0]);
                return null;
            }
        };
        asyncTask.execute(pojoNotes);
    }

    public void update(final PojoNote pojoNote) {
        new AsyncTask<PojoNote, Void,PojoNote>() {
            @Override
            protected PojoNote doInBackground(PojoNote... pojoNotes) {
                daoNote.update(pojoNotes[0]);
                return pojoNotes[0];
            }

            @Override
            protected void onPostExecute(PojoNote pojoNote) {
                super.onPostExecute(pojoNote);
                getThisDay(pojoNote.getNote_day());
                notifyNoteUpdated();
            }
        }.execute(pojoNote);
    }

    public void getThisDay(final String dayID) {
        new AsyncTask<String, Void, List<PojoNote>>() {
            @Override
            protected List<PojoNote> doInBackground(String... strings) {
                return daoNote.getDaysNotes(strings[0]);
            }

            @Override
            protected void onPostExecute(List<PojoNote> pojoNotes) {
                super.onPostExecute(pojoNotes);
                notifyNotesRetrieved(pojoNotes);
            }
        }.execute(dayID);
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

    private void deleteFiles(String audioPath, String imagePath){
        File audioFile = new File(audioPath);
        File imageFile = new File(imagePath);
        if(audioFile.exists()){
            audioFile.delete();
            Log.i("file deletion", " audio file deleted ?= " + audioFile.exists());
        }else{
            Log.i("file deletion", " cant find audio file to delete at " + audioPath);
        }
        if(imageFile.exists()){
            imageFile.delete();
            Log.i("file deletion", " audio image file deleted ?= " + imageFile.exists());
        }else{
            Log.i("file deletion", " cant find audio image file to delete at " + imagePath);
        }
    }
}
