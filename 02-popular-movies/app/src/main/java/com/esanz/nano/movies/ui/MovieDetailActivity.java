package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.repository.model.MovieDetail;
import com.esanz.nano.movies.repository.model.MovieReview;
import com.esanz.nano.movies.ui.adapter.MovieReviewAdapter;
import com.esanz.nano.movies.ui.viewModel.MovieDetailsViewModel;
import com.esanz.nano.movies.ui.viewModel.MovieDetailsViewModelFactory;
import com.esanz.nano.movies.ui.widget.CheckableFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final int RESULT_RELOAD_FAVORITES = 4;

    private static final String EXTRA_MOVIE_ID = "movie_id";

    private boolean isUpdate = false;
    private MovieReviewAdapter movieReviewAdapter = new MovieReviewAdapter();
    private SnapHelper snapHelper = new LinearSnapHelper();
    private MovieDetailsViewModel movieViewModel;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.action)
    CheckableFloatingActionButton mAction;

    @BindView(R.id.backdrop)
    ImageView backdropView;

    @BindView(R.id.poster)
    ImageView posterView;

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.release_date)
    TextView releaseDateView;

    @BindView(R.id.rating)
    TextView ratingView;

    @BindView(R.id.overview)
    TextView overviewView;

    @BindView(R.id.reviews_container)
    LinearLayout reviewsContainer;

    @BindView(R.id.reviews)
    RecyclerView reviewList;

    public static Intent createIntent(@NonNull final Context context, @NonNull final Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie.id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reviewList.setAdapter(movieReviewAdapter);
        snapHelper.attachToRecyclerView(reviewList);

        MovieDetailsViewModelFactory movieFactory = new MovieDetailsViewModelFactory(
                MovieApplication.movieRepository);
        movieViewModel = ViewModelProviders.of(this, movieFactory)
                .get(MovieDetailsViewModel.class);

        movieViewModel.movieDetailsLiveData.observe(this, this::bindMovie);

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (movieId != -1) {
            movieViewModel.loadMovieDetails(movieId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(isUpdate ? RESULT_RELOAD_FAVORITES : RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindMovie(@NonNull final MovieDetail movieDetail) {
        Movie movie = movieDetail.movie;
        boolean isFavorite = movieDetail.isFavorite;
        List<MovieReview> reviews = movieDetail.reviews;

        mAction.setChecked(isFavorite);
        mAction.setOnClickListener(v -> {
            isUpdate = true;
            if (mAction.isChecked()) {
                movieViewModel.addFavoriteMovie(movie);
            } else {
                movieViewModel.deleteFavoriteMovie(movie);
            }
        });

        collapsingToolbar.setTitle(movie.title);
        Picasso.with(this)
                .load(movie.getBackdropUrlString())
                .into(backdropView);
        Picasso.with(this)
                .load(movie.getPosterUrlString())
                .into(posterView);

        titleView.setText(movie.title);
        releaseDateView.setText(movie.getReleaseDate());
        ratingView.setText(getString(R.string.rating, movie.voteAverage, Movie.MAX_RATING));
        overviewView.setText(movie.overview);

        if (!reviews.isEmpty()) {
            reviewsContainer.setVisibility(View.VISIBLE);
            movieReviewAdapter.setReviews(reviews);
        }
    }

}
