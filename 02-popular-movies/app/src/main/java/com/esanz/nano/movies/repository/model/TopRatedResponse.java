package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopRatedResponse {

    @SerializedName("page")
    public int page;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("results")
    public List<Movie> movies;

}
