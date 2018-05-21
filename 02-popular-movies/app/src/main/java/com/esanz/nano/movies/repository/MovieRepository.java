package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.repository.model.TopRatedResponse;

import java.util.Objects;

public class MovieRepository {

    private static MovieRepository INSTANCE = null;

    private final MovieDataSource movieRemoteDataSource;

    private MovieRepository(@NonNull MovieDataSource movieRemoteDataSource) {
        this.movieRemoteDataSource = Objects.requireNonNull(movieRemoteDataSource);
    }

    public static MovieRepository getInstance(@NonNull MovieDataSource movieDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new MovieRepository(movieDataSource);
        }

        return INSTANCE;
    }

    public void getTopRatedMovies(@NonNull final MovieDataSource.LoadTopRatedMoviesCallback callback) {
        // TODO attempt to fetch from cache
        // TODO if cache is "dirty", fetch from remote
        // TODO if no cache, attempt to fetch from local DB, fallback to fetch from remote

        // for now always fetch from remote
        getTopRatedMoviesFromRemote(callback);
    }

    private void getTopRatedMoviesFromRemote(@NonNull final MovieDataSource.LoadTopRatedMoviesCallback callback) {
        movieRemoteDataSource.getTopRatedMovies(new MovieDataSource.LoadTopRatedMoviesCallback() {
            @Override
            public void onTopRatedMoviesLoaded(@Nullable TopRatedResponse response) {
                // TODO refresh cache
                // TODO refresh local DB

                callback.onTopRatedMoviesLoaded(response);
            }

            @Override
            public void onMoviesNotAvailable() {

            }
        });
    }
}
