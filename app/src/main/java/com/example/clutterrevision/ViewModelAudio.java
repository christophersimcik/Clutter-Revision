package com.example.clutterrevision;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelAudio extends AndroidViewModel implements ObserverNote {
    public LiveData values;
    public LiveData time;
    public PojoNote note;
    public MutableLiveData<Boolean> canStart = new MutableLiveData<>();
    public MutableLiveData<Boolean> canStop = new MutableLiveData<>();
    public MutableLiveData<Boolean> canSubmit = new MutableLiveData<>();
    Timer timer;
    AudioRecordHelper audioRecordHelper;
    FileHelper fileHelper;
    FileAudioImage fileAudioImage;
    LiveData<File> fileImage;
    LiveData<File> fileContent;
    RepositoryNotes repositoryNote;
    RepositoryDay repositoryDay;
    DayHelper dayHelper = DayHelper.getInstance();
    String trialList;
    String triaLString;
    private PojoDay pojoDay;

    public ViewModelAudio(@NonNull Application application) {
        super(application);
        timer = new Timer();
        fileHelper = new FileHelper();
        fileAudioImage = new FileAudioImage();
        audioRecordHelper = new AudioRecordHelper(fileHelper);
        repositoryNote = new RepositoryNotes(application);
        repositoryNote.register(this);
        repositoryDay = new RepositoryDay(application);
        values = audioRecordHelper;
        time = timer;
        fileImage = fileAudioImage;
        fileContent = fileHelper;
        onInitButtons();
    }

    public void start(){
        audioRecordHelper.record();
        timer.startTimer();
        onStartButtons();

    }
    public void getData(){
        fileAudioImage.inputAudioImageData(fileAudioImage.audioImageFile.getAbsolutePath());
        trialList = fileAudioImage.audioImageFile.getAbsolutePath();
        triaLString = fileAudioImage.time;
    }

    public void getTime(){
        timer.updateTime(audioRecordHelper.values.size());
    }

    public void stop(Activity a){
        audioRecordHelper.stop();
        timer.stopTimer();
        onStopButtons();
    }

    public void submit(Activity a){
        fileAudioImage.outputAudioImageData(a,audioRecordHelper.values,timer.time);
        audioRecordHelper.stop();
        timer.stopTimer();
        onStopButtons();
        release();
    }

    public void createTemporaryFile(Activity a){
         fileHelper.makeTempFile(a);
    }

    public void writeFile(Activity a) throws IOException {
       if(fileHelper.audioFile == null){
          fileHelper.makeFile(a);
        }
        fileHelper.writeWav();
    }

    public void stopTimer(){
        timer.stopTimer();
    }

    public void release(){
        audioRecordHelper.releaseAudioRecord();
    }

    public void insert(PojoNote pojoNote){
        repositoryNote.insert(pojoNote);
    }
    public void setPojoDay(PojoDay pojoDay) {
        this.pojoDay = pojoDay;
    }

    @Override
    public void onNoteRetrieved(PojoNote pojoNote) {
        this.note = pojoNote;
    }

    @Override
    public void onNoteInserted() {
    }

    @Override
    public void onNoteUpdated() {
    }

    public PojoNote createNote(){
        String id = String.valueOf(System.currentTimeMillis());
        String content = fileHelper.audioFile.getAbsolutePath();
        String image = fileAudioImage.audioImageFile.getAbsolutePath();
        int type = Constants.TYPE_AUDIO;
        String day = dayHelper.getDateAsString();
        return new PojoNote(id,content,image,type,day);
    }

    private void onInitButtons(){
        System.out.println("view_model_audio = " + "init");
        canStart.postValue(true);
        canStop.postValue(false);
        canSubmit.postValue(false);
    }
    private void onStartButtons(){
        System.out.println("view_model_audio = " + "start");
        canStart.postValue(false);
        canStop.postValue(true);
        canSubmit.postValue(false);
    }
    private void onStopButtons(){
        System.out.println("view_model_audio = " + "stop");
        canStart.postValue(true);
        canStop.postValue(false);
        canSubmit.postValue(true);

    }



}
