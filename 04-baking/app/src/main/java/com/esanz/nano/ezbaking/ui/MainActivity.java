package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.ui.adapter.RecipesAdapter;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipesViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipes)
    RecyclerView mRecipesView;

    private RecipesViewModel mRecipesViewModel;
    private RecipesAdapter mRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mRecipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mRecipesAdapter = new RecipesAdapter(mRecipesViewModel.getRecipes());
        mRecipesView.setAdapter(mRecipesAdapter);
    }

}
