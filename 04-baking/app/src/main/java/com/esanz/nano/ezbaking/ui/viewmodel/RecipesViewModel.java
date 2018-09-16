package com.esanz.nano.ezbaking.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeDataSource;
import com.esanz.nano.ezbaking.respository.model.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private final List<Recipe> recipes;

    public RecipesViewModel(@NonNull Application application) {
        super(application);
        recipes = RecipeDataSource.getRecipes(getApplication());
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

}
