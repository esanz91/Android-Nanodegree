package com.esanz.nano.ezbaking.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.esanz.nano.ezbaking.respository.RecipeRepository;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final BehaviorRelay<Recipe> recipeRelay = BehaviorRelay.create();
    private final CompositeDisposable disposables = new CompositeDisposable();

    protected RecipeViewModel(@NonNull final RecipeRepository recipeRepository, final int id) {
        this.recipeRepository = recipeRepository;
        loadRecipe(id);
    }

    private void loadRecipe(final int id) {
        disposables.add(recipeRepository.getRecipe(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeRelay::accept, Timber::e));
    }

    public Observable<Recipe> getRecipe() {
        return recipeRelay.observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }
}
