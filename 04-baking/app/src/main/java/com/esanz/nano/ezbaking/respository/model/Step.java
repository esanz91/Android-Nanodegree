package com.esanz.nano.ezbaking.respository.model;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("id")
    public int id;

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("description")
    public String description;

    @SerializedName("videoURL")
    public String videoURL;

    @SerializedName("thumbnailURL")
    public String thumbnailURL;

}
