package com.example.clutterrevision;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

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

    public void insert(PojoNote newNote, Context context) {
        if (newNote.getImage().equals("")) {
            newNote.setImage("No Title");
        }
        if (!newNote.getContent().equals("")) {
            repositoryNotes.insert(newNote);
        }else{
            noteRejectedWarning(context);
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

    public String parseDate(String day){
        String date;
        if(day == null){
            date = dayHelper.getDateAsString();
        }else{
            date = day;
        }

        String[] dateArray = new String[3];
        dateArray[0] = Constants.monthsOfYear.get(Integer.parseInt(date.substring(0,2)));
        dateArray[1] = date.substring(2,4);
        dateArray[2] = date.substring(4,8);
        return "Date: " + dateArray[0]+" "+dateArray[1]+", "+dateArray[2];
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

    private void noteRejectedWarning(Context context){
        Toast.makeText(context,"No Content - Deleted",Toast.LENGTH_SHORT).show();
    }
}
