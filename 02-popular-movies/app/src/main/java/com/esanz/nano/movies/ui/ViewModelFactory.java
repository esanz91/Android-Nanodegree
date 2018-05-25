package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository movieRepository;

    public ViewModelFactory(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel(movieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
