package com.esanz.nano.movies;

import android.app.Application;

import com.esanz.nano.movies.repository.MovieLocalDataSource;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.api.MovieApi;
import com.esanz.nano.movies.repository.api.MovieRemoteDataSource;
import com.esanz.nano.movies.repository.api.RetrofitClient;

public class MovieApplication extends Application {

    public static MovieRepository movieRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        MovieApi movieApi = RetrofitClient.getMovieApi(getApplicationContext());
        MovieLocalDataSource movieLocalDatabase = MovieLocalDataSource.getInstance(getApplicationContext());
        MovieRemoteDataSource movieRemoteDatabase = MovieRemoteDataSource.getInstance(movieApi);
        movieRepository = MovieRepository.getInstance(movieRemoteDatabase, movieLocalDatabase);
    }

}
