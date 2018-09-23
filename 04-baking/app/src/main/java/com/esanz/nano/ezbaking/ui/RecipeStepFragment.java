package com.esanz.nano.ezbaking.ui;

import android.content.Context;
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
import com.google.android.exoplayer2.PlaybackPreparer;
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

public class RecipeStepFragment extends Fragment
        implements PlaybackPreparer {

    private static final String ARG_STEP = "step";
    private static final String STATE_STEP = "step";

    // TODO save exoplayer state correctly on orientation change for tablet layouts
    // saving video position not working (also tried window position)
    private static final String STATE_VIDEO_POSITION = "video_position";
    private static final String STATE_VIDEO_AUTO_PLAY = "video_auto_play";
    private long mVideoPosition = C.TIME_UNSET;
    private boolean mVideoAutoPlay = true;

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
            mVideoPosition = savedInstanceState.getLong(STATE_VIDEO_POSITION);
            mVideoAutoPlay = savedInstanceState.getBoolean(STATE_VIDEO_AUTO_PLAY);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STATE_STEP, mStep);
        if (null != mPlayer) {
            outState.putLong(STATE_VIDEO_POSITION, Math.max(0, mPlayer.getContentPosition()));
            outState.putBoolean(STATE_VIDEO_AUTO_PLAY, mPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    public void bindStep(@NonNull final Step step, boolean reset) {
        if (reset) {
            mStep = step;
            if (mStep.hasVideoUrl()) initializePlayer();
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
            mPlayerView.setPlaybackPreparer(this);
        }

        mMediaSource = getMediaSource(Uri.parse(mStep.videoURL));
        mPlayer.prepare(mMediaSource);
        mPlayer.setPlayWhenReady(mVideoAutoPlay);
        if (mVideoPosition != C.TIME_UNSET) {
            mPlayer.seekTo(mVideoPosition);
        }
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

    private void releasePlayer() {
        if (null != mPlayer) {
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
            mVideoPosition = Math.max(0, mPlayer.getContentPosition());
            mVideoAutoPlay = mPlayer.getPlayWhenReady();
        }
    }

}
