package com.esanz.nano.ezbaking.respository.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM " + Recipe.TABLE_NAME + " WHERE " + Recipe.COLUMN_PRIMARY_KEY + "  = :id")
    abstract Flowable<RecipeDetail> getRecipeWithDetails(final int id);

    @Query("SELECT * FROM " + Recipe.TABLE_NAME)
    abstract Flowable<List<RecipeDetail>> getRecipesWithDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertRecipe(@NonNull final Recipe recipe);

    @Insert
    abstract void insertIngredients(@NonNull final List<Ingredient> ingredients);

    @Insert
    abstract void insertSteps(@NonNull final List<Step> steps);

    @Transaction
    public void insertRecipes(@NonNull final List<Recipe> recipes) {
        for (final Recipe recipe : recipes) {
            for (final Ingredient ingredient : recipe.ingredients) {
                ingredient.recipeId = recipe.id;
            }
            for (final Step step : recipe.steps) {
                step.recipeId = recipe.id;
            }

            insertRecipe(recipe);
            insertIngredients(recipe.ingredients);
            insertSteps(recipe.steps);
        }
    }

    public Flowable<List<Recipe>> getRecipes() {
        return getRecipesWithDetails()
                .flatMapSingle(recipeDetailsList ->
                        Flowable.fromIterable(recipeDetailsList)
                                .map(this::getRecipe).toList());
    }

    public Flowable<Recipe> getRecipe(final int id) {
        return getRecipeWithDetails(id)
                .map(this::getRecipe);
    }

    private Recipe getRecipe(@NonNull final RecipeDetail recipeDetail) {
        recipeDetail.recipe.ingredients = recipeDetail.ingredients;
        recipeDetail.recipe.steps = recipeDetail.steps;
        return recipeDetail.recipe;
    }
}
