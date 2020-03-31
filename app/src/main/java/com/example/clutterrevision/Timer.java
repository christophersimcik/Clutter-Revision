package com.example.clutterrevision;

import android.database.DatabaseUtils;
import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;

public class Timer extends LiveData{
    ScheduledExecutorService scheduledExecutorService;
    String time = "";

    public Timer(){}


        public void startTimer(){
           scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
           scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            },0,1,TimeUnit.SECONDS);

        }

        public void stopTimer(){
            if(scheduledExecutorService != null){
                scheduledExecutorService.shutdown();
            }
        }
        public void updateTime(int i){
        time = convertToString(i);
        setValue(time);
        }

        private String convertToString(int i){

        long m = convertToLong(i);

        long hundreths = m%100;
        long seconds = (m/1000)%60;
        long minutes = (m/(1000*60))%60;
        long hours = (m/(1000*60*60))%24;

        String huns, secs, mins, hrs;

        if(hundreths < 10){ huns = "0" + hundreths;}
        else{
            huns = String.valueOf(hundreths);
        }

        if(seconds < 10 ){ secs = "0"+ seconds; }
        else{
            secs = String.valueOf(seconds);
        }

        if(minutes < 10 ){mins = "0"+ minutes; }
        else{
            mins = String.valueOf(minutes);
        }

        if(hours < 10){ hrs = "0"+ hours;}
        else{
            hrs = String.valueOf(hours);
        }

        return hrs+"."+mins+"."+secs+"."+huns;

    }
    private long convertToLong(int i){
        int rate = AudioRecordHelper.RECORDER_SAMPLERATE/1000;
        int size = AudioRecordHelper.SAMPLE_SIZE;
        // millis per sample
        int mps = size/rate;
        return Long.valueOf(i*mps);
    }
}
