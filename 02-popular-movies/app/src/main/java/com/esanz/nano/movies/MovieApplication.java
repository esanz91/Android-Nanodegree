package com.esanz.nano.movies;

import android.app.Application;

import com.esanz.nano.movies.repository.api.MovieApi;
import com.esanz.nano.movies.repository.api.RetrofitClient;

public class MovieApplication extends Application {

    public static MovieApi movieApi;

    @Override
    public void onCreate() {
        super.onCreate();
        movieApi = RetrofitClient.getMovieApi(this);
    }

}
