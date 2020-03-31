package com.example.clutterrevision;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelReference extends AndroidViewModel implements ObserverNote {

    TitleHelper titleHelper;
    MutableLiveData<CharSequence> titleText;
    LiveData<PojoNote> thisNote;
    RepositoryNotes repositoryNotes;
    RepositoryDay repositoryDay;
    DayHelper dayHelper = DayHelper.getInstance();

    PojoNote pojoNote;
    PojoDay pojoDay = null;

    String id;
    String title = "";
    String content = "";
    int type = Constants.TYPE_REFERENCE;

    public ViewModelReference(@NonNull Application application) {
        super(application);
        titleHelper = new TitleHelper(application);
        titleText = new MutableLiveData<>();
        repositoryNotes = new RepositoryNotes(application);
        repositoryNotes.register(this);
        repositoryDay = new RepositoryDay(application);
    }

    public void setPojoDay(PojoDay pojoDay){
        this.pojoDay = pojoDay;
    }

    public void insert(PojoNote newNote){
        this.pojoNote = newNote;
        if(newNote.getImage().equals("")){
            newNote.setImage("No Title");
        }
        if(!newNote.getContent().equals("")) {
            repositoryNotes.insert(newNote);
        }
    }

    public void update(){
        pojoNote.setImage(title);
        pojoNote.setContent(content);
        if(pojoNote.getContent().equals("") || pojoNote.getContent() == null) {
            repositoryNotes.delete(pojoNote);
        }else {
            if(pojoNote.getImage().equals("") || pojoNote.getImage() == null) {
                pojoNote.setImage("No Title");
            }
            repositoryNotes.update(pojoNote);
        }
    }

    public void getNote(){
        if(id != null) {
            thisNote = repositoryNotes.getThisNote(id);
        }
    }


    public void setReferenceTitle(CharSequence cs) {
        if (titleHelper.limitText(cs) == false) {
            title = cs.toString();
        } else {
            if (cs.length() > title.length() && title.charAt(title.length() - 1) != '.') {
                title = title + "...";
            }
        }
        content = cs.toString();
        titleText.postValue(title);
    }

    public PojoNote createNote(){
        String id = String.valueOf(System.currentTimeMillis());
        String content = this.content;
        String image = this.title;
        int type = this.type;
        String day = dayHelper.getDateAsString();
        return new PojoNote(id,content,image,type,day);
    }

    public void updateDay(){
        if(this.pojoDay != null){
            int updatedCount = pojoDay.getNumberOfNotes() + 1;
            this.pojoDay.setNumberOfNotes(updatedCount);
            repositoryDay.update(this.pojoDay);
        }
    }


    @Override
    public void onNoteRetrieved(PojoNote pojoNote) {

    }

    @Override
    public void onNoteInserted() {
        updateDay();

    }

    @Override
    public void onNoteUpdated() {
        updateDay();
    }
}
