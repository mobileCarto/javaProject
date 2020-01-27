package com.tudresden.mobilecartoapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationsDAO {
    @Insert
    public void insert(Locations locations);

    @Update
    public void update(Locations locations);

    @Delete
    public void delete(Locations locations);

    @Query("SELECT * FROM locations_table")
    public List<Locations> getAllLocations();

}




