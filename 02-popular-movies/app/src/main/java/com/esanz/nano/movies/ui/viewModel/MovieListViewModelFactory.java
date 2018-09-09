package com.esanz.nano.movies.ui.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;

public class MovieListViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository movieRepository;

    public MovieListViewModelFactory(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieListViewModel.class)) {
            return (T) new MovieListViewModel(movieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
