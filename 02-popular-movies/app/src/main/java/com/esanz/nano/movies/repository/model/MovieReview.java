package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

public class MovieReview {

    @SerializedName("id")
    public String id;

    @SerializedName("author")
    public String author;

    @SerializedName("content")
    public String content;

    @SerializedName("url")
    public String url;

}
