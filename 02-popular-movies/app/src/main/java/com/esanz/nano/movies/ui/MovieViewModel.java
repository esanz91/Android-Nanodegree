package com.esanz.nano.movies.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.repository.MovieDataSource;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.dao.FavoriteDao;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;
import com.esanz.nano.movies.utils.MovieConstant;

import java.util.List;
import java.util.Objects;

public class MovieViewModel extends ViewModel {

    public final MutableLiveData<List<Movie>> movieListLiveData = new MutableLiveData<>();

    private final MutableLiveData<PaginatedMovieResponse> topRatedLiveData = new MutableLiveData<>();
    private final MutableLiveData<PaginatedMovieResponse> popularLiveData = new MutableLiveData<>();

    private final MovieRepository movieRepository;

    private int sortType = MovieConstant.SortType.RATING;

    public MovieViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
    }

    public void loadTopRatedMovies() {
        sortType = MovieConstant.SortType.RATING;
        if (null == topRatedLiveData.getValue()) {

            // TODO make use of CompositeDisposable
            movieRepository.getTopRatedMovies(new MovieDataSource.LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(@Nullable final PaginatedMovieResponse response) {
                    if (null != response) {
                        topRatedLiveData.postValue(response);
                        movieListLiveData.postValue(response.movies);
                    }
                }

                @Override
                public void onMoviesNotAvailable() {
                    movieListLiveData.postValue(null);
                }
            });
        } else {
            movieListLiveData.postValue(topRatedLiveData.getValue().movies);
        }
    }

    public void loadPopularMovies() {
        sortType = MovieConstant.SortType.POPULARITY;
        if (null == popularLiveData.getValue()) {

            movieRepository.getPopularMovies(new MovieDataSource.LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(@Nullable PaginatedMovieResponse response) {
                    if (null != response) {
                        popularLiveData.postValue(response);
                        movieListLiveData.postValue(response.movies);
                    }
                }

                @Override
                public void onMoviesNotAvailable() {
                    movieListLiveData.postValue(null);
                }
            });
        } else {
            movieListLiveData.postValue(popularLiveData.getValue().movies);
        }
    }

    public void loadFavoriteMovies() {
        sortType = MovieConstant.SortType.FAVORITES;

        movieRepository.getFavoriteMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@Nullable PaginatedMovieResponse response) {
                movieListLiveData.postValue(response.movies);
            }

            @Override
            public void onMoviesNotAvailable() {
                movieListLiveData.postValue(null);
            }
        });

    }

    public void addFavorite(Movie movie) {
        movieRepository.addFavoriteMovie(movie);
    }

    public void removeFavorite(Movie movie) {
        movieRepository.deleteFavoriteMovie(movie);
    }

    @MovieConstant.SortTypeDef
    public int getSortType() {
        return sortType;
    }
}
