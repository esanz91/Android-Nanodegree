package com.esanz.nano.ezbaking.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeRepository;

public class RecipesViewModelFactory implements ViewModelProvider.Factory {

    private final RecipeRepository recipeRepository;

    public RecipesViewModelFactory(@NonNull final RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipesViewModel.class)) {
            return (T) new RecipesViewModel(recipeRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
