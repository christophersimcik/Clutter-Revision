package com.example.clutterrevision;

import android.app.Application;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModelBook extends AndroidViewModel{
    LiveData<List<PojoBook>> pojoBooks;
    RepositoryGoogleBooksAPI repositoryGoogleBooksAPI;
    private TextView textView;


    public ViewModelBook(@NonNull Application application) {
        super(application);
    }

    public void getData(Bundle bundle){
        String query;

        if(bundle != null && bundle.containsKey("query")){
            query = bundle.getString("query");
        }else{
            query = "";
        }

        repositoryGoogleBooksAPI = new RepositoryGoogleBooksAPI(query);
        pojoBooks = repositoryGoogleBooksAPI;
    }
    public void setTextView(TextView textView){
        this.textView = textView;
    }
    public void registerEmptyCallback(RepositoryGoogleBooksAPI.EmptyListReturned emptyListReturned){
        repositoryGoogleBooksAPI.registerEmptyObservser(emptyListReturned);
    }
}
