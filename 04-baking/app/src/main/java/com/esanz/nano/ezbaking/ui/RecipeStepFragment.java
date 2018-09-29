package com.esanz.nano.ezbaking.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.respository.model.Step;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModel;
import com.esanz.nano.ezbaking.ui.viewmodel.RecipeViewModelFactory;
import com.esanz.nano.ezbaking.utils.ViewUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jakewharton.rxrelay2.BehaviorRelay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeStepFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_STEP_POSITION = "step_position";

    private static final String STATE_RECIPE_ID = "recipe_id";
    private static final String STATE_STEP_POSITION = "step_position";
    private static final String STATE_VIDEO_AUTO_PLAY = "video_auto_play";
    private static final String STATE_VIDEO_POSITION = "video_position";
    private static final String STATE_WINDOW_POSITION = "window_position";

    private boolean mVideoAutoPlay = true;
    private long mVideoPosition = C.TIME_UNSET;
    private int mWindowPosition = C.INDEX_UNSET;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorRelay<Boolean> mIsVisible = BehaviorRelay.createDefault(false);

    private int mRecipeId;
    private int mStepPosition;
    private String mVideoUrl;
    private RecipeViewModel mRecipeViewModel;
    private SimpleExoPlayer mPlayer;
    private DefaultTrackSelector mTrackSelector;
    private MediaSource mMediaSource;
    private Unbinder mUnbinder;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.description)
    TextView mDescription;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    public static RecipeStepFragment newInstance(final int recipeId, final int stepPosition) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_POSITION, stepPosition);
        fragment.setArguments(args);
        return fragment;
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
            mRecipeId = savedInstanceState.getInt(STATE_RECIPE_ID);
            mStepPosition = savedInstanceState.getInt(STATE_STEP_POSITION);
            mVideoAutoPlay = savedInstanceState.getBoolean(STATE_VIDEO_AUTO_PLAY);
            mVideoPosition = savedInstanceState.getLong(STATE_VIDEO_POSITION);
            mWindowPosition = savedInstanceState.getInt(STATE_WINDOW_POSITION);
        } else if (null != getArguments()) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            mStepPosition = getArguments().getInt(ARG_STEP_POSITION);
        }

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory(
                EzBakingApplication.RECIPE_REPOSITORY, mRecipeId);
        mRecipeViewModel = ViewModelProviders.of(this, recipeViewModelFactory)
                .get(RecipeViewModel.class);

        disposables.add(Observable.combineLatest(
                mIsVisible,
                mRecipeViewModel.getRecipe(),
                Pair::new
        ).subscribe(result -> {
            boolean isVisible = result.first;
            Recipe recipe = result.second;
            Step step = recipe.steps.get(mStepPosition);
            mVideoUrl = step.videoURL;

            // setup UI
            mPlayerView.setVisibility(step.hasVideoUrl() ? View.VISIBLE : View.GONE);
            mDescription.setText(step.description);

            // setup video
            if (!TextUtils.isEmpty(mVideoUrl)) {
                if (isVisible) {
                    mVideoAutoPlay = true;
                    initializePlayer(mVideoUrl);
                } else {
                    pausePlayer();
                }
            }
        }));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible.accept(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsVisible.accept(getUserVisibleHint());
        if (!TextUtils.isEmpty(mVideoUrl) && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer(mVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mVideoUrl) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || mPlayer == null)) {
            initializePlayer(mVideoUrl);
        }
    }

    @Override
    public void onPause() {
        mIsVisible.accept(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_RECIPE_ID, mRecipeId);
        outState.putInt(STATE_STEP_POSITION, mStepPosition);

        updateStartPosition();
        outState.putBoolean(STATE_VIDEO_AUTO_PLAY, mVideoAutoPlay);
        outState.putLong(STATE_VIDEO_POSITION, mVideoPosition);
        outState.putInt(STATE_WINDOW_POSITION, mWindowPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        disposables.dispose();
        super.onDestroyView();
    }

    public void bindStep(@NonNull final Step step) {
        // setup UI
        mVideoUrl = step.videoURL;
        mPlayerView.setVisibility(step.hasVideoUrl() ? View.VISIBLE : View.GONE);
        mDescription.setText(step.description);

        // setup video
        if (step.hasVideoUrl()) {
            mVideoAutoPlay = true;
            initializePlayer(mVideoUrl);
        } else {
            pausePlayer();
        }
    }

    private void initializePlayer(String url) {
        if (null == mPlayer) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            mPlayerView.setPlayer(mPlayer);
        }

        mMediaSource = getMediaSource(Uri.parse(url));
        boolean haveStartPosition = mWindowPosition != C.INDEX_UNSET;
        if (haveStartPosition) {
            mPlayer.seekTo(mWindowPosition, mVideoPosition);
        }
        mPlayer.setPlayWhenReady(mVideoAutoPlay);
        mPlayer.prepare(mMediaSource, !haveStartPosition, false);
    }

    private MediaSource getMediaSource(@NonNull final Uri videoUri) {
        Context context = getContext();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(getContext(), ViewUtils.getApplicationName(context)),
                bandwidthMeter);
        return new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri);
    }

    private void updateStartPosition() {
        if (null != mPlayer) {
            mVideoAutoPlay = mPlayer.getPlayWhenReady();
            mVideoPosition = Math.max(0, mPlayer.getContentPosition());
            mWindowPosition = mPlayer.getCurrentWindowIndex();
        }
    }

    private void releasePlayer() {
        if (null != mPlayer) {
            updateStartPosition();
            mPlayerView.setPlayer(null);
            mPlayer.release();
            mPlayer = null;
            mMediaSource = null;
            mTrackSelector = null;
        }
    }

    private void pausePlayer() {
        if (null != mPlayer) {
            mPlayer.setPlayWhenReady(false);
            mVideoAutoPlay = mPlayer.getPlayWhenReady();
        }
    }

}
