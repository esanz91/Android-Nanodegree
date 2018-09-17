package com.esanz.nano.ezbaking.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeRepository;
import com.esanz.nano.ezbaking.respository.model.Recipe;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class RecipesViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    public RecipesViewModel(@NonNull final RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        loadRecipes();
    }

    public void loadRecipes() {
        recipeRepository.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes::postValue, Timber::e);
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

}
