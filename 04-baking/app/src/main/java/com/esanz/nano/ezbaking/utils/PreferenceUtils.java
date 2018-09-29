package com.esanz.nano.ezbaking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static final String KEY_RECIPE_ID = "recipe_id";

    public static void storeLastSeenRecipe(Context context, int recipeId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_RECIPE_ID, recipeId);
        editor.apply();
    }

    public static int getLastSeenReciper(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(KEY_RECIPE_ID, -1);
    }

}
