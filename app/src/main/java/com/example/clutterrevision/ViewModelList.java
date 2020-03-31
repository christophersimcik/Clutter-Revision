package com.example.clutterrevision;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;

import com.example.clutterrevision.Constants;
import com.example.clutterrevision.DayHelper;
import com.example.clutterrevision.ObserverNoteAdded;
import com.example.clutterrevision.PojoCheckList;
import com.example.clutterrevision.PojoNote;
import com.example.clutterrevision.RepositoryChecklist;
import com.example.clutterrevision.RepositoryNotes;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiEditText;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModelList extends AndroidViewModel {
    public RepositoryChecklist repositoryChecklist;
    private RepositoryNotes repositoryNotes;
    public String checkListName;
    public String noteTitle;
    private PojoNote thisNote;
    public LiveData<List<PojoCheckList>> checklist;
    public LiveData<PojoNote> noteContainingCheckList;
    Boolean running = false;
    Boolean titleSet = false;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                if(thisNote != null) {
                    repositoryNotes.update(thisNote);
                }
                    running = false;
            }
    };

    public void startListening(){
        if(!getRunning()){
            handler.postDelayed(runnable,500);
            setRunning(true);
        }
    }
    public void setTitle(EmojiEditText emojiEditText){
        if(!titleSet){
            emojiEditText.setText(noteTitle);
            titleSet = true;
        }
    }

    public ViewModelList(@NonNull Application application) {
        super(application);
        repositoryChecklist = new RepositoryChecklist(application, checkListName);
        repositoryNotes = new RepositoryNotes(application);
    }

    public void getList() {
        if (checkListName != null) {
            checklist = repositoryChecklist.getChecklist(checkListName);
            noteContainingCheckList = repositoryNotes.getThisNote(checkListName);
        }
    }

    public void delete(PojoCheckList pojoCheckList) {

        repositoryChecklist.delete(pojoCheckList);
    }

    public void changeNoteTitle(Editable editable){
        if(thisNote != null){
            if(!editable.toString().equals("")){
                thisNote.setImage(editable.toString());
            }
        }
    }

    public void deleteAll(PojoCheckList... items) {

        repositoryChecklist.deleteAll(items);
    }

    public void updateNote(PojoNote pojoNote){
        repositoryNotes.update(pojoNote);
    }

    public void update(PojoCheckList pojoCheckList) {

        repositoryChecklist.update(pojoCheckList);
    }

    public void insert(PojoCheckList pojoCheckList) {
        repositoryChecklist.insert(pojoCheckList);
    }


    public void addCheckBox() {
        insert(createCheckBox());
    }

    public PojoCheckList createCheckBox() {
        int id = 0;
        String parent = checkListName;
        Boolean checked = false;
        String description = "";
        return new PojoCheckList(id, checked, description, parent);
    }

    public void setThisNote(PojoNote thisNote){
        this.thisNote = thisNote;
    }

    public void setRunning(Boolean running){
        this.running = running;
    }
    public Boolean getRunning(){
        return this.running;
    }
}
