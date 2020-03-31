package com.example.clutterrevision;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PojoBook {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private String smallThumbnail;
    private String thumbnail;
    private String link;

    public PojoBook(String title,
                    List<String> authors,
                    String publisher,
                    String publishedDate,
                    String description,
                    String smallThumbnail,
                    String thumbnail,
                    String link){

        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.smallThumbnail =  smallThumbnail;
        this.thumbnail = thumbnail;
        this.link = link;
    }
    public String getTitle(){return this.title;}
    public List<String> getAuthors(){return authors;}
    public String getPublisher(){return publisher;}
    public String getPublishedDate(){return publishedDate;}
    public String getDescription(){return description;}
    public String getSmallThumbnail(){return smallThumbnail;}
    public String getThumbnail(){return thumbnail;}
    public String getLink(){return link;}
}
