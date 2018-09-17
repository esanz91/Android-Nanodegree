package com.esanz.nano.ezbaking.respository.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM  " + Recipe.TABLE_NAME)
    public abstract List<Recipe> getAll();

    @Query("SELECT * FROM " + Recipe.TABLE_NAME)
    abstract List<RecipeDetail> getRecipesWithDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertRecipe(Recipe recipe);

    @Insert
    abstract void insertIngredients(List<Ingredient> ingredients);

    @Insert
    abstract void insertSteps(List<Step> steps);

    @Transaction
    public void insertRecipes(final List<Recipe> recipes) {
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

    public List<Recipe> getRecipes() {
        final List<RecipeDetail> recipeDetailList = getRecipesWithDetails();
        final List<Recipe> recipes = new ArrayList<>(recipeDetailList.size());
        for (final RecipeDetail recipeDetail : recipeDetailList) {
            recipeDetail.recipe.ingredients = recipeDetail.ingredients;
            recipeDetail.recipe.steps = recipeDetail.steps;
            recipes.add(recipeDetail.recipe);
        }
        return recipes;
    }
}
