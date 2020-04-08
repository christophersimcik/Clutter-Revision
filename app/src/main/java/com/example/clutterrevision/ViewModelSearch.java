package com.example.clutterrevision;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModelSearch extends AndroidViewModel{
    private String keySearchQuery = Constants.SEARCH_QUERY;
    private String query;
    LiveDataCheck liveDataCheck;
    LiveData<List<PojoNote>> listOfNotes;
    private RepositorySearch repositorySearch;



    public ViewModelSearch(@NonNull Application application) {
        super(application);
        repositorySearch = new RepositorySearch(application);
    }

    public void search(String string){
        listOfNotes = repositorySearch.searchAll(string);
        liveDataCheck.liveDataInitiated();
    }

    private String getQuery(Bundle bundle){
        if(bundle == null){ return "";}
        if(bundle.containsKey(keySearchQuery)){
            String string = bundle.getString(keySearchQuery);
            query = string;
            return string;
        }else{
            return "";
        }
    }

    public void getLiveData(Bundle bundle) {
        if (bundle == null) {
            return;
        }
    }

    public void getAudioImage(String s, VisualAudio view) {
        RepositoryAudioImageData fileAudioImage = new RepositoryAudioImageData();
        fileAudioImage.inputAudioImageData(s);
        fileAudioImage.register(view);
    }

    public void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void registerLiveDataCheck(LiveDataCheck liveDataCheck){
        this.liveDataCheck = liveDataCheck;
    }

    public interface LiveDataCheck{
        void liveDataInitiated();
    }

    public void getMessage(int size, final TextView statusText, final ImageView animation, final AnimationDrawable animationDrawable){
        if(size <= 0){
            animation.setVisibility(View.VISIBLE);
            animationDrawable.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                Boolean ran = false;
                @Override
                public void run() {
                    if(ran){
                        animation.setVisibility(View.INVISIBLE);
                        statusText.setText("");
                    }else {
                        animationDrawable.stop();
                        animationDrawable.selectDrawable(0);
                        ran = true;
                        handler.postDelayed(this,1000);

                    }
                }
            },800);
           statusText.setText("No notes found");
        }else{
            animation.setVisibility(View.INVISIBLE);
            statusText.setText("");
        }
    }

    public String getQuery(){
        return this.query;
    }

}
