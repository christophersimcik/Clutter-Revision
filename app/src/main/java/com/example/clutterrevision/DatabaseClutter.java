package com.example.clutterrevision;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {PojoCheckList.class, PojoDay.class, PojoNote.class}, version = 1, exportSchema = false)
public abstract class DatabaseClutter extends RoomDatabase {
    public abstract DaoChecklist getDaoChecklist();
    public abstract DaoDay getDaoDay();
    public abstract DaoNote getDaoNote();

    private static DatabaseClutter databaseClutter;

    public static DatabaseClutter getInstance(Context context) {
        if (databaseClutter == null) {
            databaseClutter = buildDatabaseInstance(context);
        }
        return databaseClutter;
    }

    private static DatabaseClutter buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DatabaseClutter.class,
                Constants.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }
    public void cleanUp() {
        databaseClutter = null;
    }
}
