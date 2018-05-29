package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;

import java.util.Objects;

public class MovieRepository {

    private static MovieRepository INSTANCE = null;

    private final MovieDataSource movieRemoteDataSource;

    private MovieRepository(@NonNull final MovieDataSource movieRemoteDataSource) {
        this.movieRemoteDataSource = Objects.requireNonNull(movieRemoteDataSource);
    }

    public static MovieRepository getInstance(@NonNull final MovieDataSource movieDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new MovieRepository(movieDataSource);
        }

        return INSTANCE;
    }

    public void getTopRatedMovies(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        // TODO attempt to fetch from cache
        // TODO if cache is "dirty", fetch from remote
        // TODO if no cache, attempt to fetch from local DB, fallback to fetch from remote

        // for now always fetch from remote
        getTopRatedMoviesFromRemote(callback);
    }

    private void getTopRatedMoviesFromRemote(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        movieRemoteDataSource.getTopRatedMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@Nullable final PaginatedMovieResponse response) {
                // TODO refresh cache
                // TODO refresh local DB

                callback.onMoviesLoaded(response);
            }

            @Override
            public void onMoviesNotAvailable() {
                callback.onMoviesNotAvailable();
            }
        });
    }

    public void getPopularMovies(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        // TODO attempt to fetch from cache
        // TODO if cache is "dirty", fetch from remote
        // TODO if no cache, attempt to fetch from local DB, fallback to fetch from remote

        // for now always fetch from remote
        getPopularMoviesFromRemote(callback);
    }

    private void getPopularMoviesFromRemote(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        movieRemoteDataSource.getPopularMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@Nullable PaginatedMovieResponse response) {
                // TODO refresh cache
                // TODO refresh local DB

                callback.onMoviesLoaded(response);
            }

            @Override
            public void onMoviesNotAvailable() {
                callback.onMoviesNotAvailable();
            }
        });
    }
}
