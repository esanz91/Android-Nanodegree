package com.esanz.nano.movies.repository.api;

import com.esanz.nano.movies.repository.model.MovieVideoResponse;
import com.esanz.nano.movies.repository.model.PaginatedMovieResponse;
import com.esanz.nano.movies.repository.model.PaginatedMovieReviewResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApi {

    @GET("movie/top_rated")
    Single<PaginatedMovieResponse> getTopRated();

    @GET("movie/popular")
    Single<PaginatedMovieResponse> getPopular();

    @GET("movie/{movie_id}/videos")
    Single<MovieVideoResponse> getVideos(
            @Path("movie_id") int movieId
    );

    @GET("movie/{movie_id}/reviews")
    Single<PaginatedMovieReviewResponse> getReviews(
            @Path("movie_id") int movieId
    );

}
