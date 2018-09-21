package com.esanz.nano.ezbaking;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import butterknife.Unbinder;


public class RecipeDetailFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "recipe_id";

    private DetailsAdapter mDetailsAdapter = new DetailsAdapter();
    private RecipeViewModel mRecipeViewModel;
    private OnStepClickListener mListener;
    private Unbinder mUnbinder;
    private int mRecipeId;

    @BindView(R.id.content)
    RecyclerView mContent;

    public interface OnStepClickListener {
        void onStepClick(); // TODO
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(
                EzBakingApplication.RECIPE_REPOSITORY, mRecipeId);
        mRecipeViewModel = ViewModelProviders.of(this, recipeViewModelFactory)
                .get(RecipeViewModel.class);
        mRecipeViewModel.getRecipe()
                .observe(this, this::bindRecipe);

        mContent.setAdapter(mDetailsAdapter);
    }

    @Override
    public void onDetach() {
        mListener = null;
        mUnbinder.unbind();
        super.onDetach();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onStepClick();
        }
    }

    private void bindRecipe(final Recipe recipe) {
        getActivity().setTitle(recipe.name);

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
