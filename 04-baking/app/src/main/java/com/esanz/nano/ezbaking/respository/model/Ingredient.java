package com.esanz.nano.ezbaking.respository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = Ingredient.TABLE_NAME,
        indices = @Index(value = Ingredient.COLUMN_FOREIGN_KEY, name = "idx_ingredients_recipe_id"),
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = Recipe.COLUMN_PRIMARY_KEY,
                childColumns = Ingredient.COLUMN_FOREIGN_KEY,
                onDelete = ForeignKey.CASCADE))
public class Ingredient {

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_FOREIGN_KEY = "recipe_id";
    public static final String COLUMN_PRIMARY_KEY = "ingredient_id";

    @ColumnInfo(name = COLUMN_FOREIGN_KEY)
    public int recipeId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_PRIMARY_KEY)
    public int id;

    @SerializedName("quantity")
    public double quantity;

    @SerializedName("measure")
    public String measure;

    @SerializedName("ingredient")
    public String ingredient;

}
