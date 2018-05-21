package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.repository.model.TopRatedResponse;

/**
 * movie data source contract (from cache, local data source, or remote data source)
 */
public interface MovieDataSource {

    interface LoadTopRatedMoviesCallback {
        void onTopRatedMoviesLoaded(@Nullable TopRatedResponse response);

        void onMoviesNotAvailable();
    }

    void getTopRatedMovies(@NonNull final LoadTopRatedMoviesCallback callback);

}
