package com.esanz.nano.movies.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites", indices = {@Index("movie_id")})
public class Favorite {

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_ID = "id";

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "movie_id")
    public int movieId;

}
