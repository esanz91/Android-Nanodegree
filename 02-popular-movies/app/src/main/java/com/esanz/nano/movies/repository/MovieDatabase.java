package com.esanz.nano.movies.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.esanz.nano.movies.repository.dao.FavoriteDao;
import com.esanz.nano.movies.repository.dao.MovieDao;
import com.esanz.nano.movies.repository.model.Favorite;
import com.esanz.nano.movies.repository.model.Movie;


@Database(entities = {Movie.class, Favorite.class},
        version = 1,
        exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies";

    public static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                    .build();
        }
        return sInstance;
    }

    public abstract MovieDao moviesDao();

    public abstract FavoriteDao favoritesDao();
}
