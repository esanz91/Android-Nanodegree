package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;

/**
 * movie data source contract (from cache, local data source, or remote data source)
 */
public interface MovieDataSource {

    interface LoadMoviesCallback {
        void onMoviesLoaded(@Nullable final PaginatedMovieResponse response);

        void onMoviesNotAvailable();
    }

    void getTopRatedMovies(@NonNull final LoadMoviesCallback callback);

    void getPopularMovies(@NonNull final LoadMoviesCallback callback);

}
