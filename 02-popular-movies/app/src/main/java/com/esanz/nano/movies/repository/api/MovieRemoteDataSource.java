package com.esanz.nano.movies.repository.api;

import android.support.annotation.NonNull;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.repository.MovieDataSource;
import com.esanz.nano.movies.repository.model.TopRatedResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieRemoteDataSource implements MovieDataSource {

    private static MovieRemoteDataSource INSTANCE;

    public static MovieRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieRemoteDataSource();
        }
        return INSTANCE;
    }

    // prevent direct instantiation
    private MovieRemoteDataSource() {
    }

    @Override
    public void getTopRatedMovies(@NonNull final LoadTopRatedMoviesCallback callback) {
        // TODO make Result Wrapper to handle errors
        MovieApplication.movieApi.getTopRated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<TopRatedResponse>() {

                    @Override
                    public void onSuccess(TopRatedResponse topRatedResponse) {
                        callback.onTopRatedMoviesLoaded(topRatedResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }

}
