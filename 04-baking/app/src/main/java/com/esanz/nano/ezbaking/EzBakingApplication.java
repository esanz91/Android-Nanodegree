package com.esanz.nano.ezbaking;

import android.app.Application;

import com.esanz.nano.ezbaking.respository.RecipeRepository;
import com.esanz.nano.ezbaking.respository.api.OkHttpClientProvider;
import com.esanz.nano.ezbaking.respository.api.RecipeApi;
import com.esanz.nano.ezbaking.respository.api.RetrofitClient;
import com.esanz.nano.ezbaking.respository.db.RecipeDb;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class EzBakingApplication extends Application {

    public static RecipeRepository RECIPE_REPOSITORY;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = OkHttpClientProvider.getInstance();
        RecipeApi recipeApi = RetrofitClient.getInstance(okHttpClient).create(RecipeApi.class);
        RecipeDb recipeDb = RecipeDb.getInstance(getApplicationContext());
        RECIPE_REPOSITORY = RecipeRepository.getInstance(recipeApi, recipeDb);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
