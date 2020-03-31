package com.example.clutterrevision;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryChecklist implements SubjectChecklist {
    private Application application;
    private DatabaseClutter databaseClutter;
    private DaoChecklist daoChecklist;
    public LiveData<List<PojoCheckList>> checklist;
    List<ObserverChecklist> observerChecklists;
    String name;

    public RepositoryChecklist(Application application, String name) {
        this.application = application;
        this.name = name;
        init();
        checklist = daoChecklist.getallItems();
        observerChecklists = new ArrayList<>();
    }

    private void init() {
        databaseClutter = DatabaseClutter.getInstance(application);
        daoChecklist = databaseClutter.getDaoChecklist();
    }

    public void insert(final PojoCheckList pojoCheckList) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoChecklist.insert(pojoCheckList);
                return null;
            }
        };
        asyncTask.execute();
    }

    public void delete(final PojoCheckList pojoCheckList) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoChecklist.delete(pojoCheckList);
                return null;
            }
        };
        asyncTask.execute();
    }
    public void deleteAll(final PojoCheckList... pojoCheckLists) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoChecklist.deleteAll(pojoCheckLists);
                return null;
            }
        };
        asyncTask.execute();
    }

    public void update(final PojoCheckList pojoCheckList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                daoChecklist.update(pojoCheckList);
                System.out.println("updating ok ");
                return null;
            }
        }.execute();
    }

    public LiveData<List<PojoCheckList>> getChecklist(String name) {
            notifyChecklistObservers();
            return daoChecklist.getThisList(name);
            }

    @Override
    public void register(ObserverChecklist observerChecklist) {
      observerChecklists.add(observerChecklist);
    }

    @Override
    public void unregister(ObserverChecklist observerChecklist) {

    }

    @Override
    public void notifyChecklistObservers() {
        for(ObserverChecklist observed : observerChecklists){
            observed.onChecklisteRetrieved();
        }
    }
}


