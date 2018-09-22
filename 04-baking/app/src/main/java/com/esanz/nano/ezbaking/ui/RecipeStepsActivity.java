package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModel;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepsActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "recipe_id";
    private static final String EXTRA_STEP_ID = "step_id";

    private int mRecipeId;
    private int mStepId;
    private RecipeViewModel mRecipeViewModel;
    private FragmentStatePagerAdapter mPagerAdaper;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.action_back)
    Button mBackButton;

    @BindView(R.id.action_next)
    Button mNextButton;

    public static Intent createIntent(@NonNull final Context context,
                                      final int recipeId,
                                      final int stepId) {
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_STEP_ID, stepId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, mRecipeId);
        mStepId = getIntent().getIntExtra(EXTRA_STEP_ID, mStepId);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int stepIndex) {
                setUpNavigation(stepIndex);
            }
        });

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(
                EzBakingApplication.RECIPE_REPOSITORY, mRecipeId);
        mRecipeViewModel = ViewModelProviders.of(this, recipeViewModelFactory)
                .get(RecipeViewModel.class);

        mRecipeViewModel.getRecipe()
                .observe(this, (Recipe recipe) -> {
                    getSupportActionBar().setTitle(recipe.name);

                    if (null == mPagerAdaper) {
                        init(recipe);
                    }

                    int currentStep = recipe.getStepIndexFromId(mStepId);
                    int totalSteps = mPagerAdaper.getCount();
                    mProgressBar.setProgress(currentStep / totalSteps);
                    mPager.setCurrentItem(currentStep);
                    setUpNavigation(currentStep);
                });
    }

    @OnClick({R.id.action_back, R.id.action_next})
    public void onNavigationClick(View view) {
        int currentStep = mPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.action_back:
                mPager.setCurrentItem(--currentStep);
                break;
            case R.id.action_next:
                mPager.setCurrentItem(++currentStep);
                break;
        }
    }

    private void init(@NonNull final Recipe recipe) {
        mPagerAdaper = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(final int stepIndex) {
                return RecipeStepFragment.newInstance(recipe.steps.get(stepIndex));
            }

            @Override
            public int getCount() {
                return recipe.steps.size();
            }
        };
        mPager.setAdapter(mPagerAdaper);
    }

    private void setUpNavigation(final int stepIndex) {
        if (stepIndex == 0) {
            mBackButton.setEnabled(false);
            if (stepIndex < mPagerAdaper.getCount() - 1) {
                mNextButton.setEnabled(true);
            }
        } else if (stepIndex == mPagerAdaper.getCount() - 1) {
            mNextButton.setEnabled(false);
            mBackButton.setEnabled(true);
        } else {
            mBackButton.setEnabled(true);
            mNextButton.setEnabled(true);
        }
    }
}
