package com.example.clutterrevision;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoDay {
    // additions:
    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes + 1, number_of_note_notes = number_of_note_notes + 1 WHERE day_id = :id")
    void addNote(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes + 1, number_of_ref_notes = number_of_ref_notes + 1 WHERE day_id = :id")
    void addReference(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes + 1, number_of_book_notes = number_of_book_notes + 1 WHERE day_id = :id")
    void addBook(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes + 1, number_of_audio_notes = number_of_audio_notes + 1 WHERE day_id = :id")
    void addAudio(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes + 1, number_of_list_notes = number_of_list_notes + 1 WHERE day_id = :id")
    void addList(String id);

    // deletions:
    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes - 1, number_of_note_notes = number_of_note_notes - 1 WHERE day_id = :id")
    void deleteNote(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes - 1, number_of_ref_notes = number_of_ref_notes - 1 WHERE day_id = :id")
    void deleteReference(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes - 1, number_of_book_notes = number_of_book_notes - 1 WHERE day_id = :id")
    void deleteBook(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes - 1, number_of_audio_notes = number_of_audio_notes - 1 WHERE day_id = :id")
    void deleteAudio(String id);

    @Query("UPDATE PojoDay SET number_of_notes= number_of_notes - 1, number_of_list_notes = number_of_list_notes - 1 WHERE day_id = :id")
    void deleteList(String id);





    @Query("SELECT * FROM PojoDay WHERE day_id = :day")
    PojoDay getThisDay(String day);

    @Query("SELECT * FROM PojoDay WHERE day_id = :day")
    PojoDay getQuanity(String day);

    @Query("SELECT * FROM PojoDay")
    List<PojoDay> getAllDays();

    @Query("SELECT * FROM PojoDay")
    LiveData<List<PojoDay>> getAllDaysAsLiveData();

    @Insert
    void insert(PojoDay pojoDay);

    @Update
    void update(PojoDay pojoDay);

    @Delete
    void delete(PojoDay pojoDay);

    @Query("DELETE FROM PojoDay WHERE number_of_notes = :num")
    void deleteEmpties(int num);

    @Delete
    void deleteAll(PojoDay... pojoDays);

}
