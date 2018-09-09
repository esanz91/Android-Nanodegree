package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.repository.dao.FavoriteDao;
import com.esanz.nano.movies.repository.dao.MovieDao;
import com.esanz.nano.movies.repository.model.Favorite;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private static MovieRepository INSTANCE = null;

    private final MovieDataSource movieRemoteDataSource;
    private final MovieDao movieLocalDataSource;
    private final FavoriteDao favoriteLocalDataSource;

    private MovieRepository(@NonNull final MovieDataSource movieRemoteDataSource) {
        this.movieRemoteDataSource = Objects.requireNonNull(movieRemoteDataSource);
        this.movieLocalDataSource = MovieApplication.movieDatabase.moviesDao();
        this.favoriteLocalDataSource = MovieApplication.movieDatabase.favoritesDao();
    }

    public static MovieRepository getInstance(@NonNull final MovieDataSource movieDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new MovieRepository(movieDataSource);
        }

        return INSTANCE;
    }

    public void getTopRatedMovies(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        getTopRatedMoviesFromRemote(callback);
    }

    private void getTopRatedMoviesFromRemote(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        movieRemoteDataSource.getTopRatedMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@Nullable final PaginatedMovieResponse response) {
                callback.onMoviesLoaded(response);
            }

            @Override
            public void onMoviesNotAvailable() {
                callback.onMoviesNotAvailable();
            }
        });
    }

    public void getPopularMovies(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        // for now always fetch from remote
        getPopularMoviesFromRemote(callback);
    }

    private void getPopularMoviesFromRemote(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        movieRemoteDataSource.getPopularMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@Nullable PaginatedMovieResponse response) {
                callback.onMoviesLoaded(response);
            }

            @Override
            public void onMoviesNotAvailable() {
                callback.onMoviesNotAvailable();
            }
        });
    }

    public void getFavoriteMovies(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        getFavoriteMoviesFromLocal(callback);
    }

    private void getFavoriteMoviesFromLocal(@NonNull final MovieDataSource.LoadMoviesCallback callback) {
        favoriteLocalDataSource.getAll()
                .flatMapSingle(favorites -> Observable.fromIterable(favorites)
                        .map(favorite -> favorite.movieId)
                        .toList())
                .flatMapMaybe(movieLocalDataSource::findByIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> callback.onMoviesLoaded(new PaginatedMovieResponse(movies)));
    }

    public void addFavoriteMovie(Movie movie) {
        favoriteLocalDataSource.insert(new Favorite(movie.id));
    }

    public void deleteFavoriteMovie(Movie movie) {
        favoriteLocalDataSource.deleteById(movie.id);
    }
}
