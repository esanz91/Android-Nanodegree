package com.esanz.nano.ezbaking.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeRepository;
import com.esanz.nano.ezbaking.respository.model.Recipe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RecipeViewModel extends ViewModel {

    private final int recipeId;
    private final RecipeRepository recipeRepository;
    private final MutableLiveData<Recipe> recipeLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public RecipeViewModel(@NonNull final RecipeRepository recipeRepository, final int id) {
        this.recipeId = id;
        this.recipeRepository = recipeRepository;
        loadRecipe(id);
    }

    public void loadRecipe(final int id) {
        disposables.add(recipeRepository.getRecipe(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeLiveData::postValue, Timber::e));
    }

    public MutableLiveData<Recipe> getRecipe() {
        return recipeLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.dispose();
    }
}
