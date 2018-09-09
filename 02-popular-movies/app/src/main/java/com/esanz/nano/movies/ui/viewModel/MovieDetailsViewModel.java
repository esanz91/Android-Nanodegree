package com.esanz.nano.movies.ui.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.MovieDetail;
import com.esanz.nano.movies.repository.model.MovieVideo;

import java.util.Objects;

import io.reactivex.Observable;

public class MovieDetailsViewModel extends ViewModel {

    public final MutableLiveData<MovieDetail> movieDetailsLiveData = new MutableLiveData<>();

    private final MovieRepository movieRepository;

    public MovieDetailsViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
    }

    public void loadMovieDetails(int movieId) {
        if (null == movieDetailsLiveData.getValue()) {
            movieRepository.getMovieDetailsById(movieId)
                    .doOnSuccess(details ->
                            details.videos = Observable.just(details.videos)
                                    .flatMapIterable(videos -> videos)
                                    .filter(video -> video.site.equalsIgnoreCase(MovieVideo.SITE_YOUTUBE))
                                    .toList()
                                    .blockingGet())
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
