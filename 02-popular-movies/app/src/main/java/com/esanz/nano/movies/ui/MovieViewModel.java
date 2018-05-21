package com.esanz.nano.movies.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esanz.nano.movies.repository.MovieDataSource;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.model.TopRatedResponse;

public class MovieViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MutableLiveData<TopRatedResponse> topRatedLiveData = new MutableLiveData<>();

    public MovieViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void loadTopRatedMovies() {
        if (null == topRatedLiveData.getValue()) {
            Log.d("zzz", "loadTopRatedMovies");

            // TODO make use of CompositeDisposable
            movieRepository.getTopRatedMovies(new MovieDataSource.LoadTopRatedMoviesCallback() {
                @Override
                public void onTopRatedMoviesLoaded(@Nullable TopRatedResponse response) {
                    if (null != response) {
                        topRatedLiveData.postValue(response);
                    }
                }

                @Override
                public void onMoviesNotAvailable() {

                }
            });
        }
    }

}
