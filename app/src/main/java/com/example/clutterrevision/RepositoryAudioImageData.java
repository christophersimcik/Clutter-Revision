package com.example.clutterrevision;

import android.app.Activity;
import android.os.AsyncTask;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryAudioImageData extends LiveData implements SubjectAudioImage {

    private ObjectInputStream  inputStream;
    private List<ObserverAudioImage> observers;
    public List<Integer> data;
    public String time;
    public RepositoryAudioImageData(){
        observers = new ArrayList<>();
    }

    public void inputAudioImageData(final String s) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(s);
                    inputStream = new ObjectInputStream(fileInputStream);
                    data = (List<Integer>) inputStream.readObject();
                    time = String.valueOf(inputStream.readObject());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return  null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                notifyObservers();
            }
        }.execute();
    }

    @Override
    public void register(ObserverAudioImage observerAudioImage) {
        observers.add(observerAudioImage);
    }

    @Override
    public void unregister(ObserverAudioImage observerAudioImage) {

    }

    @Override
    public void notifyObservers() {
        System.out.println("*** = " + " observers size =  " + observers.size());
        for(ObserverAudioImage observerAudioImage : observers){
            System.out.println("*** = " + " notify called ");
            observerAudioImage.onImageDataRetrieved(time, data);
        }

    }
}
