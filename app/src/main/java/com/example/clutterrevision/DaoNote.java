package com.example.clutterrevision;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoNote {
    @Query("SELECT * FROM PojoNote WHERE note_type = :type")
    LiveData<List<PojoNote>> getType(String type);

    @Query("SELECT * FROM PojoNote WHERE note_day = :dayID ")
    List<PojoNote> getDaysNotes(String dayID);

    @Query("SELECT * FROM PojoNote WHERE note_id = :noteID ")
    LiveData<PojoNote> getNote(String noteID);

    @Query("SELECT * FROM PojoNote")
    LiveData<List<PojoNote>> getAllNotes();

    @Insert
    void insert(PojoNote pojoNote);

    @Update
    void update(PojoNote pojoNote);

    @Delete
    void delete(PojoNote pojoNote);

    @Delete
    void deleteAll(PojoNote... pojoNotes);

}
