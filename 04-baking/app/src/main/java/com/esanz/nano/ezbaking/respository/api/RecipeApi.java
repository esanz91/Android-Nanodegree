package com.esanz.nano.ezbaking.respository.api;

import com.esanz.nano.ezbaking.respository.model.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RecipeApi {

    @GET("android-baking-app-json")
    Single<List<Recipe>> getRecipes();

}
