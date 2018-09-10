package com.esanz.nano.movies.repository.model;

import com.google.gson.annotations.SerializedName;

public class MovieVideo {

    public static final String SITE_YOUTUBE = "YouTube";
    public static final String LINK_YOUTUBE = "https://www.youtube.com/watch?v=";

    @SerializedName("id")
    public String id;

    @SerializedName("key")
    public String key;

    @SerializedName("name")
    public String name;

    @SerializedName("site")
    public String site;

    @SerializedName("type")
    public String type;

    public String getYouTubeLink() {
        return LINK_YOUTUBE + key;
    }
}
