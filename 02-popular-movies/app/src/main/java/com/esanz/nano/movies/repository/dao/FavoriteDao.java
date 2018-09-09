package com.esanz.nano.movies.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.esanz.nano.movies.repository.model.Favorite;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorite favorite);

    @Query("SELECT * FROM " + Favorite.TABLE_NAME + " WHERE " + Favorite.COLUMN_MOVIE_ID + " = :id")
    Maybe<Favorite> findById(int id);

    @Query("SELECT * FROM " + Favorite.TABLE_NAME)
    Maybe<List<Favorite>> getAll();

    @Query("DELETE FROM " + Favorite.TABLE_NAME + " WHERE " + Favorite.COLUMN_MOVIE_ID + " = :id")
    void deleteById(int id);

}
