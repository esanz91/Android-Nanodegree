package com.esanz.nano.ezbaking.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeRepository;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {

    private final RecipeRepository recipeRepository;
    private final int recipeId;

    public RecipeViewModelFactory(@NonNull final RecipeRepository recipeRepository, final int id) {
        this.recipeRepository = recipeRepository;
        this.recipeId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(recipeRepository, recipeId);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
