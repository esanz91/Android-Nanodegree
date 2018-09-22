package com.esanz.nano.ezbaking.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment {

    private static final String ARG_STEP = "step";
    private static final String STATE_STEP = "step";

    private Step mStep;
    private Unbinder mUnbinder;

    @BindView(R.id.description)
    TextView mDescription;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    public static RecipeStepFragment newInstance(@NonNull final Step step) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null != savedInstanceState) {
            mStep = savedInstanceState.getParcelable(STATE_STEP);
        }

        if (null != mStep) {
            bindStep(mStep);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STATE_STEP, mStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public void bindStep(@NonNull final Step step) {
        // TODO add video
        mStep = step;
        mDescription.setText(step.description);
    }
}
