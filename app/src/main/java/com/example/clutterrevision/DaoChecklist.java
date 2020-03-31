package com.example.clutterrevision;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoChecklist {
    @Query("SELECT * FROM PojoCheckList WHERE checklist_day = :name ORDER BY is_item_checked= 1, item_description ASC ")
    LiveData<List<PojoCheckList>> getThisList(String name);

    @Query("SELECT * FROM PojoCheckList ORDER BY is_item_checked= 1, item_description ASC ")
    LiveData<List<PojoCheckList>> getallItems();

    @Insert
    void insert(PojoCheckList pojoCheckList);

    @Update
    void update(PojoCheckList updatedItem);

    @Delete
    void delete(PojoCheckList pojoCheckList);

    @Delete
    void deleteAll(PojoCheckList... pojoCheckLists);

}
