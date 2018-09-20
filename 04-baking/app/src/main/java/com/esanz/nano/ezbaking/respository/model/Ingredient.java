package com.esanz.nano.ezbaking.respository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import com.esanz.nano.ezbaking.respository.RecipeDetail;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;


@Entity(tableName = Ingredient.TABLE_NAME,
        indices = @Index(value = Ingredient.COLUMN_FOREIGN_KEY, name = "idx_ingredients_recipe_id"),
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = Recipe.COLUMN_PRIMARY_KEY,
                childColumns = Ingredient.COLUMN_FOREIGN_KEY,
                onDelete = ForeignKey.CASCADE))
public class Ingredient implements RecipeDetail {

    public static final String TITLE = "Ingredients";

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_FOREIGN_KEY = "recipe_id";
    public static final String COLUMN_PRIMARY_KEY = "ingredient_id";

    public static final String LABEL_UNIT = "unit";

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
    public String name;

    public String getLabel() {
        if (LABEL_UNIT.equalsIgnoreCase(measure)) {
            return TextUtils.join(" ", Collections.singleton(quantity));
        } else {
            return TextUtils.join(" ", Arrays.asList(quantity, measure.toLowerCase()));
        }
    }

    @Override
    public int getType() {
        return RecipeDetail.TYPE_INGREDIENT;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
