package com.esanz.nano.movies.repository.api;

import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieRemoteDataSource {

    private static MovieRemoteDataSource INSTANCE;

    private final MovieApi movieApi;

    public static MovieRemoteDataSource getInstance(@NonNull MovieApi movieApi) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRemoteDataSource(movieApi);
        }
        return INSTANCE;
    }

    private MovieRemoteDataSource(@NonNull MovieApi movieApi) {
        this.movieApi = movieApi;
    }

    public Single<PaginatedMovieResponse> getTopRatedMovies() {
        return movieApi.getTopRated()
                .subscribeOn(Schedulers.io());
    }

    public Single<PaginatedMovieResponse> getPopularMovies() {
        return movieApi.getPopular()
                .subscribeOn(Schedulers.io());
    }

}
