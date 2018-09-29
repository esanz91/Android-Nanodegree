package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
    private static final String EXTRA_STEP_POSITION = "step_position";

    private static final String STATE_RECIPE_ID = "recipe_id";
    private static final String STATE_STEP_POSITION = "step_position";

    private int mRecipeId;
    private int mStepPosition;
    private RecipeViewModel mRecipeViewModel;
    private FragmentStatePagerAdapter mPagerAdaper;

    @BindView(R.id.root)
    View mRoot;

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
                                      final int stepPosition) {
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_STEP_POSITION, stepPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
            getSupportActionBar().hide();
            showViewPagerNavigation(false);
        }
        else {
            showSystemUI();
            getSupportActionBar().show();
            showViewPagerNavigation(true);
        }

        if (null == savedInstanceState) {
            mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, mRecipeId);
            mStepPosition = getIntent().getIntExtra(EXTRA_STEP_POSITION, mStepPosition);
        } else {
            mRecipeId = savedInstanceState.getInt(STATE_RECIPE_ID, mRecipeId);
            mStepPosition = savedInstanceState.getInt(STATE_STEP_POSITION, mStepPosition);
        }

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int stepIndex) {
                mStepPosition = stepIndex;
                setUpNavigation();
                mProgressBar.setProgress(mStepPosition);
            }
        });

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(
                EzBakingApplication.RECIPE_REPOSITORY, mRecipeId);
        mRecipeViewModel = ViewModelProviders.of(this, recipeViewModelFactory)
                .get(RecipeViewModel.class);

        mRecipeViewModel.getRecipe()
                .subscribe(recipe -> {
                    getSupportActionBar().setTitle(recipe.name);

                    if (null == mPagerAdaper) {
                        init(recipe);
                    }

                    int totalSteps = mPagerAdaper.getCount();
                    mProgressBar.setMax(totalSteps - 1);
                    mProgressBar.setProgress(mStepPosition);
                    mPager.setCurrentItem(mStepPosition);
                    setUpNavigation();
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RECIPE_ID, mRecipeId);
        outState.putInt(STATE_STEP_POSITION, mStepPosition);
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.action_back, R.id.action_next})
    public void onNavigationClick(View view) {
        mStepPosition = mPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.action_back:
                mStepPosition -= 1;
                break;
            case R.id.action_next:
                mStepPosition += 1;
                break;
        }
        mPager.setCurrentItem(mStepPosition);
    }

    private void init(@NonNull final Recipe recipe) {
        mPagerAdaper = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(final int stepIndex) {
                return RecipeStepFragment.newInstance(recipe.id, stepIndex);
            }

            @Override
            public int getCount() {
                return recipe.steps.size();
            }
        };
        mPager.setAdapter(mPagerAdaper);
    }

    private void setUpNavigation() {
        if (mStepPosition == 0) {
            mBackButton.setEnabled(false);
            if (mStepPosition < mPagerAdaper.getCount() - 1) {
                mNextButton.setEnabled(true);
            }
        } else if (mStepPosition == mPagerAdaper.getCount() - 1) {
            mNextButton.setEnabled(false);
            mBackButton.setEnabled(true);
        } else {
            mBackButton.setEnabled(true);
            mNextButton.setEnabled(true);
        }
    }

    public void showViewPagerNavigation(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mBackButton.setVisibility(visibility);
        mNextButton.setVisibility(visibility);
        mProgressBar.setVisibility(visibility);
        mRoot.setFitsSystemWindows(show);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
