package com.esanz.nano.movies.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.repository.MovieDataSource;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.model.TopRatedResponse;

import java.util.Objects;

public class MovieViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    public final MutableLiveData<TopRatedResponse> topRatedLiveData = new MutableLiveData<>();

    public MovieViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
    }

    public void loadTopRatedMovies() {
        if (null == topRatedLiveData.getValue()) {

            // TODO make use of CompositeDisposable
            movieRepository.getTopRatedMovies(new MovieDataSource.LoadTopRatedMoviesCallback() {
                @Override
                public void onTopRatedMoviesLoaded(@Nullable final TopRatedResponse response) {
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
