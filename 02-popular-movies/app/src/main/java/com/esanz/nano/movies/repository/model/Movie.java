package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    private static final String IMAGE_SECURE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_PATH = "w342";
    private static final String BACKDROP_SIZE_PATH = "w500";

    @SerializedName("id")
    public int id;

    @SerializedName("video")
    public boolean hasVideo;

    @SerializedName("vote_average")
    public float voteAverage;

    @SerializedName("title")
    public String title;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("overview")
    public String overview;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("genre_ids")
    public Integer[] genreIds;

    public String getBackdropUrlString() {
        return IMAGE_SECURE_BASE_URL + BACKDROP_SIZE_PATH + backdropPath;
    }

    public String getPosterUrlString() {
        return IMAGE_SECURE_BASE_URL + POSTER_SIZE_PATH + posterPath;
    }
}