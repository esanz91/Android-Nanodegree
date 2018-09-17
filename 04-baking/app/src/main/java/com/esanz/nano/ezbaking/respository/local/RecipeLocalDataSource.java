package com.esanz.nano.ezbaking.respository.local;

import android.content.Context;
import android.support.annotation.RawRes;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class RecipeLocalDataSource {

    @RawRes
    private static final int fileRes = R.raw.recipes;

    private static final Type TYPE_RECIPE_LIST = new TypeToken<List<Recipe>>() {}.getType();

    public static List<Recipe> getRecipes(Context context) {
        InputStream inputStream = context.getResources().openRawResource(fileRes);
        final Gson gson = new Gson();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return gson.fromJson(reader, TYPE_RECIPE_LIST);
    }

}
