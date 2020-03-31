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
    @Query("SELECT * FROM PojoDay WHERE day_id = :day")
    PojoDay getThisDay(String day);

    @Query("SELECT * FROM PojoDay")
    List<PojoDay> getAllDays();

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
