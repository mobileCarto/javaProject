package com.tudresden.mobilecartoapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Locations.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationsDAO getLocationsDAO();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };
}

