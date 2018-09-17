package com.esanz.nano.ezbaking.respository;

import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.api.RecipeApi;
import com.esanz.nano.ezbaking.respository.db.RecipeDao;
import com.esanz.nano.ezbaking.respository.db.RecipeDb;
import com.esanz.nano.ezbaking.respository.model.Recipe;

import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

public class RecipeRepository {

    private static RecipeRepository INSTANCE;

    private final RecipeApi mRecipeApi;
    private final RecipeDao mRecipeDao;

    private RecipeRepository(@NonNull final RecipeApi recipeApi, @NonNull final RecipeDb recipeDb) {
        mRecipeApi = recipeApi;
        mRecipeDao = recipeDb.recipeDao();
    }

    public static RecipeRepository getInstance(@NonNull final RecipeApi recipeApi,
                                               @NonNull final RecipeDb recipeDb) {
        if (null == INSTANCE) {
            INSTANCE = new RecipeRepository(recipeApi, recipeDb);
        }

        return INSTANCE;
    }

    public Single<List<Recipe>> getRecipes() {
        return mRecipeApi.getRecipes()
                .doOnSuccess(mRecipeDao::insertRecipes)
                .onErrorResumeNext(e -> {
                    Timber.e(e);
                    return Single.just(mRecipeDao.getRecipes());
                });
    }

}
