package com.esanz.nano.ezbaking.respository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = Recipe.TABLE_NAME,
        indices = {@Index(Recipe.COLUMN_PRIMARY_KEY)})
public class Recipe {

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_PRIMARY_KEY = "recipe_id";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_PRIMARY_KEY)
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("servings")
    public int servings;

    @SerializedName("image")
    public String image;

    @Ignore
    @SerializedName("ingredients")
    public List<Ingredient> ingredients;

    @Ignore
    @SerializedName("steps")
    public List<Step> steps;

}
