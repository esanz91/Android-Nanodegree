package com.esanz.nano.movies.ui.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository movieRepository;

    public MovieDetailsViewModelFactory(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            return (T) new MovieDetailsViewModel(movieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
