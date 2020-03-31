package com.example.clutterrevision;

import android.app.Application;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModelNote extends AndroidViewModel {

    TitleHelper titleHelper;
    MutableLiveData<CharSequence> titleText;
    Boolean defaultTitle = false;
    Boolean newNote = true;
    DayHelper dayHelper = DayHelper.getInstance();
    RepositoryNotes repositoryNotes;
    RepositoryDay repositoryDay;
    PojoDay pojoDay = null;

    String title = "";
    String id;
    String content = "";

    LiveData<PojoNote> thisNote;

    public PojoNote pojoNote = null;


    public ViewModelNote(@NonNull Application application) {
        super(application);
        titleHelper = new TitleHelper(application);
        titleText = new MutableLiveData<>();
        repositoryNotes = new RepositoryNotes(application);
        repositoryDay = new RepositoryDay(application);
        pojoNote = createNote();
    }

    public void insert(PojoNote newNote) {
        if (newNote.getImage().equals("")) {
            newNote.setImage("No Title");
        }
        if (!newNote.getContent().equals("")) {
            repositoryNotes.insert(newNote);
        }
    }

    public void update() {
        if (pojoNote.getContent().equals("") || pojoNote.getContent() == null) {
            repositoryNotes.delete(pojoNote);
        } else {
            if (pojoNote.getImage().equals("") || pojoNote.getImage() == null) {
                pojoNote.setImage("No Title");
            }
            repositoryNotes.update(pojoNote);
        }
    }

    public void getNote() {
        if (id != null) {
            thisNote = repositoryNotes.getThisNote(id);
        }
    }

    public void setNoteTitle(CharSequence cs) {
        if (defaultTitle == true) {
            if (titleHelper.limitText(cs) == false) {
                title = cs.toString();
            } else {
                if (cs.length() > title.length() && title.charAt(title.length() - 1) != '.') {
                    title = title + "...";
                }
            }
            titleText.postValue(title);
        }
    }

    public PojoNote createNote() {
        String id = String.valueOf(System.currentTimeMillis());
        String content = this.content;
        String image = this.title;
        int type = Constants.TYPE_NOTE;
        String day = dayHelper.getDateAsString();
        return new PojoNote(id, content, image, type, day);
    }

    public void setPojoDay(PojoDay pojoDay) {
        this.pojoDay = pojoDay;
    }

    public PojoDay getPojoDay() {
        return this.pojoDay;
    }

    public void updateDay() {
        if (this.pojoDay != null) {
            int updatedCount = pojoDay.getNumberOfNotes() + 1;
            this.pojoDay.setNumberOfNotes(updatedCount);
            repositoryDay.update(this.pojoDay);
        }
    }

    public TextWatcher createTitleWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    pojoNote.setImage(editable.toString());
            }
        };
    }

    public void unBundler(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey("id")) {
                id = bundle.getString("id");
                getNote();
            }
            if (bundle.containsKey("new_note")) {
                newNote = bundle.getBoolean("new_note");
                if (newNote) {
                    defaultTitle = true;
                } else {
                    defaultTitle = false;
                }
            }
        }

    }
}
