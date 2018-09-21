package com.esanz.nano.ezbaking.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.RecipeDetailFragment;
import com.esanz.nano.ezbaking.respository.RecipeDetail;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
    implements RecipeDetailFragment.OnStepClickListener {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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

        if (savedInstanceState == null) {
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(FragmentUtils.intentToFragmentArguments(getIntent()));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, "recipe_detail")
                    .commit();
        }
    }

    @Override
    public void onStepClick() {
        // TODO
    }
}
