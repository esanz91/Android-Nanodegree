package com.esanz.nano.movies.repository.model;

import java.util.List;

public class MovieDetail {
    public Movie movie;
    public boolean isFavorite;
    public List<MovieReview> reviews;

    public MovieDetail(final Movie movie,
                       final boolean isFavorite,
                       final List<MovieReview> reviews) {
        this.movie = movie;
        this.isFavorite = isFavorite;
        this.reviews = reviews;
    }
}
