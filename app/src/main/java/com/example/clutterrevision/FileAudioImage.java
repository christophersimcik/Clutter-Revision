package com.example.clutterrevision;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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

public class FileAudioImage extends LiveData {

    private ObjectOutputStream outputStream;
    private ObjectInputStream  inputStream;
    public File audioImageFile = null;
    public List<Integer> data;
    public String time;
    public FileAudioImage(){
    }

    public void outputAudioImageData(Activity a , List<Integer> list, String s){
        List<Integer> l = new ArrayList<>(list);
        Collections.copy(l,list);
        if(audioImageFile == null) {
            audioImageFile = createTemporaryFile(a);
            setValue(audioImageFile);
        }
        try {
            outputStream.writeObject(l);
            outputStream.writeObject(s);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
            }
        }.execute();
    }

    private  File createTemporaryFile(Activity a) {
        String path = a.getExternalFilesDir(null).getAbsolutePath();
        String suffix = ".txt";
        String name = timeStamp();
        File audioImg = new File(path,name + suffix);
        try {
            if(audioImg != null) {
                outputStream = new ObjectOutputStream(new FileOutputStream(audioImg.getAbsolutePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return audioImg;
    }

    private String timeStamp(){
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM'_'dd'_'yy'_'hh'_'mm'_'ss");
        return date.toString(fmt);
    }
}
