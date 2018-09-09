package com.esanz.nano.movies.ui.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.MovieDetail;

import java.util.Objects;

public class MovieDetailsViewModel extends ViewModel {

    public final MutableLiveData<MovieDetail> movieDetailsLiveData = new MutableLiveData<>();

    private final MovieRepository movieRepository;

    public MovieDetailsViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
    }

    public void loadMovieDetails(int movieId) {
        if (null == movieDetailsLiveData.getValue()) {
            movieRepository.getMovieDetailsById(movieId)
                    .subscribe(movieDetailsLiveData::postValue);
        }
    }

    public void addFavoriteMovie(Movie movie) {
        movieRepository.addFavoriteMovie(movie).subscribe();
    }

    public void deleteFavoriteMovie(Movie movie) {
        movieRepository.deleteFavoriteMovie(movie).subscribe();
    }
}
