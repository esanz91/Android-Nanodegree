package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.RecipeDetail;
import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.SectionHeader;
import com.esanz.nano.ezbaking.respository.model.Step;
import com.esanz.nano.ezbaking.ui.adapter.DetailsAdapter;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModel;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private DetailsAdapter mDetailsAdapter = new DetailsAdapter();

    private RecipeViewModel mRecipeViewModel;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.content)
    RecyclerView mContent;

    public static Intent createIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipe.id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContent.setAdapter(mDetailsAdapter);

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (-1 != recipeId) {
            RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(
                    EzBakingApplication.RECIPE_REPOSITORY, recipeId);
            mRecipeViewModel = ViewModelProviders.of(this, recipeViewModelFactory)
                    .get(RecipeViewModel.class);
            mRecipeViewModel.getRecipe()
                    .observe(this, this::bindRecipe);
        }
    }

    private void bindRecipe(final Recipe recipe) {
        getSupportActionBar().setTitle(recipe.name);

        List<RecipeDetail> details = new ArrayList<>();
        if (null != recipe.ingredients && !recipe.ingredients.isEmpty()) {
            details.add(new SectionHeader(Ingredient.TITLE));
            details.addAll(recipe.ingredients);
        }
        if (null != recipe.steps && !recipe.steps.isEmpty()) {
            details.add(new SectionHeader(Step.TITLE));
            details.addAll(recipe.steps.subList(1, recipe.steps.size()));
        }

        mDetailsAdapter.setDetails(details);
    }

}
