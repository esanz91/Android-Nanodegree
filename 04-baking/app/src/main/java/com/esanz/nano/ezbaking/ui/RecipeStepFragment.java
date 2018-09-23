package com.esanz.nano.ezbaking.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment {

    private static final String ARG_STEP = "step";
    private static final String STATE_STEP = "step";
    private static final String STATE_VIDEO_AUTO_PLAY = "video_auto_play";
    private static final String STATE_VIDEO_POSITION = "video_position";
    private static final String STATE_WINDOW_POSITION = "window_position";

    private boolean mVideoAutoPlay = true;
    private long mVideoPosition = C.TIME_UNSET;
    private int mWindowPosition = C.INDEX_UNSET;

    private Step mStep;
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

        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
                        RecipeStepsActivity activity = (RecipeStepsActivity) getActivity();
                        if (null != activity) {
                            activity.getSupportActionBar().hide();
                            activity.showViewPagerNavigation(false);
                        }
                    }
                });
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
            mVideoAutoPlay = savedInstanceState.getBoolean(STATE_VIDEO_AUTO_PLAY);
            mVideoPosition = savedInstanceState.getLong(STATE_VIDEO_POSITION);
            mWindowPosition = savedInstanceState.getInt(STATE_WINDOW_POSITION);
        }

        if (null != mStep) {
            bindStep(mStep, false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            pausePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mStep.hasVideoUrl() && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStep.hasVideoUrl() && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStep.hasVideoUrl() && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mStep.hasVideoUrl() && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showSystemUI();

            RecipeStepsActivity activity = (RecipeStepsActivity) getActivity();
            if (null != activity) {
                activity.getSupportActionBar().show();
                activity.showViewPagerNavigation(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STATE_STEP, mStep);
        if (null != mPlayer) {
            updateStartPosition();
            outState.putBoolean(STATE_VIDEO_AUTO_PLAY, mVideoAutoPlay);
            outState.putLong(STATE_VIDEO_POSITION, mVideoPosition);
            outState.putInt(STATE_WINDOW_POSITION, mWindowPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public void bindStep(@NonNull final Step step, boolean reset) {
        if (reset) {
            mStep = step;
            if (mStep.hasVideoUrl()) {
                mVideoAutoPlay = true;
                initializePlayer();
            } else {
                pausePlayer();
            }
        }

        mPlayerView.setVisibility(mStep.hasVideoUrl() ? View.VISIBLE : View.GONE);
        mDescription.setText(mStep.description);
    }

    private void initializePlayer() {
        if (null == mPlayer) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            mPlayerView.setPlayer(mPlayer);
        }

        boolean haveStartPosition = mWindowPosition != C.INDEX_UNSET;
        if (haveStartPosition) {
            mPlayer.seekTo(mWindowPosition, mVideoPosition);
        }

        mMediaSource = getMediaSource(Uri.parse(mStep.videoURL));
        mPlayer.prepare(mMediaSource, !haveStartPosition, false);
        mPlayer.setPlayWhenReady(mVideoAutoPlay);
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
        if (mPlayer != null) {
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

    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
