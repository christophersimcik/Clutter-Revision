package com.example.clutterrevision;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = PojoDay.class,
        parentColumns = "day_id",
        childColumns = "note_day",
        onDelete = CASCADE))

public class PojoNote {

    @NonNull
    @PrimaryKey()
    private String note_id;

    @ColumnInfo(name = "note_content")
    private String content;

    @ColumnInfo(name = "note_image")
    private String image;

    @ColumnInfo(name = "note_type")
    private int type;

    @ColumnInfo(name = "note_day", index = true)
    private String note_day;


    public PojoNote(String note_id, String content, String image, int type, String note_day) {
        this.note_id = note_id;
        this.content = content;
        this.image = image;
        this.type = type;
        this.note_day = note_day;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_id() {
        return this.note_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setNote_day(String day) { this.note_day = day; }

    public String getNote_day(){return note_day;}

    @Override
    public String toString() {
        return note_id+"/"+content+"/"+image+"/"+type+"/"+note_day;
    }

}
