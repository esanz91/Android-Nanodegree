package com.esanz.nano.movies.repository.api;

import com.esanz.nano.movies.repository.model.TopRatedResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MovieApi {

    @GET("movie/top_rated")
    Single<TopRatedResponse> getTopRated();

}
