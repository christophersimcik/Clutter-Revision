package com.example.clutterrevision;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositorySearch  {
    private Application application;
    private DatabaseClutter databaseClutter;
    private DaoNote daoNote;

    public RepositorySearch(Application application) {
        this.application = application;
        init();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoNote = databaseClutter.getDaoNote();
    }

    public LiveData<List<PojoNote>> searchAll(String string){
        System.out.println("*** " + "get by title = " + string);
        return daoNote.searchAll(string);
    }

    public LiveData<List<PojoNote>> getAll(){
        System.out.println("*** " + "get all = ");
        return daoNote.getAllNotes();
    }

}
