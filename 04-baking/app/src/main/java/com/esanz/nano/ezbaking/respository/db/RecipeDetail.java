package com.esanz.nano.ezbaking.respository.db;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;

import java.util.List;

public class RecipeDetail {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = Recipe.COLUMN_PRIMARY_KEY,
            entityColumn = Ingredient.COLUMN_FOREIGN_KEY,
            entity = Ingredient.class)
    public List<Ingredient> ingredients;

    @Relation(parentColumn = Recipe.COLUMN_PRIMARY_KEY,
            entityColumn = Step.COLUMN_FOREIGN_KEY,
            entity = Step.class)
    public List<Step> steps;

}
