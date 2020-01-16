package com.tudresden.mobilecartoapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations_table")
public class Locations {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer ID;
    @NonNull
    @ColumnInfo(name = "time")
    public String Time;
    @NonNull
    @ColumnInfo(name = "latitude")
    public String Latitude;
    @NonNull
    @ColumnInfo(name = "longitude")
    public String Longitude;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer locations_id) {
        this.ID = locations_id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String locations_time) {
        this.Time = locations_time;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String locations_latitude) {
        this.Latitude = locations_latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String locations_longitude) {
        this.Longitude = locations_longitude;
    }

}



