package com.esanz.nano.movies.ui.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;
import com.esanz.nano.movies.utils.MovieConstant;

import java.util.List;
import java.util.Objects;

public class MovieListViewModel extends ViewModel {

    public final MutableLiveData<List<Movie>> movieListLiveData = new MutableLiveData<>();

    private final MutableLiveData<PaginatedMovieResponse> topRatedLiveData = new MutableLiveData<>();
    private final MutableLiveData<PaginatedMovieResponse> popularLiveData = new MutableLiveData<>();

    private final MovieRepository movieRepository;

    private int sortType = MovieConstant.SortType.RATING;

    // TODO make use of CompositeDisposable

    public MovieListViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
    }

    public void loadTopRatedMovies() {
        sortType = MovieConstant.SortType.RATING;
        if (null == topRatedLiveData.getValue()) {

            movieRepository.getTopRatedMovies()
                    .subscribe(response -> {
                        topRatedLiveData.postValue(response);
                        movieListLiveData.postValue(response.movies);
                    }, e -> movieListLiveData.postValue(null));
        } else {
            movieListLiveData.postValue(topRatedLiveData.getValue().movies);
        }
    }

    public void loadPopularMovies() {
        sortType = MovieConstant.SortType.POPULARITY;
        if (null == popularLiveData.getValue()) {
            movieRepository.getPopularMovies()
                    .subscribe(response -> {
                        popularLiveData.postValue(response);
                        movieListLiveData.postValue(response.movies);
                    }, e -> movieListLiveData.postValue(null));
        } else {
            movieListLiveData.postValue(popularLiveData.getValue().movies);
        }
    }

    public void loadFavoriteMovies() {
        sortType = MovieConstant.SortType.FAVORITES;

        movieRepository.getFavoriteMovies()
                .subscribe(movieListLiveData::postValue, e -> movieListLiveData.postValue(null));

    }

    public void addFavorite(Movie movie) {
        movieRepository.addFavoriteMovie(movie).subscribe();
    }

    public void removeFavorite(Movie movie) {
        movieRepository.deleteFavoriteMovie(movie).subscribe();
    }

    @MovieConstant.SortTypeDef
    public int getSortType() {
        return sortType;
    }
}
