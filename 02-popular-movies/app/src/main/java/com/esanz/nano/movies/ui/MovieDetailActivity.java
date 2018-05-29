package com.esanz.nano.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    // TODO store movies in Room and just pass in the id
    public static Intent createIntent(@NonNull final Context context, @NonNull final Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindMovie(getIntent().getParcelableExtra(EXTRA_MOVIE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindMovie(@NonNull final Movie movie) {
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
    }

}
