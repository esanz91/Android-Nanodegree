package com.esanz.nano.movies.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites", indices = {@Index("movie_id")})
public class Favorite {

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MOVIE_ID = "movie_id";

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = COLUMN_MOVIE_ID)
    public int movieId;

    public Favorite(int movieId) {
        this.movieId = movieId;
    }
}
