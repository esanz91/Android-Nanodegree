package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaginatedMovieReviewResponse {

    @SerializedName("results")
    public List<MovieReview> movieReviews;

}
