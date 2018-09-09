package com.esanz.nano.movies.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.esanz.nano.movies.repository.model.Movie;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Movie[] movies);

    @Query("SELECT * FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_ID + " = :id")
    Maybe<Movie> findById(int id);

    @Query("SELECT * FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_ID + " IN(:ids)")
    Maybe<List<Movie>> findByIds(List<Integer> ids);

}
