package com.esanz.nano.movies.repository;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.esanz.nano.movies.repository.api.MovieRemoteDataSource;
import com.esanz.nano.movies.repository.dao.FavoriteDao;
import com.esanz.nano.movies.repository.dao.MovieDao;
import com.esanz.nano.movies.repository.model.Favorite;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.MovieDetail;
import com.esanz.nano.movies.repository.model.MovieReview;
import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;
import com.esanz.nano.movies.repository.model.PaginatedMovieReviewResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private static MovieRepository INSTANCE = null;

    private final MovieRemoteDataSource movieRemoteDataSource;
    private final MovieDao movieDao;
    private final FavoriteDao favoriteDao;

    // TODO make use of CompositeDisposable

    private MovieRepository(@NonNull final MovieRemoteDataSource movieRemoteDataSource,
                            @NonNull final MovieLocalDataSource movieLocalDataSource) {
        this.movieRemoteDataSource = Objects.requireNonNull(movieRemoteDataSource);
        this.movieDao = movieLocalDataSource.moviesDao();
        this.favoriteDao = movieLocalDataSource.favoritesDao();
    }

    public static MovieRepository getInstance(@NonNull final MovieRemoteDataSource movieRemoteDataSource,
                                              @NonNull final MovieLocalDataSource movieLocalDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new MovieRepository(movieRemoteDataSource, movieLocalDataSource);
        }

        return INSTANCE;
    }

    public Single<PaginatedMovieResponse> getTopRatedMovies() {
        return getTopRatedMoviesFromRemote();
    }

    private Single<PaginatedMovieResponse> getTopRatedMoviesFromRemote() {
        return movieRemoteDataSource.getTopRatedMovies()
                .doOnSuccess(response -> {
                    Movie[] movies = response.movies.toArray(new Movie[response.movies.size()]);
                    movieDao.insertAll(movies);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PaginatedMovieResponse> getPopularMovies() {
        // for now always fetch from remote
        return getPopularMoviesFromRemote();
    }

    private Single<PaginatedMovieResponse> getPopularMoviesFromRemote() {
        return movieRemoteDataSource.getPopularMovies()
                .doOnSuccess(response -> {
                    Movie[] movies = response.movies.toArray(new Movie[response.movies.size()]);
                    movieDao.insertAll(movies);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<Movie>> getFavoriteMovies() {
        return getFavoriteMoviesFromLocal();
    }

    private Maybe<List<Movie>> getFavoriteMoviesFromLocal() {
        return favoriteDao.getAll()
                .flatMapSingle(favorites -> Observable.fromIterable(favorites)
                        .map(favorite -> favorite.movieId)
                        .toList())
                .flatMapMaybe(movieDao::findByIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addFavoriteMovie(Movie movie) {
        return Completable.fromAction(() ->
                favoriteDao.insert(new Favorite(movie.id)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteFavoriteMovie(Movie movie) {
        return Completable.fromAction(() ->
                favoriteDao.deleteById(movie.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieDetail> getMovieDetailsById(int movieId) {
        return Maybe.zip(
                movieDao.findById(movieId),
                favoriteDao.findById(movieId).map(favorite -> true).defaultIfEmpty(false),
                getMovieReviewsFromRemote(movieId).toMaybe(),
                MovieDetail::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<MovieReview>> getMovieReviewsFromRemote(int movieId) {
        return movieRemoteDataSource.getMovieReviews(movieId)
                .map(movieReviewDetails -> movieReviewDetails.movieReviews);
    }
}
