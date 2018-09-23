package com.esanz.nano.ezbaking.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;
import com.esanz.nano.ezbaking.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.OnStepClickListener {

    private static final String EXTRA_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_DETAIL = "recipe_detail";
    private static final String TAG_RECIPE_STEP = "recipe_step";

    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean mIsTwoPane = false;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent createIntent(@NonNull final Context context, @NonNull final Recipe recipe) {
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

        determinePaneLayout();

        if (null == savedInstanceState) {
            initDetails();

            if (mIsTwoPane) {
                int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
                if (recipeId != -1) {
                    disposables.add(EzBakingApplication.RECIPE_REPOSITORY.getRecipe(recipeId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(recipe -> initStep(recipe.steps.get(0))));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();

        super.onDestroy();
    }

    @Override
    public void onStepClick(final int recipeId, @NonNull final Step step) {
        if (mIsTwoPane) {
            RecipeStepFragment stepFragment = (RecipeStepFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_RECIPE_STEP);
            if (null == stepFragment) {
                initStep(step);
            } else {
                stepFragment.bindStep(step, true);
            }
        } else {
            startActivity(RecipeStepsActivity.createIntent(this, recipeId, step.id));
        }
    }

    private void determinePaneLayout() {
        FrameLayout stepFragment = findViewById(R.id.recipe_step_container);
        mIsTwoPane = null != stepFragment;
    }

    private void initDetails() {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(FragmentUtils.intentToArguments(getIntent()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_container, detailFragment, TAG_RECIPE_DETAIL)
                .commit();
    }

    private void initStep(@NonNull final Step step) {
        RecipeStepFragment stepFragment = RecipeStepFragment.newInstance(step);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_container, stepFragment, TAG_RECIPE_STEP)
                .commit();
    }
}
