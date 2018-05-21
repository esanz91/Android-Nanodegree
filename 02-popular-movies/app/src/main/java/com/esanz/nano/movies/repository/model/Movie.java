package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

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

}