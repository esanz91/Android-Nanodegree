package com.esanz.nano.ezbaking.respository;

import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.api.RecipeApi;
import com.esanz.nano.ezbaking.respository.api.RetrofitClient;
import com.esanz.nano.ezbaking.respository.model.Recipe;

import java.util.List;

import io.reactivex.Single;

public class RecipeRepository {

    private static RecipeRepository INSTANCE;

    private final RecipeApi mRecipeApi;

    private RecipeRepository(@NonNull final RecipeApi recipeApi) {
        mRecipeApi = recipeApi;
    }

    // TODO should pass in RetrofitClient in constructor
    public static RecipeRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new RecipeRepository(RetrofitClient.getInstance().create(RecipeApi.class));
        }

        return INSTANCE;
    }

    public Single<List<Recipe>> getRecipes() {
        return mRecipeApi.getRecipes();
    }
}
