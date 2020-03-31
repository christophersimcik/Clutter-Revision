package com.example.clutterrevision;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class RepositoryGoogleBooksAPI extends LiveData<List<PojoBook>> {
    String base = "https://www.googleapis.com/books/v1/volumes?q=";
    String addendum = "+subject&maxResults=40";
    List<PojoBook> pojoBooks = new ArrayList<>();
    EmptyListReturned emptyListReturned;
    public RepositoryGoogleBooksAPI(String query) {
        this.emptyListReturned = emptyListReturned;
        getData(query);
    }

    public void registerEmptyObservser(EmptyListReturned emptyListReturned){
       this.emptyListReturned = emptyListReturned;
    }


    public void getData(final String query) {
        new AsyncTask<Void,Void, List<PojoBook>>() {


           @Override
            protected List<PojoBook> doInBackground(Void... voids) {
                URL url = null;
                BufferedReader bufferedReader;
                HttpURLConnection connection = null;
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                try {
                    url = new URL(base + query + addendum);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
                return makeBooks(stringBuilder.toString());
            }

            @Override
            protected void onPostExecute(List<PojoBook> list) {
                if(list.size() == 0){
                    emptyListReturned.onEmptyReturned();
                }else{
                    emptyListReturned.onListReturned();
                }
                for(PojoBook pojoBook : list){
                    setValue(list);
                }
                super.onPostExecute(list);
            }
        }.execute();
    }

    private List<PojoBook> makeBooks(String jsonString) {
        String title = "";
        String publisher = "";
        String publishedDate = "";
        String description = "";
        String smallThumbnail = "";
        String thumbnail = "";
        String selfLink = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray books = jsonObject.getJSONArray("items");
            for (int i = 0; i < books.length(); i++) {

                List<String> authors = new ArrayList<>();

                JSONObject book = books.getJSONObject(i);
                if(book.has("selfLink")) {
                    selfLink = book.getString("selfLink");
                }
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                if(volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                }
                if(volumeInfo.has("publishedDate")) {
                    publishedDate = volumeInfo.getString("publishedDate");
                }
                if(volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                }
                if(volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                }
                if(volumeInfo.has("authors")) {
                    JSONArray jsonAuthors = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < jsonAuthors.length(); j++) {
                        authors.add((String) jsonAuthors.get(j));
                    }
                }
                if(volumeInfo.has("imageLinks")) {
                    JSONObject thumbs = volumeInfo.getJSONObject("imageLinks");
                    if (thumbs.has("smallThumbnail")) {
                        smallThumbnail = thumbs.getString("smallThumbnail");
                    }
                    if (thumbs.has("thumbnail")) {
                        thumbnail = thumbs.getString("thumbnail");
                    }
                }
                pojoBooks.add(new PojoBook(title, authors, publisher, publishedDate, description, smallThumbnail, thumbnail, selfLink));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pojoBooks;
    }

    public interface EmptyListReturned{
       void onEmptyReturned();
       void onListReturned();
    }


}