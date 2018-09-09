package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaginatedMovieResponse {

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<Movie> movies;

    public PaginatedMovieResponse(List<Movie> movies) {
        this.movies = movies;
    }
}
