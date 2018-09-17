package com.esanz.nano.ezbaking.respository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("ingredients")
    public List<Ingredient> ingredients;

    @SerializedName("steps")
    public List<Step> steps;

    @SerializedName("servings")
    public int servings;

    @SerializedName("image")
    public String image;

}
