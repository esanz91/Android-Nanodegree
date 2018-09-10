package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {

    @SerializedName("results")
    public List<MovieVideo> videos;

}
