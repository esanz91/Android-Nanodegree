package com.esanz.nano.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.dao.FavoriteDao;
import com.esanz.nano.movies.repository.dao.MovieDao;
import com.esanz.nano.movies.repository.model.Favorite;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.ui.widget.CheckableFloatingActionButton;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    public static final int RESULT_RELOAD_FAVORITES = 4;

    private static final String EXTRA_MOVIE_ID = "movie_id";

    private boolean isUpdate = false;
    private MovieDao mMovieDao;
    private FavoriteDao mFavoriteDao;

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

        mMovieDao = MovieApplication.movieDatabase.moviesDao();
        mFavoriteDao = MovieApplication.movieDatabase.favoritesDao();

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (movieId != -1) {
            Maybe.zip(
                    mMovieDao.findById(movieId),
                    mFavoriteDao.findById(movieId).map(favorite -> true).defaultIfEmpty(false),
                    Pair::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindMovie);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(isUpdate? RESULT_RELOAD_FAVORITES : RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindMovie(@NonNull final Pair<Movie, Boolean> pair) {
        Movie movie = pair.first;
        Boolean isFavorite = pair.second;

        mAction.setChecked(isFavorite);
        mAction.setOnClickListener(v -> {
            isUpdate = true;
            if (mAction.isChecked()) {
                Completable.fromAction(() ->
                        mFavoriteDao.insert(new Favorite(movie.id)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            } else {
                Completable.fromAction(() ->
                        mFavoriteDao.deleteById(movie.id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
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
    }

}
