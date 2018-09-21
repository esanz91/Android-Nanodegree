package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.RecipeRepository;
import com.esanz.nano.ezbaking.ui.adapter.RecipesAdapter;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipesViewModel;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipesViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RecipesViewModel mRecipesViewModel;
    private RecipesAdapter mRecipesAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipes)
    RecyclerView mRecipesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mRecipesAdapter = new RecipesAdapter();
        mRecipesView.setAdapter(mRecipesAdapter);

        RecipesViewModelFactory recipesViewModelFactory =
                new RecipesViewModelFactory(EzBakingApplication.RECIPE_REPOSITORY);
        mRecipesViewModel = ViewModelProviders.of(this, recipesViewModelFactory)
                .get(RecipesViewModel.class);
        mRecipesViewModel.getRecipes()
                .observe(this, recipes -> mRecipesAdapter.setRecipes(recipes));
    }

}
