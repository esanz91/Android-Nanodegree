package com.esanz.nano.movies.repository.model;

import java.util.List;

public class MovieDetail {
    public Movie movie;
    public boolean isFavorite;
    public List<MovieVideo> videos;
    public List<MovieReview> reviews;

    public MovieDetail(final Movie movie,
                       final boolean isFavorite,
                       final List<MovieVideo> videos,
                       final List<MovieReview> reviews) {
        this.movie = movie;
        this.isFavorite = isFavorite;
        this.videos = videos;
        this.reviews = reviews;
    }
}
