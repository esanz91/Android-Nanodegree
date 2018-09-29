package com.esanz.nano.ezbaking.respository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.esanz.nano.ezbaking.R;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = Recipe.TABLE_NAME,
        indices = {@Index(Recipe.COLUMN_PRIMARY_KEY)})
public class Recipe {

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_PRIMARY_KEY = "recipe_id";

    private static final String NUTELLA = "Nutella Pie";
    private static final String BROWNIE = "Brownies";
    private static final String YELLOW_CAKE = "Yellow Cake";
    private static final String CHEESECAKE = "Cheesecake";
    private static final Map<String, Integer> BACKUP_IMAGE = new HashMap<String, Integer>() {{
        put(NUTELLA, R.drawable.nutella_pie);
        put(BROWNIE, R.drawable.brownie);
        put(YELLOW_CAKE, R.drawable.yellow_cake);
        put(CHEESECAKE, R.drawable.cheesecake);
    }};

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

    @Nullable
    public String getImage() {
        return !TextUtils.isEmpty(image) ? image : null;
    }

    @DrawableRes
    public int getBackupImage() {
        return BACKUP_IMAGE.containsKey(name) ? BACKUP_IMAGE.get(name) : R.drawable.recipe_placeholder;
    }

}
